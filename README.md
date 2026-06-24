# Kotlin ANTLR ActionScript Validator

Kotlin ANTLR sample project for validating a small ActionScript-like grammar.

## What It Does

- Parses input with ANTLR-generated lexer and parser code.
- Validates text through `ActionScriptValidator`.
- Provides a small console entry point via `MainKt`.
- Includes a sample runner task for validating built-in example input.

## Project Layout

- `src/main/antlr/ActionScript.g4`: ANTLR grammar source.
- `src/main/kotlin/com/example/javacc/ActionScriptValidator.kt`: validation logic.
- `src/main/kotlin/com/example/javacc/ActionScriptSample.kt`: sample command-line runner.
- `src/main/kotlin/com/example/javacc/Main.kt`: application entry point.
- `src/test/kotlin/com/example/javacc/ActionScriptValidatorTest.kt`: validator tests.

Generated ANTLR Kotlin sources are written to `build/generatedAntlr/com/example/javacc/generated`.

## Requirements

- JDK 17
- Gradle wrapper

## Common Tasks

Run the full build:

```bash
./gradlew build
```

Run tests:

```bash
./gradlew test
```

Run the application:

```bash
./gradlew run
```

Run the bundled sample validator:

```bash
./gradlew runActionScriptSample
```

Pass custom input to the sample runner:

```bash
./gradlew runActionScriptSample -Pscript=$'Title\nDescription\n\n\n<char0> [RUN] <book> (12)'
```

Build the shaded jar:

```bash
./gradlew shadowJar
```

Format Kotlin and ANTLR sources:

```bash
./gradlew ktlintFormat spotlessApply
```

## Notes

- `build` runs the normal compile, test, and verification tasks. Run `shadowJar` separately when you want the shaded artifact.
- The ANTLR grammar is kept under `src/main/antlr`, and the generated Kotlin sources are created during the build.
