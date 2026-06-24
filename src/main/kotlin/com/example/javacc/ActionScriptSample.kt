package com.example.javacc

/**
 * Command-line entry point for validating the built-in ActionScript sample.
 */
object ActionScriptSample {
    /**
     * Validates the supplied ActionScript text or the bundled sample when no arguments are provided.
     *
     * @param args Optional lines of ActionScript input to validate.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val script =
            if (args.isNotEmpty()) {
                args.joinToString("\n")
            } else {
                sampleActionScript
            }

        val result = ActionScriptValidator.validate(script)

        println("ActionScript validation result: ${if (result.valid) "valid" else "invalid"}")

        if (result.errors.isNotEmpty()) {
            println("Errors:")
            result.errors.forEach { error -> println("- $error") }
        }
    }
}

private val sampleActionScript =
    """
    Title
    Description


    <char0> [RUN] <book> (12)
    <char0> [FIND] <book> (12)
    <char0> [READ] <book> (12)
    <char0> [WALK] <sofa> (244)
    <char0> [SIT] <sofa> (244)
    """.trimIndent()
