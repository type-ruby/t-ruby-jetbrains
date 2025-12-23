package io.truby.intellij.config

import com.intellij.openapi.project.Project
import java.io.File

/**
 * Parser for trbconfig.yml configuration file.
 * Reads the output directory path for VFS refresh functionality.
 *
 * Uses simple regex-based parsing to avoid SnakeYAML dependency conflicts
 * with IntelliJ Platform's bundled version.
 */
class TrbConfig private constructor(
    val rubyDir: String,
    val rbsDir: String?
) {
    companion object {
        private const val CONFIG_FILE_NAME = "trbconfig.yml"
        private const val DEFAULT_RUBY_DIR = "build"

        // Regex to match "ruby_dir: value" (handles quoted and unquoted values)
        private val RUBY_DIR_PATTERN = Regex("""^\s*ruby_dir:\s*["']?([^"'\s#]+)["']?""", RegexOption.MULTILINE)
        private val RBS_DIR_PATTERN = Regex("""^\s*rbs_dir:\s*["']?([^"'\s#]+)["']?""", RegexOption.MULTILINE)

        /**
         * Load configuration from project root.
         * Returns null if config file doesn't exist.
         */
        fun load(project: Project): TrbConfig? {
            val basePath = project.basePath ?: return null
            val configFile = File(basePath, CONFIG_FILE_NAME)

            if (!configFile.exists()) {
                return null
            }

            return try {
                parse(configFile)
            } catch (e: Exception) {
                // If parsing fails, return default config
                TrbConfig(DEFAULT_RUBY_DIR, null)
            }
        }

        /**
         * Get output directory path, using default if config doesn't exist.
         */
        fun getOutputDir(project: Project): String {
            return load(project)?.rubyDir ?: DEFAULT_RUBY_DIR
        }

        private fun parse(configFile: File): TrbConfig {
            val content = configFile.readText()

            val rubyDir = RUBY_DIR_PATTERN.find(content)?.groupValues?.get(1) ?: DEFAULT_RUBY_DIR
            val rbsDir = RBS_DIR_PATTERN.find(content)?.groupValues?.get(1)

            return TrbConfig(rubyDir, rbsDir)
        }
    }
}
