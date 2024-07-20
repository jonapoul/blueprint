@file:Suppress("UnstableApiUsage")

package blueprint.recipes

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

public fun Project.androidLibBlueprint(
  kotlinVersion: Provider<String>,
  explicitApi: Boolean = true,
  freeCompilerArgs: List<String> = DEFAULT_KOTLIN_FREE_COMPILER_ARGS,
) {
  with(plugins) {
    apply("org.jetbrains.kotlin.android")
    apply("com.android.library")
  }

  kotlinBlueprint(kotlinVersion, explicitApi, freeCompilerArgs)
  androidBaseBlueprint()

  extensions.getByType(LibraryExtension::class).apply {
    buildFeatures {
      // Enabled in modules that need them
      androidResources = false

      // Unused
      dataBinding = false
      mlModelBinding = false
      prefabPublishing = false
    }

    packagingOptions {
      resources.excludes.add("META-INF/*")
    }
  }
}
