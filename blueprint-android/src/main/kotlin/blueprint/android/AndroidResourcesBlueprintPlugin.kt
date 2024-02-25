@file:Suppress("UnstableApiUsage")

package blueprint.android

import com.android.build.gradle.LibraryExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

public class AndroidResourcesBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("com.android.library")
    target.pluginManager.apply("dev.jonpoulton.blueprint.android.base")

    val hasOtherPluginsApplied = target.plugins.any { plugin ->
      plugin is AndroidLibraryBlueprintPlugin
    }
    if (hasOtherPluginsApplied) {
      val allPlugins = target.plugins.joinToString()
      throw GradleException("Can't use resources blueprint alongside lib or app blueprints. Plugins: $allPlugins")
    }

    target.extensions.configure(LibraryExtension::class.java) { ext ->
      ext.configureExtension()
    }
  }

  private fun LibraryExtension.configureExtension() {
    buildFeatures {
      androidResources = true
      resValues = true
    }
  }
}
