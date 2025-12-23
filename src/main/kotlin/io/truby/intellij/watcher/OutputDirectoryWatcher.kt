package io.truby.intellij.watcher

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import io.truby.intellij.config.TrbConfig
import java.io.File
import java.nio.file.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

/**
 * Watches the T-Ruby output directory for changes and triggers VFS refresh.
 * This ensures that files compiled by `trc -w` are immediately visible in the IDE.
 */
@Service(Service.Level.PROJECT)
class OutputDirectoryWatcher(private val project: Project) : Disposable {
    private val logger = Logger.getInstance(OutputDirectoryWatcher::class.java)
    private var watchThread: Thread? = null
    private var watchService: WatchService? = null
    private val isRunning = AtomicBoolean(false)
    private var currentWatchDir: String? = null

    /**
     * Start watching the output directory.
     * Called automatically when project opens.
     */
    fun start() {
        if (isRunning.get()) {
            logger.info("OutputDirectoryWatcher already running")
            return
        }

        val outputDir = TrbConfig.getOutputDir(project)
        val basePath = project.basePath ?: return

        val watchPath = File(basePath, outputDir).toPath()

        // Create directory if it doesn't exist
        if (!Files.exists(watchPath)) {
            try {
                Files.createDirectories(watchPath)
            } catch (e: Exception) {
                logger.warn("Failed to create output directory: $watchPath", e)
                return
            }
        }

        currentWatchDir = watchPath.toString()
        startWatching(watchPath)
    }

    /**
     * Stop watching and clean up resources.
     */
    fun stop() {
        isRunning.set(false)
        watchService?.close()
        watchThread?.interrupt()
        watchThread = null
        watchService = null
        currentWatchDir = null
        logger.info("OutputDirectoryWatcher stopped")
    }

    /**
     * Restart watching (e.g., when config changes).
     */
    fun restart() {
        stop()
        start()
    }

    private fun startWatching(watchPath: Path) {
        try {
            watchService = FileSystems.getDefault().newWatchService()
            registerRecursively(watchPath)

            isRunning.set(true)
            logger.info("Started watching: $watchPath")

            watchThread = thread(name = "T-Ruby-OutputWatcher", isDaemon = true) {
                watchLoop()
            }
        } catch (e: Exception) {
            logger.error("Failed to start watching: $watchPath", e)
        }
    }

    private fun registerRecursively(path: Path) {
        if (!Files.isDirectory(path)) return

        path.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_MODIFY,
            StandardWatchEventKinds.ENTRY_DELETE
        )

        // Register subdirectories
        Files.list(path).use { stream ->
            stream.filter { Files.isDirectory(it) }
                .forEach { registerRecursively(it) }
        }
    }

    private fun watchLoop() {
        while (isRunning.get()) {
            try {
                val key = watchService?.poll(500, java.util.concurrent.TimeUnit.MILLISECONDS)
                    ?: continue

                val events = key.pollEvents()
                if (events.isNotEmpty()) {
                    // Debounce: collect all events before refresh
                    Thread.sleep(100)

                    // Process events
                    for (event in events) {
                        val kind = event.kind()
                        val context = event.context() as? Path ?: continue

                        logger.debug("File event: $kind - $context")

                        // Register new directories for recursive watching
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                            val watchable = key.watchable() as? Path
                            val fullPath = watchable?.resolve(context)
                            if (fullPath != null && Files.isDirectory(fullPath)) {
                                registerRecursively(fullPath)
                            }
                        }
                    }

                    // Trigger VFS refresh
                    refreshVfs()
                }

                if (!key.reset()) {
                    logger.warn("Watch key is no longer valid")
                    break
                }
            } catch (e: InterruptedException) {
                logger.info("Watch thread interrupted")
                break
            } catch (e: ClosedWatchServiceException) {
                logger.info("Watch service closed")
                break
            } catch (e: Exception) {
                logger.warn("Error in watch loop", e)
            }
        }
    }

    private fun refreshVfs() {
        val watchDir = currentWatchDir ?: return

        ApplicationManager.getApplication().invokeLater {
            if (project.isDisposed) return@invokeLater

            val virtualFile = LocalFileSystem.getInstance().findFileByPath(watchDir)
            if (virtualFile != null) {
                VfsUtil.markDirtyAndRefresh(
                    true,  // async
                    true,  // recursive
                    true,  // reload children
                    virtualFile
                )
                logger.debug("VFS refreshed: $watchDir")
            }
        }
    }

    override fun dispose() {
        stop()
    }

    companion object {
        fun getInstance(project: Project): OutputDirectoryWatcher {
            return project.getService(OutputDirectoryWatcher::class.java)
        }
    }
}
