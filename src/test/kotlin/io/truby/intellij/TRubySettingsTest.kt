package io.truby.intellij

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import io.truby.intellij.settings.TRubySettings
import org.junit.jupiter.api.Assertions.*

/**
 * Tests for T-Ruby settings and configuration.
 */
class TRubySettingsTest : BasePlatformTestCase() {

    fun testDefaultSettings() {
        val settings = TRubySettings.instance
        assertNotNull(settings)

        // Check default values
        assertEquals("trc", settings.trcPath)
        assertTrue(settings.enableLsp)
    }

    fun testSettingsPersistence() {
        val settings = TRubySettings.instance

        // Modify settings
        settings.trcPath = "/custom/path/trc"
        settings.enableLsp = false

        // Verify changes
        assertEquals("/custom/path/trc", settings.trcPath)
        assertFalse(settings.enableLsp)

        // Reset to defaults for other tests
        settings.trcPath = "trc"
        settings.enableLsp = true
    }
}
