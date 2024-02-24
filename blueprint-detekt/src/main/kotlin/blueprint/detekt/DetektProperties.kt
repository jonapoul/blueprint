package blueprint.detekt

import blueprint.core.BlueprintProperties
import org.gradle.api.Project

internal class DetektProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix = "blueprint.detekt"

  val buildUponDefaultConfig = boolProperty(key = "buildUponDefaultConfig", default = true)
  val configFilePath = stringProperty(key = "configFilePath", default = "detekt.yml")
  val ignoreFailures = boolProperty(key = "ignoreFailures", default = false)

  val htmlReports = boolProperty(key = "htmlReports", default = true)
  val xmlReports = boolProperty(key = "xmlReports", default = false)
  val txtReports = boolProperty(key = "txtReports", default = false)
  val sarifReports = boolProperty(key = "sarifReports", default = false)
  val mdReports = boolProperty(key = "mdReports", default = false)

  val autoDetektMain = boolProperty(key = "autoDetektMain", default = true)
}
