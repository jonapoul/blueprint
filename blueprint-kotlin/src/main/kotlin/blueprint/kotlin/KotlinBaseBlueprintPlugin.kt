package blueprint.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public class KotlinBaseBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    val properties = KotlinProperties(target)
    target.configureExtension(properties)
    target.configureTask(properties)
  }

  private fun Project.configureExtension(properties: KotlinProperties) {
    extensions.configure(KotlinTopLevelExtension::class.java) { ext ->
      if (properties.explicitApi) {
        ext.explicitApi()
      }
    }
  }

  private fun Project.configureTask(properties: KotlinProperties) {
    tasks.withType(KotlinCompile::class.java) { task ->
      task.kotlinOptions { configureOptions(properties) }
    }
  }

  private fun KotlinJvmOptions.configureOptions(properties: KotlinProperties) {
    jvmTarget = properties.javaVersion.toString()
    freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"

    if (properties.allCompatibility) {
      freeCompilerArgs += "-Xjvm-default=all-compatibility"
    }

    if (properties.explicitApi) {
      freeCompilerArgs += "-Xexplicit-api=strict"
    }

    if (properties.experimentalCoroutines) {
      freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    }
  }
}
