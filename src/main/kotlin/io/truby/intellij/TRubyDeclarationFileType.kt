package io.truby.intellij

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

/**
 * File type for T-Ruby declaration files (.d.trb).
 *
 * Declaration files contain only type signatures without implementation,
 * similar to TypeScript's .d.ts files.
 * Note: Syntax highlighting is handled by TextMate bundle via editorHighlighterProvider.
 */
class TRubyDeclarationFileType private constructor() : LanguageFileType(TRubyLanguage) {
    companion object {
        @JvmStatic
        val INSTANCE = TRubyDeclarationFileType()
    }

    override fun getName(): String = "T-Ruby Declaration"

    override fun getDescription(): String = "T-Ruby declaration file"

    override fun getDisplayName(): String = "T-Ruby Declaration"

    override fun getDefaultExtension(): String = "d.trb"

    override fun getIcon(): Icon = TRubyIcons.DECLARATION_FILE
}
