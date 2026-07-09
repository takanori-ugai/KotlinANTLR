import com.strumenta.antlrkotlin.gradle.AntlrKotlinTask
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    kotlin("jvm") version "2.4.0"
    kotlin("plugin.serialization") version "2.3.0"
    id("com.strumenta.antlr-kotlin") version "1.0.10"
    java
    id("com.gradleup.shadow") version "9.4.3"
    jacoco
    id("org.jetbrains.dokka") version "2.2.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("org.jlleitschuh.gradle.ktlint") version "14.0.1"
    id("com.github.jk1.dependency-license-report") version "3.1.4"
    id("com.github.spotbugs") version "6.5.8"
    id("com.diffplug.spotless") version "8.8.0"
    application
}

group = "jp.live.ugai"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val generateKotlinGrammarSource =
    tasks.register<AntlrKotlinTask>("generateKotlinGrammarSource") {
//        dependsOn("cleanGenerateKotlinGrammarSource")

        source =
            fileTree(layout.projectDirectory.dir("src/main/antlr")) {
                include("**/*.g4")
            }

        packageName = "com.example.javacc.generated"
        arguments = listOf("-visitor")
        outputDirectory =
            layout.buildDirectory
                .dir("generatedAntlr/com/example/javacc/generated")
                .get()
                .asFile
    }

dependencies {
    implementation("com.strumenta:antlr-kotlin-runtime:1.0.10")
    testImplementation("org.junit.jupiter:junit-jupiter:6.1.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.1.1")
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget = JvmTarget.JVM_17
        dependsOn(generateKotlinGrammarSource)
        source(layout.buildDirectory.dir("generatedAntlr/com/example/javacc/generated"))
    }

    compileTestKotlin {
        compilerOptions.jvmTarget = JvmTarget.JVM_17
    }

    compileJava {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    compileTestJava {
        options.encoding = "UTF-8"
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport) // report is always generated after tests run
    }

    withType<Detekt>().configureEach {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        jvmTarget = "17"
        reports {
            // observe findings in your browser with structure and code snippets
            html.required.set(true)
            // checkstyle like format mainly for integrations like Jenkins
            xml.required.set(true)
            // similar to the console output, contains issue signature to manually edit baseline files
            txt.required.set(true)
            // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations
            // with Github Code Scanning
            sarif.required.set(true)
        }
    }

    jacocoTestReport {
        dependsOn(test) // tests are required to run before generating the report
    }

    register<JavaExec>("execute") {
        group = "application"
        mainClass.set(
            if (project.hasProperty("mainClass")) {
                project.property("mainClass") as String
            } else {
                application.mainClass.get()
            },
        )
        classpath = sourceSets.main.get().runtimeClasspath
    }

    register<JavaExec>("runActionScriptSample") {
        group = "application"
        description = "Runs the ActionScript ANTLR sample validator."
        dependsOn("classes")
        mainClass.set("com.example.javacc.ActionScriptSample")
        classpath = sourceSets.main.get().runtimeClasspath

        if (project.hasProperty("script")) {
            args(project.property("script").toString())
        }
    }
}

application {
    mainClass.set("com.example.javacc.MainKt")
    applicationDefaultJvmArgs = listOf("--add-modules=jdk.incubator.vector")
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    filter {
        include("src/**/kotlin/**")
        exclude("build/**/generatedAntlr/**")
    }
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.JSON)
        reporter(ReporterType.HTML)
    }
}

/*
tasks.named("ktlintMainSourceSetCheck") {
    enabled = false
}

tasks.named("ktlintMainSourceSetFormat") {
    enabled = false
}

tasks.named("build") {
    dependsOn("shadowJar")
}
*/

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("$projectDir/config/detekt.yml")
}

spotbugs {
    ignoreFailures.set(true)
}

spotless {
    java {
        target("src/*/java/**/*.java")
        // Use the default importOrder configuration
        importOrder()
        removeUnusedImports()

        // Choose one of these formatters.
        googleJavaFormat("1.27.0") // has its own section below
        formatAnnotations() // fixes formatting of type annotations, see below
    }
    antlr4 {
        target("src/*/antlr/**/*.g4") // default value, you can change if you want
        antlr4Formatter() // has its own section below
    }
}

dokka.dokkaSourceSets {
    configureEach {
        jdkVersion.set(17)
        enableJdkDocumentationLink.set(false)
        enableKotlinStdLibDocumentationLink.set(false)
    }
}
