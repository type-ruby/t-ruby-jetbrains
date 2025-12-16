package io.truby.intellij

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

/**
 * File type for T-Ruby source files (.trb).
 * Note: Syntax highlighting is handled by TextMate bundle via editorHighlighterProvider.
 */
class TRubyFileType private constructor() : LanguageFileType(TRubyLanguage) {
    companion object {
        @JvmStatic
        val INSTANCE = TRubyFileType()
    }

    override fun getName(): String = "T-Ruby"

    override fun getDescription(): String = "T-Ruby source file"

    override fun getDefaultExtension(): String = "trb"

    override fun getIcon(): Icon = TRubyIcons.FILE
}
