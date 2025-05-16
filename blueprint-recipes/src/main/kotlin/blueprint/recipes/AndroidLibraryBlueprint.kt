package blueprint.recipes

import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.AndroidBasePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

public class AndroidLibraryBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(plugins) {
      apply(LibraryPlugin::class)
      apply(AndroidBasePlugin::class)
    }

    extensions.getByType(LibraryExtension::class).apply {
      buildFeatures {
        // Enabled in modules that need them
        androidResources = false
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
}
