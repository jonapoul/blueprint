@file:Suppress("LongParameterList")

package blueprint.recipes

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType

public fun Project.testBlueprint(
  versions: TestVersions,
  otherKotlinTestLibs: List<String> = emptyList(),
  otherAndroidTestLibs: List<String> = emptyList(),
) {
  testBaseBlueprint()

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
        add(testImplementation, "androidx.test.ext:junit-ktx", versions.androidxJunit)
        add(testImplementation, "androidx.test:rules", versions.androidxRules)
        add(testImplementation, "androidx.test:runner", versions.androidxRunner)
        add(testImplementation, "org.robolectric:robolectric", versions.robolectric)
        otherAndroidTestLibs.forEach { testImplementation(it) }
      }
    }
  }
}

public data class TestVersions(
  val alakazam: String? = null,
  val androidxArch: String? = null,
  val androidxCoreKtx: String? = null,
  val androidxJunit: String? = null,
  val androidxRules: String? = null,
  val androidxRunner: String? = null,
  val coroutines: String? = null,
  val junit: String? = null,
  val kotlin: String? = null,
  val mockk: String? = null,
  val robolectric: String? = null,
  val turbine: String? = null,
)

public fun TestVersions(
  alakazam: Provider<String>? = null,
  androidxArch: Provider<String>? = null,
  androidxCoreKtx: Provider<String>? = null,
  androidxJunit: Provider<String>? = null,
  androidxRules: Provider<String>? = null,
  androidxRunner: Provider<String>? = null,
  coroutines: Provider<String>? = null,
  junit: Provider<String>? = null,
  kotlin: Provider<String>? = null,
  mockk: Provider<String>? = null,
  robolectric: Provider<String>? = null,
  turbine: Provider<String>? = null,
): TestVersions = TestVersions(
  alakazam = alakazam?.get(),
  androidxArch = androidxArch?.get(),
  androidxCoreKtx = androidxCoreKtx?.get(),
  androidxJunit = androidxJunit?.get(),
  androidxRules = androidxRules?.get(),
  androidxRunner = androidxRunner?.get(),
  coroutines = coroutines?.get(),
  junit = junit?.get(),
  kotlin = kotlin?.get(),
  mockk = mockk?.get(),
  robolectric = robolectric?.get(),
  turbine = turbine?.get(),
)

public fun TestVersions(
  alakazam: VersionConstraint? = null,
  androidxArch: VersionConstraint? = null,
  androidxCoreKtx: VersionConstraint? = null,
  androidxJunit: VersionConstraint? = null,
  androidxRules: VersionConstraint? = null,
  androidxRunner: VersionConstraint? = null,
  coroutines: VersionConstraint? = null,
  junit: VersionConstraint? = null,
  kotlin: VersionConstraint? = null,
  mockk: VersionConstraint? = null,
  robolectric: VersionConstraint? = null,
  turbine: VersionConstraint? = null,
): TestVersions = TestVersions(
  alakazam = alakazam?.toString(),
  androidxArch = androidxArch?.toString(),
  androidxCoreKtx = androidxCoreKtx?.toString(),
  androidxJunit = androidxJunit?.toString(),
  androidxRules = androidxRules?.toString(),
  androidxRunner = androidxRunner?.toString(),
  coroutines = coroutines?.toString(),
  junit = junit?.toString(),
  kotlin = kotlin?.toString(),
  mockk = mockk?.toString(),
  robolectric = robolectric?.toString(),
  turbine = turbine?.toString(),
)

private fun DependencyHandlerScope.add(config: Configuration, lib: String, version: String?) {
  version ?: return
  config("$lib:$version")
}
