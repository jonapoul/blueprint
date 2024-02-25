@file:Suppress("UnstableApiUsage")

package blueprint.android

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class AndroidDesugaringBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("dev.jonpoulton.blueprint.android.base")

    val properties = AndroidProperties(target)

    target.extensions.configure(CommonExtension::class.java) { ext ->
      ext.compileOptions {
        isCoreLibraryDesugaringEnabled = true
      }
    }

    target.dependencies.add(
      "coreLibraryDesugaring",
      "com.android.tools:desugar_jdk_libs:${properties.desugaringVersion}"
    )
  }
}
