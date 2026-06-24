package com.example.javacc

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Verifies the ActionScript validator against valid and invalid sample inputs.
 */
class ActionScriptValidatorTest {
    /**
     * Confirms the bundled sample script is accepted.
     */
    @Test
    fun `sample action script is valid`() {
        val script =
            """
            Title
            Description


            <char0> [RUN] <book> (12)
            <char0> [FIND] <book> (12)
            <char0> [READ] <book> (12)
            <char0> [WALK] <sofa> (244)
            <char0> [SIT] <sofa> (244)
            """.trimIndent()

        val result = ActionScriptValidator.validate(script)

        assertTrue(result.valid)
        assertTrue(result.errors.isEmpty())
    }

    /**
     * Confirms malformed input is rejected with at least one error.
     */
    @Test
    fun `invalid action script is rejected`() {
        val script =
            """
            Title
            Description


            <char0> RUN <book> (12)
            """.trimIndent()

        val result = ActionScriptValidator.validate(script)

        assertFalse(result.valid)
        assertTrue(result.errors.isNotEmpty())
    }
}
