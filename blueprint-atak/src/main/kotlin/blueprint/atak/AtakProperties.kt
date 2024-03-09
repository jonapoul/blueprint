package blueprint.atak

import blueprint.core.BlueprintProperties
import org.gradle.api.Project

internal class AtakProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix = "blueprint.atak"

  val pluginVersion by lazy { stringPropertyOrThrow(key = "version.plugin") }
  val atakVersion = stringProperty(key = "version.atak", default = BuildConfig.ATAK_BASE_VERSION)
  val sdkVersion = stringProperty(key = "version.sdk", default = BuildConfig.ATAK_SDK_VERSION)
  val sdkCoordinates = stringProperty(key = "sdkCoordinates", default = "com.atakmap.civ.common:api")

  val applyExclusions = boolProperty(key = "exclusions.apply", default = true)
  val exclusionsFile = stringPropertyOrNull(key = "exclusions.file")

  val proguardBase = stringPropertyOrNull(key = "proguard.base")
  val proguardRules = stringProperty(key = "proguard.rules", default = "proguard-rules.pro")
  val proguardMapping = stringProperty(key = "proguard.mapping", default = "proguard-mapping.pro")

  val apkName by lazy { stringPropertyOrThrow(key = "apkName") }
}
