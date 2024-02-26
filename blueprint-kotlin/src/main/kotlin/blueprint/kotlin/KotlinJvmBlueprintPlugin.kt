package blueprint.kotlin

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension

public class KotlinJvmBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("org.jetbrains.kotlin.jvm")
    target.pluginManager.apply("dev.jonpoulton.blueprint.kotlin.base")

    val properties = KotlinProperties(target)
    val javaVersion = properties.javaVersion
    target.extensions.configure(JavaPluginExtension::class.java) { java ->
      java.sourceCompatibility = JavaVersion.toVersion(javaVersion)
      java.targetCompatibility = JavaVersion.toVersion(javaVersion)
    }
  }
}
