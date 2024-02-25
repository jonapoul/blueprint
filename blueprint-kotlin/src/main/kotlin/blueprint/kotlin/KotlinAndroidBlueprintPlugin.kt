package blueprint.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project

public class KotlinAndroidBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("org.jetbrains.kotlin.android")
    target.pluginManager.apply("dev.jonpoulton.blueprint.kotlin.base")
  }
}
