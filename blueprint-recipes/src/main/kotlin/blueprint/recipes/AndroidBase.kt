@file:Suppress("UnstableApiUsage")

package blueprint.recipes

import blueprint.core.getOrCreate
import blueprint.core.intProperty
import blueprint.core.javaVersion
import blueprint.core.jvmTarget
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions

public fun Project.androidBaseBlueprint(
  enableTestInDebug: Boolean = true,
  enableTestInRelease: Boolean = false,
) {
  with(plugins) {
    apply("org.gradle.android.cache-fix")
  }

  extensions.getByType(CommonExtension::class).apply {
    compileSdk = intProperty(key = "blueprint.android.compileSdk")

    defaultConfig {
      minSdk = intProperty(key = "blueprint.android.minSdk")
      testInstrumentationRunnerArguments["disableAnalytics"] = "true"
    }

    extensions.findByType(KotlinJvmCompilerOptions::class)?.apply {
      jvmTarget.set(project.jvmTarget())
    }

    val version = javaVersion()
    compileOptions {
      sourceCompatibility = version
      targetCompatibility = version
    }

    configureBuildFeatures()
    configureLint(project)
    configureTestOptions(version)

    buildTypes {
      getOrCreate("debug") {
        enableUnitTestCoverage = enableTestInDebug
        enableAndroidTestCoverage = enableTestInDebug
      }

      getOrCreate("release") {
        enableUnitTestCoverage = enableTestInRelease
        enableAndroidTestCoverage = enableTestInRelease
      }
    }

    extensions.configure<LibraryAndroidComponentsExtension> {
      // disable instrumented tests if androidTest folder doesn't exist
      beforeVariants {
        it.enableAndroidTest = it.enableAndroidTest && projectDir.resolve("src/androidTest").exists()
      }
    }
  }
}

private fun CommonExtension<*, *, *, *, *, *>.configureBuildFeatures() = buildFeatures {
  // Enabled in modules that need them
  resValues = false
  viewBinding = false

  // Disable useless build steps
  aidl = false
  buildConfig = false
  compose = false
  prefab = false
  renderScript = false
  shaders = false
}

private fun CommonExtension<*, *, *, *, *, *>.configureLint(project: Project) = lint {
  abortOnError = false
  checkGeneratedSources = false
  checkReleaseBuilds = false
  checkReleaseBuilds = false
  checkTestSources = true
  explainIssues = true
  htmlReport = true
  xmlReport = true
  lintConfig = project.rootProject.file("lint.xml")
}

private fun CommonExtension<*, *, *, *, *, *>.configureTestOptions(javaVersion: JavaVersion) = testOptions {
  unitTests {
    isIncludeAndroidResources = true
    isReturnDefaultValues = true

    if (javaVersion < JavaVersion.VERSION_13) {
      // deprecated in java 13
      all { it.jvmArgs("-noverify") }
    }
  }
}
