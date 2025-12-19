package io.truby.intellij

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.plugins.textmate.api.TextMateBundleProvider
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

/**
 * TextMate bundle provider for T-Ruby syntax highlighting.
 *
 * This class provides the TextMate grammar bundle to JetBrains IDEs,
 * enabling syntax highlighting for .trb and .d.trb files.
 *
 * Note: Resources inside JAR files cannot be accessed directly via Path.of(uri)
 * because the ZIP filesystem is not automatically mounted. Instead, we extract
 * the bundle files to a persistent directory in the IDE's system folder.
 */
class TRubyTextMateBundleProvider : TextMateBundleProvider {

    companion object {
        private val LOG = Logger.getInstance(TRubyTextMateBundleProvider::class.java)
        private const val BUNDLE_DIR_NAME = "t-ruby-textmate-bundle"
    }

    override fun getBundles(): List<TextMateBundleProvider.PluginBundle> {
        return try {
            // Use a persistent directory in IDE's system folder instead of temp
            val systemPath = Path.of(PathManager.getSystemPath())
            val bundleDir = systemPath.resolve(BUNDLE_DIR_NAME)

            // Create the directory if it doesn't exist
            if (!Files.exists(bundleDir)) {
                Files.createDirectories(bundleDir)
            }

            val filesToCopy = listOf(
                "textmate-bundle/package.json",
                "textmate-bundle/language-configuration.json",
                "textmate-bundle/syntaxes/t-ruby.tmLanguage.json",
                "textmate-bundle/themes/t-ruby-darcula.json"
            )

            var allFilesCopied = true
            for (fileToCopy in filesToCopy) {
                val resource = javaClass.classLoader.getResource(fileToCopy)
                if (resource == null) {
                    LOG.warn("T-Ruby TextMate bundle resource not found: $fileToCopy")
                    allFilesCopied = false
                    continue
                }

                resource.openStream().use { inputStream ->
                    // Remove "textmate-bundle/" prefix for target path
                    val relativePath = fileToCopy.removePrefix("textmate-bundle/")
                    val target = bundleDir.resolve(relativePath)
                    Files.createDirectories(target.parent)
                    // Always overwrite to ensure latest version
                    Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING)
                }
            }

            if (allFilesCopied) {
                LOG.info("T-Ruby TextMate bundle loaded from: $bundleDir")
                listOf(TextMateBundleProvider.PluginBundle("T-Ruby", bundleDir))
            } else {
                LOG.warn("T-Ruby TextMate bundle partially loaded, some files missing")
                listOf(TextMateBundleProvider.PluginBundle("T-Ruby", bundleDir))
            }
        } catch (e: Exception) {
            LOG.error("Failed to load T-Ruby TextMate bundle", e)
            emptyList()
        }
    }
}
