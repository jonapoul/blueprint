package blueprint.android

import blueprint.core.BlueprintProperties
import org.gradle.api.Project

public class AndroidProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix: String = "blueprint.android"

  public val applyCacheFix: Boolean = boolProperty(key = "applyCacheFix", default = true)
  public val minSdk: Int = intProperty(key = "minSdk", default = 21)
  public val compileSdk: Int = intProperty(key = "compileSdk", default = 33)
  public val targetSdk: Int = intProperty(key = "targetSdk", default = 33)

  public val instrumentationRunner: String =
    stringProperty("instrumentationRunner", "androidx.test.runner.AndroidJUnitRunner")
  public val instrumentationAnalytics: Boolean = boolProperty(key = "instrumentationAnalytics", default = false)
  public val multidex: Boolean = boolProperty(key = "multidex", default = true)

  public val lintCheckReleaseBuilds: Boolean = boolProperty(key = "lint.checkReleaseBuilds", default = false)
  public val abortOnLintError: Boolean = boolProperty(key = "lint.checkReleaseBuilds", default = false)
  public val quietLint: Boolean = boolProperty(key = "lint.quiet", default = true)

  public val testIncludeResources: Boolean = boolProperty(key = "test.includeResources", default = true)
  public val testReturnDefaultValues: Boolean = boolProperty(key = "test.returnDefaultValues", default = true)
  public val noVerify: Boolean = boolProperty(key = "test.noVerify", default = true)

  public val enableAidl: Boolean = boolProperty(key = "build.aidl", default = false)
  public val enableBuildConfig: Boolean = boolProperty(key = "build.buildConfig", default = false)
  public val enablePrefab: Boolean = boolProperty(key = "build.prefab", default = false)
  public val enableRenderScript: Boolean = boolProperty(key = "build.renderScript", default = false)
  public val enableResValues: Boolean = boolProperty(key = "build.resValues", default = false)
  public val enableShaders: Boolean = boolProperty(key = "build.shaders", default = false)
  public val enableViewBinding: Boolean = boolProperty(key = "build.viewBinding", default = false)

  public val enableAndroidResources: Boolean = boolProperty(key = "build.androidResources", default = false)
  public val enableDataBinding: Boolean = boolProperty(key = "build.dataBinding", default = false)
  public val enableMlModelBinding: Boolean = boolProperty(key = "build.mlModelBinding", default = false)
  public val enablePrefabPublishing: Boolean = boolProperty(key = "build.prefabPublishing", default = false)

  public val packagingExcludes: List<String> = stringListProperty("packaging.excludes", listOf("META-INF/*"))
  public val packagingPickFirsts: List<String> = stringListProperty("packaging.pickFirsts", listOf("MANIFEST.MF"))
  public val useLegacyPackaging: Boolean = boolProperty(key = "packaging.useLegacyPackaging", default = true)

  public val desugaringVersion: String = stringProperty("desugaringVersion", BuildConfig.DESUGARING_VERSION)

  public val applyComposeBom: Boolean = boolProperty(key = "compose.applyBom", default = true)
  public val composeBom: String = stringProperty(key = "compose.bom", default = BuildConfig.COMPOSE_BOM_VERSION)
  public val composeCompiler: String = stringProperty("compose.compiler", BuildConfig.COMPOSE_COMPILER_VERSION)
  public val composeExperimentalFoundation: Boolean = boolProperty(key = "compose.exp.foundation", default = true)
  public val composeExperimentalMaterial: Boolean = boolProperty(key = "compose.exp.material", default = true)
  public val composeExperimentalUi: Boolean = boolProperty(key = "compose.exp.ui", default = true)

  public val includeTimestamp: Boolean = boolProperty(key = "app.includeTimestamp", default = true)
}
