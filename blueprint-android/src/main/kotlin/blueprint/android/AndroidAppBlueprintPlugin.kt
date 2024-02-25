@file:Suppress("UnstableApiUsage")

package blueprint.android

import blueprint.kotlin.KotlinProperties
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

/**
 * TODO: Flesh this out?
 */
public class AndroidAppBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("dev.jonpoulton.blueprint.kotlin.android")
    target.pluginManager.apply("com.android.application")
    target.pluginManager.apply("dev.jonpoulton.blueprint.android.base")

    val androidProps = AndroidProperties(target)
    val kotlinProps = KotlinProperties(target)

    target.extensions.configure(BaseAppModuleExtension::class.java) { ext ->
      ext.configureExtension(androidProps, kotlinProps)
    }
  }

  private fun BaseAppModuleExtension.configureExtension(
    androidProps: AndroidProperties,
    kotlinProps: KotlinProperties,
  ) {
    (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") { options ->
      options.jvmTarget = kotlinProps.javaVersion.toString()
    }

    defaultConfig {
      targetSdk = androidProps.targetSdk

      if (androidProps.includeTimestamp) {
        buildConfigField(
          "kotlinx.datetime.Instant",
          "BUILD_TIME",
          "kotlinx.datetime.Instant.Companion.fromEpochMilliseconds(${System.currentTimeMillis()}L)",
        )
      }
    }
  }
}
