package blueprint.recipes

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradlePlugin

@ExperimentalKotlinGradlePluginApi
public class TestPowerAssertBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(PowerAssertGradlePlugin::class)
    }

    extensions.configure<PowerAssertGradleExtension> {
      functions.addAll(DEFAULT_POWER_ASSERT_FUNCTIONS)
    }
  }
}
