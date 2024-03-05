package blueprint.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project

public class KotlinJvmBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("org.jetbrains.kotlin.jvm")
    target.pluginManager.apply("dev.jonpoulton.blueprint.kotlin.base")

    val properties = KotlinProperties(target)
    target.setJavaVersion(properties.javaVersion)
  }
}
