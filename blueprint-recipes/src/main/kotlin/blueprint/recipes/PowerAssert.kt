/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package blueprint.recipes

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradlePlugin

@ExperimentalKotlinGradlePluginApi
public fun Project.powerAssertBlueprint(
  functions: Set<String> = DEFAULT_POWER_ASSERT_FUNCTIONS,
) {
  with(pluginManager) {
    apply(PowerAssertGradlePlugin::class.java)
  }

  extensions.configure<PowerAssertGradleExtension> {
    this.functions.addAll(functions)
  }
}
