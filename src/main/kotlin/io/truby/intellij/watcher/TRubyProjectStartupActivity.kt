package io.truby.intellij.watcher

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import io.truby.intellij.config.TrbConfig

/**
 * Starts the OutputDirectoryWatcher when a T-Ruby project is opened.
 */
class TRubyProjectStartupActivity : ProjectActivity {
    private val logger = Logger.getInstance(TRubyProjectStartupActivity::class.java)

    override suspend fun execute(project: Project) {
        // Only start watcher if this is a T-Ruby project (has trbconfig.yml)
        val config = TrbConfig.load(project)
        if (config == null) {
            logger.debug("No trbconfig.yml found, skipping OutputDirectoryWatcher")
            return
        }

        logger.info("T-Ruby project detected, starting OutputDirectoryWatcher")
        OutputDirectoryWatcher.getInstance(project).start()
    }
}
