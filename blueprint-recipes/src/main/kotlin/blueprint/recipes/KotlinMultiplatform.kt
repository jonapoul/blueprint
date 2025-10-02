/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package blueprint.recipes

import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

public fun Project.kotlinMultiplatformBlueprint(
  freeCompilerArgs: List<String> = DEFAULT_KOTLIN_FREE_COMPILER_ARGS,
) {
  with(pluginManager) {
    apply(KotlinMultiplatformPluginWrapper::class)
  }

  androidLibBlueprint()

  extensions.configure<KotlinMultiplatformExtension> {
    compilerOptions {
      this.freeCompilerArgs.addAll(freeCompilerArgs)
    }

    jvm()
    androidTarget()

//     androidNativeArm32()
//     androidNativeArm64()
//     androidNativeX64()
//     androidNativeX86()
//     iosArm64()
//     iosSimulatorArm64()
//     linuxArm64()
//     linuxX64()
//     macosArm64()
//     macosX64()
//     mingwX64()
//     tvosArm64()
//     tvosSimulatorArm64()
//     tvosX64()
//     watchosArm32()
//     watchosArm64()
//     watchosDeviceArm64()
//     watchosSimulatorArm64()
//     watchosX64()
  }
}
