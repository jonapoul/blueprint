@file:Suppress("UnstableApiUsage")

package blueprint.android

import blueprint.kotlin.KotlinProperties
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

public class AndroidLibraryBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("dev.jonpoulton.blueprint.kotlin.android")
    target.pluginManager.apply("com.android.library")
    target.pluginManager.apply("dev.jonpoulton.blueprint.android.base")

    val androidProps = AndroidProperties(target)
    val kotlinProps = KotlinProperties(target)

    target.extensions.configure(LibraryExtension::class.java) { ext ->
      ext.configureExtension(androidProps, kotlinProps)
    }
  }

  private fun LibraryExtension.configureExtension(
    androidProps: AndroidProperties,
    kotlinProps: KotlinProperties,
  ) {
    (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") { options ->
      options.jvmTarget = kotlinProps.javaVersion.toString()
    }

    defaultConfig {
      multiDexEnabled = androidProps.multidex
    }

    buildFeatures {
      androidResources = androidProps.enableAndroidResources
      dataBinding = androidProps.enableDataBinding
      mlModelBinding = androidProps.enableMlModelBinding
      prefabPublishing = androidProps.enablePrefabPublishing
    }

    packagingOptions {
      jniLibs {
        useLegacyPackaging = androidProps.useLegacyPackaging
      }

      resources {
        pickFirsts.addAll(androidProps.packagingPickFirsts)
        excludes.addAll(androidProps.packagingExcludes)
      }
    }
  }
}
