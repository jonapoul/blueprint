/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package blueprint.core

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

public fun Project.javaVersionInt(): Int = intProperty(key = "blueprint.javaVersion")

public fun Project.javaVersionString(): String = stringProperty(key = "blueprint.javaVersion")

public fun Project.javaVersion(): JavaVersion = JavaVersion.toVersion(javaVersionInt())

public fun Project.jvmTarget(): JvmTarget = JvmTarget.fromTarget(javaVersionString())
