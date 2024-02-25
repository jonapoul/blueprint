package blueprint.atak

import org.gradle.api.Plugin
import org.gradle.api.Project

public class AtakLibBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("dev.jonpoulton.blueprint.kotlin.android")
    target.pluginManager.apply("dev.jonpoulton.blueprint.android.library")

    if (isTakDevPipeline) {
      target.pluginManager.apply("atak-takdev-plugin")
    }

    target.pluginManager.apply("dev.jonpoulton.blueprint.atak.base")
  }
}
