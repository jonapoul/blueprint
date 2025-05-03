package blueprint.recipes

import app.cash.licensee.LicenseeExtension
import app.cash.licensee.LicenseePlugin
import app.cash.licensee.UnusedAction
import blueprint.core.stringListPropertyOrElse
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

public class LicenseeBlueprint : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(LicenseePlugin::class)
    }

    val spdxIds = stringListPropertyOrElse(key = "blueprint.licensee.spdxIds", default = DEFAULT_LICENSE_SPDX_IDS)
    val urls = stringListPropertyOrElse(key = "blueprint.licensee.urls", default = emptyList())

    extensions.configure<LicenseeExtension> {
      spdxIds.forEach(::allow)
      urls.forEach(::allowUrl)
      unusedAction(UnusedAction.IGNORE)
    }
  }
}
