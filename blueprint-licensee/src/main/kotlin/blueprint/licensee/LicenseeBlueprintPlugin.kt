package blueprint.licensee

import app.cash.licensee.LicenseeExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class LicenseeBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("app.cash.licensee")
    val properties = LicenseeProperties(target)
    target.configureExtension(properties)
  }

  private fun Project.configureExtension(properties: LicenseeProperties) {
    extensions.configure(LicenseeExtension::class.java) { ext ->
      if (properties.useDefaultSpdxIds) {
        ext.allow(spdxId = "Apache-2.0") // Most libraries
        ext.allow(spdxId = "MIT") // Some other libraries
        ext.allow(spdxId = "BSD-3-Clause") // Hamcrest, Protobuf
        ext.allow(spdxId = "BSD-2-Clause") // Hamcrest
        ext.allow(spdxId = "EPL-1.0") // JUnit
      }

      properties.extraSpdxIds?.forEach { id ->
        ext.allow(spdxId = id)
      }

      if (properties.useDefaultUrls) {
        ext.allowUrl(url = "http://www.opensource.org/licenses/bsd-license.php") // Hamcrest, BSD-2-Clause
        ext.allowUrl(url = "https://github.com/mockito/mockito/blob/master/LICENSE") // MIT
      }

      properties.extraUrls?.forEach { url ->
        ext.allowUrl(url = url)
      }
    }
  }
}
