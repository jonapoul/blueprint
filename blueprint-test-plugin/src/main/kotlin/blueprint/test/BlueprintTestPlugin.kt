package blueprint.test

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.plugin.devel.tasks.PluginUnderTestMetadata

public class BlueprintTestPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    pluginManager.withPlugin("java-gradle-plugin") {
      configure()
    }
  }

  private fun Project.configure() {
    val testPluginClasspath = configurations.register("testPluginClasspath") {
      it.isCanBeResolved = true
    }

    tasks.withType(PluginUnderTestMetadata::class.java).configureEach {
      it.pluginClasspath.from(testPluginClasspath)
    }

    dependencies.add(
      "testImplementation",
      "dev.jonpoulton.blueprint:test-runtime:$BLUEPRINT_VERSION",
    )
  }
}
