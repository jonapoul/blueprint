package blueprint.recipes

import app.cash.licensee.LicenseeExtension
import app.cash.licensee.LicenseePlugin
import app.cash.licensee.UnusedAction
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

public fun Project.licenseeBlueprint(
  allowedSpdxIds: List<String> = DEFAULT_LICENSE_SPDX_IDS,
  allowedUrls: List<String> = emptyList(),
  unusedAction: UnusedAction = UnusedAction.IGNORE,
) {
  with(pluginManager) {
    apply(LicenseePlugin::class.java)
  }

  extensions.configure<LicenseeExtension> {
    allowedSpdxIds.forEach(::allow)
    allowedUrls.forEach(::allowUrl)
    unusedAction(unusedAction)
  }
}
