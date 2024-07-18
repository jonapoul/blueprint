package blueprint.recipes

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

public fun Project.androidDesugaringBlueprint(desugaringVersion: Provider<String>) {
  extensions.getByType(CommonExtension::class).apply {
    compileOptions {
      isCoreLibraryDesugaringEnabled = true
    }
  }

  val coreLibraryDesugaring = configurations.getByName("coreLibraryDesugaring")
  dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:${desugaringVersion.get()}")
  }
}
