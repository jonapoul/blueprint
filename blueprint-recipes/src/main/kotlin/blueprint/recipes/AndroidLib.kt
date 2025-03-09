@file:Suppress("UnstableApiUsage")

package blueprint.recipes

import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

public fun Project.androidLibBlueprint() {
  with(plugins) {
    apply(LibraryPlugin::class)
  }

  androidBaseBlueprint()

  extensions.getByType(LibraryExtension::class).apply {
    buildFeatures {
      // Enabled in modules that need them
      dataBinding = false
      mlModelBinding = false
      prefabPublishing = false
    }

    packaging {
      resources {
        excludes.addAll(
          listOf(
            "META-INF/DEPENDENCIES",
            "META-INF/LICENSE*",
            "META-INF/NOTICE*",
            "META-INF/ASL2.0",
          )
        )
      }

      jniLibs {
        useLegacyPackaging = true
      }
    }
  }
}
