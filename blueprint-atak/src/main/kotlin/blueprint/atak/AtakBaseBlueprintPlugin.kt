package blueprint.atak

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

public class AtakBaseBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    val properties = AtakProperties(target)
    target.applySdk(properties)
    target.excludeDependencies(properties)
  }

  private fun Project.applySdk(properties: AtakProperties) {
    val sdk = "${properties.sdkCoordinates}:${properties.sdkVersion}"
    dependencies.apply {
      add("compileOnly", sdk)
      add("testImplementation", sdk)
      add("androidTestImplementation", sdk)
    }
  }

  private fun Project.excludeDependencies(properties: AtakProperties) {
    if (!properties.applyExclusions) {
      return
    }

    val file = properties.exclusionsFile?.let { relativePath -> File(rootProject.projectDir, relativePath) }

    val exclusions = file?.readLines()
      ?.filter { line -> line.isNotBlank() }
      ?.map { line -> line.throwIfNotValidCoordinate() }
      ?.map { line -> line.split(":") } // "com.whatever:abc" -> ["com.whatever", "abc"]
      ?.associate { coords -> coords[0] to coords[1] }
      ?: DEFAULT_EXCLUDES

    if (exclusions.isEmpty()) {
      throw GradleException(
        """
          Configured to exclude dependencies, but nothing was found in $file.
          Set property "blueprint.atak.exclusions.file" to a file relative to the root project. Example contents:

             androidx.something:whatever
             com.another.library:something-else
        """.trimIndent()
      )
    }

    configurations.configureEach { config ->
      config.exclude(exclusions)
    }
  }

  private fun String.throwIfNotValidCoordinate(): String {
    val hasOneColon = count { it == ':' } == 1
    val isMavenArtifact = matches(MAVEN_COORDINATE_PATTERN)
    return if (hasOneColon && isMavenArtifact) this else throw GradleException("Invalid maven coordinate $this")
  }

  private companion object {
    val MAVEN_COORDINATE_PATTERN = "^(.+):(.+)$".toRegex()

    @Suppress("SpellCheckingInspection")
    val DEFAULT_EXCLUDES = mapOf(
      "androidx.activity" to "activity",
      "androidx.annotation" to "annotation",
      "androidx.annotation" to "annotation-experimental",
      "androidx.arch.core" to "core-common",
      "androidx.arch.core" to "core-runtime",
      "androidx.collection" to "collection",
      "androidx.concurrent" to "concurrent-futures",
      "androidx.core" to "core",
      "androidx.core" to "core-ktx",
      "androidx.customview" to "customview",
      "androidx.customview" to "customview-poolingcontainer",
      "androidx.exifinterface" to "exifinterface",
      "androidx.fragment" to "fragment",
      "androidx.lifecycle" to "lifecycle-livedata",
      "androidx.lifecycle" to "lifecycle-livedata-core",
      "androidx.lifecycle" to "lifecycle-process",
      "androidx.lifecycle" to "lifecycle-runtime",
      "androidx.lifecycle" to "lifecycle-runtime-ktx",
      "androidx.lifecycle" to "lifecycle-viewmodel",
      "androidx.lifecycle" to "lifecycle-viewmodel-savedstate",
      "androidx.loader" to "loader",
      "androidx.localbroadcastmanager" to "localbroadcastmanager",
      "androidx.profileinstaller" to "profileinstaller",
      "androidx.savedstate" to "savedstate",
      "androidx.startup" to "startup-runtime",
      "androidx.tracing" to "tracingsavedstate",
      "androidx.versionedparcelable" to "versionedparcelable",
      "androidx.viewpager" to "viewpager",
      "org.jetbrains.kotlinx" to "kotlinx-coroutines-core",
      "org.jetbrains.kotlinx" to "kotlinx-coroutines-core-jvm",
      "org.jetbrains.kotlinx" to "kotlinx-coroutines-android",
    )
  }
}
