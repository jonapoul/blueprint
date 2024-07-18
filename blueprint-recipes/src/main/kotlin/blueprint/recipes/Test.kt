package blueprint.recipes

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
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
      testImplementation("dev.jonpoulton.alakazam:testing-core:${versions.alakazam.get()}")
      testImplementation("junit:junit:${versions.junit.get()}")
      testImplementation("org.jetbrains.kotlin:kotlin-test-common:${versions.kotlin.get()}")
      testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${versions.kotlin.get()}")
      testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${versions.coroutines.get()}")
      testImplementation("io.mockk:mockk:${versions.mockk.get()}")
      testImplementation("app.cash.turbine:turbine:${versions.turbine.get()}")
      otherKotlinTestLibs.forEach { testImplementation(it) }

      if (isAndroid) {
        testImplementation("androidx.arch.core:core-testing:${versions.androidxArch.get()}")
        testImplementation("androidx.test:core-ktx:${versions.androidxCoreKtx.get()}")
        testImplementation("androidx.test.ext:junit:${versions.androidxJunit.get()}")
        testImplementation("androidx.test:rules:${versions.androidxRules.get()}")
        testImplementation("androidx.test:runner:${versions.androidxRunner.get()}")
        testImplementation("org.robolectric:robolectric:${versions.robolectric.get()}")
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
  val alakazam: Provider<String>,
  val junit: Provider<String>,
  val kotlin: Provider<String>,
  val coroutines: Provider<String>,
  val mockk: Provider<String>,
  val turbine: Provider<String>,
  val androidxArch: Provider<String>,
  val androidxCoreKtx: Provider<String>,
  val androidxJunit: Provider<String>,
  val androidxRules: Provider<String>,
  val androidxRunner: Provider<String>,
  val robolectric: Provider<String>,
)
