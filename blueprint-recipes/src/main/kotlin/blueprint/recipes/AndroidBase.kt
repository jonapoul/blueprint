@file:Suppress("UnstableApiUsage")

package blueprint.recipes

import blueprint.core.intProperty
import blueprint.core.javaVersion
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

public fun Project.androidBaseBlueprint() {
  with(plugins) {
    apply("org.gradle.android.cache-fix")
  }

  extensions.getByType(CommonExtension::class).apply {
    compileSdk = intProperty(key = "android.compileSdk")

    defaultConfig {
      minSdk = intProperty(key = "android.minSdk")
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
      checkReleaseBuilds = false
      abortOnError = false
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
