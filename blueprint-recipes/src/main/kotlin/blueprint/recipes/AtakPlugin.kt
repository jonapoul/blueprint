@file:Suppress("UnstableApiUsage", "CyclomaticComplexMethod", "ThrowsCount", "LongMethod")

package blueprint.recipes

import blueprint.core.getValue
import blueprint.core.gitVersionCode
import blueprint.core.gitVersionName
import blueprint.core.intProperty
import blueprint.core.localProperties
import blueprint.core.provideDelegate
import blueprint.core.stringProperty
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import java.io.File

public fun Project.atakPluginBlueprint(
  kotlinxDatetimeVersion: Provider<String>?,
) {
  with(plugins) {
    apply("org.jetbrains.kotlin.android")
  }

  val gitVersionName = gitVersionName()
  val gitVersionCode = gitVersionCode()

  val pluginVersion = stringProperty(key = "blueprint.atak.pluginVersion")
  val apkName = stringProperty(key = "blueprint.atak.apkName")
  val atakVersion = stringProperty(key = "blueprint.atak.appVersion")

  extensions.getByType(ApplicationExtension::class).apply {
    defaultConfig.apply {
      versionCode = gitVersionCode
      versionName = "$pluginVersion (${gitVersionName}) - [$atakVersion]"
      targetSdk = intProperty(key = "blueprint.android.targetSdk")
      buildConfigField("String", "GIT_HASH", "\"${gitVersionName}\"")

      if (kotlinxDatetimeVersion != null) {
        buildConfigField(
          "kotlinx.datetime.Instant",
          "BUILD_TIME",
          "kotlinx.datetime.Instant.Companion.fromEpochMilliseconds(${System.currentTimeMillis()}L)",
        )
      }
    }

    bundle {
      storeArchive {
        enable = false
      }
    }

    lint {
      checkReleaseBuilds = true
      abortOnError = true
      warning.add("ExpiredTargetSdkVersion")
    }

    signingConfigs {
      getByName("debug").apply {
        val localProps = rootProject.localProperties()
        val kf = localProps["takDebugKeyFile"]?.toString()
        val kfp = localProps["takDebugKeyFilePassword"]?.toString()
        val ka = localProps["takDebugKeyAlias"]?.toString()
        val kp = localProps["takDebugKeyPassword"]?.toString()

        if (kf == null) throw GradleException("No signing key configured!")
        val file = File(rootProject.projectDir, kf)
        if (!file.exists()) throw GradleException("File $file doesn't exist")

        storeFile = file
        if (kfp != null) storePassword = kfp
        if (ka != null) keyAlias = ka
        if (kp != null) keyPassword = kp
      }
      create("release").apply {
        val localProps = rootProject.localProperties()
        val kf = localProps["takReleaseKeyFile"]?.toString()
        val kfp = localProps["takReleaseKeyFilePassword"]?.toString()
        val ka = localProps["takReleaseKeyAlias"]?.toString()
        val kp = localProps["takReleaseKeyPassword"]?.toString()

        if (kf == null) throw GradleException("No signing key configured!")
        val file = File(rootProject.projectDir, kf)
        if (!file.exists()) throw GradleException("File $file doesn't exist")

        storeFile = file
        if (kfp != null) storePassword = kfp
        if (ka != null) keyAlias = ka
        if (kp != null) keyPassword = kp
      }
    }

    buildTypes {
      debug {
        isDebuggable = true
        matchingFallbacks.add("sdk")
        signingConfig = signingConfigs.getByName("debug")
      }

      release {
        matchingFallbacks.add("odk")
        isMinifyEnabled = true
        signingConfig = signingConfigs.getByName("release")
        if (isTakGovPipeline()) {
          proguardFiles("proguard-rules.pro", "proguard-mapping.pro")
        } else {
          proguardFile("proguard-rules.pro")
        }
      }
    }

    packagingOptions {
      resources.excludes.addAll(
        listOf(
          "META-INF/DEPENDENCIES",
          "META-INF/INDEX.LIST",
          "META-INF/*.kotlin_module",
        ),
      )

      jniLibs {
        useLegacyPackaging = true
      }
    }

    buildFeatures {
      buildConfig = true
    }

    sourceSets {
      getByName("main").apply {
        java.srcDirs("src/main/kotlin")
        setProperty("archivesBaseName", "ATAK-Plugin-$apkName-${pluginVersion}-${gitVersionName}-${atakVersion}")
      }

      getByName("debug").setRoot("build-types/debug")
      getByName("release").setRoot("build-types/release")
    }

    flavorDimensions += "application"
    productFlavors {
      create("civ").apply {
        dimension = "application"
        manifestPlaceholders["atakApiVersion"] = "com.atakmap.app@${atakVersion}.CIV"
      }
    }

    val implementation by configurations
    dependencies {
      if (kotlinxDatetimeVersion != null) {
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:${kotlinxDatetimeVersion.get()}")
      }
    }
  }
}
