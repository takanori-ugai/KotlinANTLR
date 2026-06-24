package com.example.javacc

import com.example.javacc.generated.ActionScriptLexer
import com.example.javacc.generated.ActionScriptParser
import org.antlr.v4.kotlinruntime.BaseErrorListener
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.RecognitionException
import org.antlr.v4.kotlinruntime.Recognizer
import org.antlr.v4.kotlinruntime.misc.ParseCancellationException

/**
 * Validation outcome for an ActionScript document.
 *
 * @property valid `true` when the input parsed without errors.
 * @property errors The collected syntax errors, in encounter order.
 */
data class ValidationResult(
    val valid: Boolean,
    val errors: List<String>,
)

/**
 * Validates ActionScript input with the generated ANTLR lexer and parser.
 */
object ActionScriptValidator {
    /**
     * Parses the provided script and returns the validation result.
     *
     * @param script ActionScript text to validate.
     * @return A result containing the validity flag and collected errors.
     */
    fun validate(script: String): ValidationResult {
        val lexer = ActionScriptLexer(CharStreams.fromString(script))
        val parser = ActionScriptParser(CommonTokenStream(lexer))
        val errors = mutableListOf<String>()
        val errorListener = collectingErrorListener(errors)

        lexer.removeErrorListeners()
        parser.removeErrorListeners()
        lexer.addErrorListener(errorListener)
        parser.addErrorListener(errorListener)

        try {
            parser.document()
        } catch (_: ParseCancellationException) {
            // Syntax errors are already collected by the listener.
        }

        return ValidationResult(errors.isEmpty(), errors)
    }

    private fun collectingErrorListener(errors: MutableList<String>): BaseErrorListener =
        object : BaseErrorListener() {
            override fun syntaxError(
                recognizer: Recognizer<*, *>,
                offendingSymbol: Any?,
                line: Int,
                charPositionInLine: Int,
                msg: String,
                e: RecognitionException?,
            ) {
                errors += "Line $line:$charPositionInLine $msg"
            }
        }
}
