package blueprint.android

import blueprint.core.VersionProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

public class AndroidHiltBlueprintPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.plugins.apply("com.google.dagger.hilt.android")

    val properties = AndroidProperties(target)
    val versions = VersionProperties(target)

    val config = when {
      properties.daggerKapt -> {
        target.plugins.apply("org.jetbrains.kotlin.kapt")
        "kapt"
      }

      properties.daggerKsp -> {
        target.plugins.apply("com.google.devtools.ksp")
        "ksp"
      }

      else -> {
        error("Need to enable KSP or KAPT for Hilt via Gradle properties!")
      }
    }

    target.dependencies.add(config, "com.google.dagger:hilt-compiler:${versions.dagger}")
    target.dependencies.add("implementation", "com.google.dagger:hilt-android:${versions.dagger}")
  }
}
