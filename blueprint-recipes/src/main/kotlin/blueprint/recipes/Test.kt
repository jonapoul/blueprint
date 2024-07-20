package blueprint.recipes

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.withType

public fun Project.testBlueprint(
  versions: TestVersions,
  otherKotlinTestLibs: List<String> = emptyList(),
  otherAndroidTestLibs: List<String> = emptyList(),
) {
  val isAndroid = project.extensions.findByType(LibraryExtension::class) != null ||
    project.extensions.findByType(AppExtension::class) != null

  val testImplementation = configurations.findByName("testImplementation")
  dependencies {
    testImplementation?.let { testImplementation ->
      add(testImplementation, "dev.jonpoulton.alakazam:testing-core", versions.alakazam)
      add(testImplementation, "junit:junit", versions.junit)
      add(testImplementation, "org.jetbrains.kotlin:kotlin-test-common", versions.kotlin)
      add(testImplementation, "org.jetbrains.kotlin:kotlin-test-junit", versions.kotlin)
      add(testImplementation, "org.jetbrains.kotlinx:kotlinx-coroutines-test", versions.coroutines)
      add(testImplementation, "io.mockk:mockk", versions.mockk)
      add(testImplementation, "app.cash.turbine:turbine", versions.turbine)
      otherKotlinTestLibs.forEach { testImplementation(it) }

      if (isAndroid) {
        add(testImplementation, "androidx.arch.core:core-testing", versions.androidxArch)
        add(testImplementation, "androidx.test:core-ktx", versions.androidxCoreKtx)
        add(testImplementation, "androidx.test.ext:junit", versions.androidxJunit)
        add(testImplementation, "androidx.test:rules", versions.androidxRules)
        add(testImplementation, "androidx.test:runner", versions.androidxRunner)
        add(testImplementation, "org.robolectric:robolectric", versions.robolectric)
        otherAndroidTestLibs.forEach { testImplementation(it) }
      }
    }
  }

  tasks.withType<Test> {
    testLogging {
      it.exceptionFormat = TestExceptionFormat.FULL
      it.showCauses = true
      it.showExceptions = true
      it.showStackTraces = true
      it.showStandardStreams = true
    }
  }
}

public data class TestVersions(
  val alakazam: Provider<String>?,
  val junit: Provider<String>?,
  val kotlin: Provider<String>?,
  val coroutines: Provider<String>?,
  val mockk: Provider<String>?,
  val turbine: Provider<String>?,
  val androidxArch: Provider<String>?,
  val androidxCoreKtx: Provider<String>?,
  val androidxJunit: Provider<String>?,
  val androidxRules: Provider<String>?,
  val androidxRunner: Provider<String>?,
  val robolectric: Provider<String>?,
)

private fun DependencyHandlerScope.add(config: Configuration, lib: String, version: Provider<String>?) {
  version ?: return
  config("$lib:${version.get()}")
}
