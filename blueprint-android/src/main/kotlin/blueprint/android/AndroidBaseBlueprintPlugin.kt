@file:Suppress("UnstableApiUsage")

package blueprint.android

import blueprint.kotlin.KotlinProperties
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

public class AndroidBaseBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    val androidProps = AndroidProperties(target)
    val kotlinProps = KotlinProperties(target)

    if (androidProps.applyCacheFix) {
      target.plugins.apply("org.gradle.android.cache-fix")
    }

    target.extensions.configure(CommonExtension::class.java) { ext ->
      ext.configureExtension(androidProps, kotlinProps)
    }
  }

  private fun CommonExtension<*, *, *, *>.configureExtension(
    androidProps: AndroidProperties,
    kotlinProps: KotlinProperties,
  ) {
    compileSdk = androidProps.compileSdk

    defaultConfig {
      minSdk = androidProps.minSdk
      testInstrumentationRunner = androidProps.instrumentationRunner
      testInstrumentationRunnerArguments["disableAnalytics"] = androidProps.instrumentationAnalytics.toString()
    }

    compileOptions {
      sourceCompatibility = JavaVersion.toVersion(kotlinProps.javaVersion)
      targetCompatibility = JavaVersion.toVersion(kotlinProps.javaVersion)
    }

    buildFeatures {
      aidl = androidProps.enableAidl
      buildConfig = androidProps.enableBuildConfig
      compose = false // enabled by the compose plugin
      prefab = androidProps.enablePrefab
      renderScript = androidProps.enableRenderScript
      resValues = androidProps.enableResValues
      shaders = androidProps.enableShaders
      viewBinding = androidProps.enableViewBinding
    }

    lint {
      checkReleaseBuilds = androidProps.lintCheckReleaseBuilds
      abortOnError = androidProps.abortOnLintError
      quiet = androidProps.quietLint
    }

    testOptions {
      unitTests {
        isIncludeAndroidResources = androidProps.testIncludeResources
        isReturnDefaultValues = androidProps.testReturnDefaultValues

        if (androidProps.noVerify) {
          all { test -> test.jvmArgs("-noverify") }
        }
      }
    }
  }
}
