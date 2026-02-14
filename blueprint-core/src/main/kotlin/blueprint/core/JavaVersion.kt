package blueprint.core

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.initialization.Settings
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.NONE
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.File

public fun Project.javaVersion(): Provider<JavaVersion> = javaVersionString().map(JavaVersion::toVersion)

public fun Project.jvmTarget(): Provider<JvmTarget> = javaVersionString().map(JvmTarget::fromTarget)

public fun Project.javaLanguageVersion(): Provider<JavaLanguageVersion> =
  javaVersionString().map(JavaLanguageVersion::of)

public fun Project.javaVersionString(): Provider<String> =
  providers.of(JavaVersionValueSource::class.java) { parameters.javaVersionFile.set(javaVersionFile) }

private val Project.javaVersionFile: RegularFile
  @Suppress("UnstableApiUsage")
  get() = rootProject.isolated.projectDirectory.file(FILENAME)

public fun Settings.javaVersion(): Provider<JavaVersion> = javaVersionString().map(JavaVersion::toVersion)

public fun Settings.jvmTarget(): Provider<JvmTarget> = javaVersionString().map(JvmTarget::fromTarget)

public fun Settings.javaLanguageVersion(): Provider<JavaLanguageVersion> =
  javaVersionString().map(JavaLanguageVersion::of)

public fun Settings.javaVersionString(): Provider<String> =
  providers.of(JavaVersionValueSource::class.java) { parameters.javaVersionFile.set(javaVersionFile) }

private val Settings.javaVersionFile: File
  get() = rootProject.projectDir.resolve(FILENAME)

private const val FILENAME = ".java-version"

private abstract class JavaVersionValueSource : ValueSource<String, JavaVersionValueSource.Parameters> {
  interface Parameters : ValueSourceParameters {
    @get:InputFile
    @get:PathSensitive(NONE)
    val javaVersionFile: RegularFileProperty
  }

  override fun obtain(): String {
    val file = parameters.javaVersionFile.asFile.get()
    require(file.isFile) { "Java version file does not exist: ${file.absolutePath}" }

    val content = file.readText().trim()
    require(content.isNotEmpty()) { "Java version file is empty: ${file.absolutePath}" }

    requireNotNull(content.toIntOrNull()) {
      "Java version must be a valid integer, but was: '$content'"
    }

    return content
  }
}
