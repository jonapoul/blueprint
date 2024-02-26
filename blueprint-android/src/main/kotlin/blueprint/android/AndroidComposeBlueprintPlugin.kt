@file:Suppress("UnstableApiUsage")

package blueprint.android

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public class AndroidComposeBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("dev.jonpoulton.blueprint.kotlin.android")
    target.pluginManager.apply("dev.jonpoulton.blueprint.android.base")

    val properties = AndroidProperties(target)

    target.extensions.configure(CommonExtension::class.java) { ext ->
      ext.configureExtension(properties)
    }

    if (properties.applyComposeBom) {
      val bom = target.dependencies.platform("androidx.compose:compose-bom:${properties.composeBom}")
      target.dependencies.add("implementation", bom)

      val preview = "androidx.compose.ui:ui-tooling-preview"
      target.dependencies.add("compileOnly", preview)
    }

    target.configureTask(properties)
  }

  private fun CommonExtension<*, *, *, *>.configureExtension(properties: AndroidProperties) {
    buildFeatures {
      compose = true
    }

    composeOptions {
      kotlinCompilerExtensionVersion = properties.composeCompiler
    }
  }

  private fun Project.configureTask(properties: AndroidProperties) {
    tasks.withType(KotlinCompile::class.java) { task ->
      task.kotlinOptions {
        if (properties.composeExperimentalFoundation) {
          freeCompilerArgs += "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
        }
        if (properties.composeExperimentalUi) {
          freeCompilerArgs += "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi"
        }
      }
    }
  }
}
