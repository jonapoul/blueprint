@file:Suppress("UnstableApiUsage")

package blueprint.recipes

import blueprint.core.intProperty
import blueprint.core.javaVersion
import blueprint.core.javaVersionString
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

public fun Project.androidBaseBlueprint() {
  with(plugins) {
    apply("org.gradle.android.cache-fix")
  }

  extensions.getByType(CommonExtension::class).apply {
    compileSdk = intProperty(key = "blueprint.android.compileSdk")

    defaultConfig {
      minSdk = intProperty(key = "blueprint.android.minSdk")
      testInstrumentationRunnerArguments["disableAnalytics"] = "true"
    }

    extensions.findByType(KotlinJvmOptions::class)?.apply {
      jvmTarget = javaVersionString()
    }

    compileOptions {
      val version = javaVersion()
      sourceCompatibility = version
      targetCompatibility = version
    }

    buildFeatures {
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

    lint {
      abortOnError = false
      checkGeneratedSources = false
      checkReleaseBuilds = false
      checkReleaseBuilds = false
      checkTestSources = true
      explainIssues = true
      htmlReport = true
      xmlReport = true
      lintConfig = rootProject.file("lint.xml")
    }

    testOptions {
      unitTests {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
        all { it.jvmArgs("-noverify") }
      }
    }
  }
}
