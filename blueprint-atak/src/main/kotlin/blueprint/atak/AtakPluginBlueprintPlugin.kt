@file:Suppress("UnstableApiUsage")

package blueprint.atak

import blueprint.android.AndroidProperties
import blueprint.core.getStringOrThrow
import blueprint.core.localProperties
import blueprint.git.gitVersionCode
import blueprint.git.gitVersionName
import com.android.build.api.dsl.AndroidSourceSet
import com.android.build.api.dsl.ApkSigningConfig
import com.android.build.api.dsl.ApplicationBuildType
import com.android.build.api.dsl.PackagingOptions
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.build.gradle.internal.dsl.SigningConfig
import org.gradle.api.GradleException
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

public class AtakPluginBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("dev.jonpoulton.blueprint.kotlin.android")
    target.pluginManager.apply("dev.jonpoulton.blueprint.android.base")
    target.pluginManager.apply("atak-takdev-plugin")
    target.pluginManager.apply("dev.jonpoulton.blueprint.atak.base")

    val androidProperties = AndroidProperties(target)
    val atakProperties = AtakProperties(target)

    val gitVersionName = target.gitVersionName()
    val apkName = atakProperties.apkName
    val atakVersion = atakProperties.atakVersion
    val pluginVersion = atakProperties.pluginVersion
    target.setProperty("archivesBaseName", "ATAK-Plugin-${apkName}-${pluginVersion}-${gitVersionName}-${atakVersion}")

    target.extensions.configure(BaseAppModuleExtension::class.java) { ext ->
      ext.configureAndroid(target, androidProperties, atakProperties, gitVersionName)
    }
  }

  private fun BaseAppModuleExtension.configureAndroid(
    target: Project,
    androidProperties: AndroidProperties,
    atakProperties: AtakProperties,
    gitVersionName: String,
  ) {
    defaultConfig {
      buildConfigField("String", "GIT_HASH", "\"${gitVersionName}\"")

      if (androidProperties.includeTimestamp) {
        buildConfigField(
          "kotlinx.datetime.Instant",
          "BUILD_TIME",
          "kotlinx.datetime.Instant.Companion.fromEpochMilliseconds(${System.currentTimeMillis()}L)",
        )
      }

      versionCode = target.gitVersionCode()
      versionName = "${atakProperties.pluginVersion} (${gitVersionName}) - [${atakProperties.atakVersion}]"
    }

    bundle {
      storeArchive {
        enable = false
      }
    }

    signingConfigs { configureSigning(target) }
    buildTypes { configureBuildTypes(atakProperties, signingConfigs) }
    packagingOptions { configurePackaging(androidProperties) }
    buildFeatures { buildConfig = true }
    sourceSets { configureSourceSets() }
    configureFlavours(atakProperties)
  }

  private fun NamedDomainObjectContainer<out ApkSigningConfig>.configureSigning(target: Project) {
    val localProperties = target.localProperties()

    getByName("debug") { config ->
      config.storePassword = localProperties.getStringOrThrow(key = "takDebugKeyFilePassword")
      config.keyAlias = localProperties.getStringOrThrow(key = "takDebugKeyAlias")
      config.keyPassword = localProperties.getStringOrThrow(key = "takDebugKeyPassword")

      val kf = localProperties.getStringOrThrow(key = "takDebugKeyFile")
      val file = File(target.rootProject.projectDir, kf)
      if (!file.exists()) throw GradleException("takDebugKeyFile $file doesn't exist")
      config.storeFile = file
    }
    create("release") { config ->
      config.storePassword = localProperties.getStringOrThrow(key = "takReleaseKeyFilePassword")
      config.keyAlias = localProperties.getStringOrThrow(key = "takReleaseKeyAlias")
      config.keyPassword = localProperties.getStringOrThrow(key = "takReleaseKeyPassword")

      val kf = localProperties.getStringOrThrow(key = "takReleaseKeyFile")
      val file = File(target.rootProject.projectDir, kf)
      if (!file.exists()) throw GradleException("takReleaseKeyFile $file doesn't exist")
      config.storeFile = file
    }
  }

  private fun NamedDomainObjectContainer<ApplicationBuildType>.configureBuildTypes(
    atakProperties: AtakProperties,
    signingConfigs: NamedDomainObjectContainer<SigningConfig>,
  ) {
    getByName("debug").apply {
      isDebuggable = true
      matchingFallbacks.add("sdk")
      signingConfig = signingConfigs.getByName("debug")
    }

    getByName("release").apply {
      matchingFallbacks.add("odk")
      isMinifyEnabled = true
      signingConfig = signingConfigs.getByName("release")
      if (isTakDevPipeline) {
        proguardFiles(atakProperties.proguardRules, atakProperties.proguardMapping)
      } else {
        proguardFile(atakProperties.proguardRules)
      }
    }
  }

  private fun PackagingOptions.configurePackaging(properties: AndroidProperties) {
    jniLibs {
      useLegacyPackaging = properties.useLegacyPackaging
    }

    resources {
      pickFirsts.addAll(properties.packagingPickFirsts)
      excludes.addAll(properties.packagingExcludes)
    }
  }

  private fun NamedDomainObjectContainer<out AndroidSourceSet>.configureSourceSets() {
    getByName("main").java.srcDirs("src/main/kotlin")
    getByName("debug").setRoot("build-types/debug")
    getByName("release").setRoot("build-types/release")
  }

  private fun BaseAppModuleExtension.configureFlavours(properties: AtakProperties) {
    flavorDimensions += "application"
    productFlavors {
      create("civ") { f ->
        f.dimension = "application"
        f.manifestPlaceholders["atakApiVersion"] = "com.atakmap.app@${properties.atakVersion}.CIV"
      }
    }
  }
}

