package blueprint.guard

import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.plugin.DefaultKotlinBasePlugin

public class DependencyGuardBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("com.dropbox.dependency-guard")
    val properties = DependencyGuardProperties(target)
    target.configureExtension(properties)
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
      val isAtak = plugins.hasPlugin("atak-gradle-plugin")
      val isKotlin = plugins.hasPlugin(DefaultKotlinBasePlugin::class.java)

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
