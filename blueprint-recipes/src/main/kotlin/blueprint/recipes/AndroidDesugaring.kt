/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package blueprint.recipes

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

public fun Project.androidDesugaringBlueprint(desugaringVersion: Provider<String>) {
  androidDesugaringBlueprint(desugaringVersion.get())
}

public fun Project.androidDesugaringBlueprint(desugaringVersion: VersionConstraint) {
  androidDesugaringBlueprint(desugaringVersion.toString())
}

public fun Project.androidDesugaringBlueprint(desugaringVersion: String) {
  extensions.getByType(CommonExtension::class).apply {
    compileOptions {
      isCoreLibraryDesugaringEnabled = true
    }
  }

  val coreLibraryDesugaring = configurations.getByName("coreLibraryDesugaring")
  dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:$desugaringVersion")
  }
}
