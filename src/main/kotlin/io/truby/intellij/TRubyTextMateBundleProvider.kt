package io.truby.intellij

import com.intellij.openapi.application.PathManager
import org.jetbrains.plugins.textmate.api.TextMateBundleProvider
import java.nio.file.Files
import java.nio.file.Path

/**
 * TextMate bundle provider for T-Ruby syntax highlighting.
 *
 * This class provides the TextMate grammar bundle to JetBrains IDEs,
 * enabling syntax highlighting for .trb and .d.trb files.
 *
 * Note: Resources inside JAR files cannot be accessed directly via Path.of(uri)
 * because the ZIP filesystem is not automatically mounted. Instead, we extract
 * the bundle files to a temporary directory.
 */
class TRubyTextMateBundleProvider : TextMateBundleProvider {
    override fun getBundles(): List<TextMateBundleProvider.PluginBundle> {
        return try {
            val bundleTmpDir = Files.createTempDirectory(
                Path.of(PathManager.getTempPath()),
                "textmate-t-ruby"
            )

            val filesToCopy = listOf(
                "textmate-bundle/package.json",
                "textmate-bundle/language-configuration.json",
                "textmate-bundle/syntaxes/t-ruby.tmLanguage.json"
            )

            for (fileToCopy in filesToCopy) {
                val resource = javaClass.classLoader.getResource(fileToCopy)
                    ?: continue

                resource.openStream().use { inputStream ->
                    // Remove "textmate-bundle/" prefix for target path
                    val relativePath = fileToCopy.removePrefix("textmate-bundle/")
                    val target = bundleTmpDir.resolve(relativePath)
                    Files.createDirectories(target.parent)
                    Files.copy(inputStream, target)
                }
            }

            listOf(TextMateBundleProvider.PluginBundle("T-Ruby", bundleTmpDir))
        } catch (e: Exception) {
            emptyList()
        }
    }
}
