package blueprint.doctor

import blueprint.core.BlueprintProperties
import org.gradle.api.Project

internal class DoctorProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix = "blueprint.doctor"

  val allowBuildingAllAndroidAppsSimultaneously =
    boolProperty(key = "allowBuildingAllAndroidAppsSimultaneously", default = false)

  val disallowMultipleDaemons = boolProperty(key = "disallowMultipleDaemons", default = false)
  val downloadSpeedWarningThreshold = floatProperty(key = "downloadSpeedWarningThreshold", default = 0.5f)
  val ensureJavaHomeMatches = boolProperty(key = "ensureJavaHomeMatches", default = true)
  val ensureJavaHomeIsSet = boolProperty(key = "ensureJavaHomeIsSet", default = true)
  val failOnEmptyDirectories = boolProperty(key = "failOnEmptyDirectories", default = true)
  val failOnError = boolProperty(key = "failOnError", default = true)
}
