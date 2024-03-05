package blueprint.guard

import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized

public class DependencyGuardBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("com.dropbox.dependency-guard")
    val properties = DependencyGuardProperties(target)
    target.afterEvaluate {
      target.configureExtension(properties)
    }
  }

  private fun Project.configureExtension(properties: DependencyGuardProperties) {
    extensions.configure(DependencyGuardPluginExtension::class.java) { ext ->
      if (properties.customConfigurations != null) {
        properties.customConfigurations.forEach { ext.configuration(it) }
        return@configure
      }

      val isRoot = project == rootProject
      val isAndroid = plugins.hasPlugin("com.android.base")
      val isAndroidApp = plugins.hasPlugin("com.android.application")
      val isAtak = isAtak()
      val isKotlin = isKotlin()

      val configs = when {
        isRoot -> listOf("classpath")
        isAtak && isAndroidApp -> DEFAULT_ANDROID_CONFIGS.map { "civ${it.capitalized()}" }
        isAndroid -> DEFAULT_ANDROID_CONFIGS
        isKotlin -> DEFAULT_KOTLIN_CONFIGS
        else -> error("Not sure how to handle ${project.path}. Plugins = $plugins")
      }

      configs.forEach {
        ext.configuration(it)
      }
    }
  }

  private fun Project.isAtak(): Boolean {
    if (plugins.hasPlugin("atak-takdev-plugin")) {
      return true
    }

    configurations.forEach { config ->
      config.allDependencies.forEach { dep ->
        if (dep.group == "com.atakmap.civ.common" && dep.name == "api") {
          return true
        }
      }
    }
    return false
  }

  private fun Project.isKotlin(): Boolean {
    return listOf(
      "org.jetbrains.kotlin.jvm",
      "org.jetbrains.kotlin.android",
    ).any {
      plugins.hasPlugin(it)
    }
  }

  private companion object {
    val DEFAULT_KOTLIN_CONFIGS = listOf(
      "compileClasspath",
      "runtimeClasspath",
    )

    val DEFAULT_ANDROID_CONFIGS = listOf(
      "debugCompileClasspath",
      "releaseCompileClasspath",
      "debugRuntimeClasspath",
      "releaseRuntimeClasspath",
    )
  }
}
