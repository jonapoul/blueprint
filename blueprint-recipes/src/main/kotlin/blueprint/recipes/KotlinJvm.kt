/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package blueprint.recipes

import blueprint.core.javaVersionInt
import blueprint.core.jvmTarget
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public fun Project.kotlinJvmBlueprint(
  freeCompilerArgs: List<String> = DEFAULT_KOTLIN_FREE_COMPILER_ARGS,
) {
  kotlinBaseBlueprint(freeCompilerArgs)

  tasks.withType<KotlinCompile> {
    compilerOptions {
      jvmTarget.set(project.jvmTarget())
    }
  }

  extensions.getByType(KotlinBaseExtension::class).apply {
    jvmToolchain(javaVersionInt())
  }
}
