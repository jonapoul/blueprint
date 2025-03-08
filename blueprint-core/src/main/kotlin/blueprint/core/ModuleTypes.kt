@file:Suppress("UnstableApiUsage")

package blueprint.core

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.kotlin.dsl.findByType

public fun Project.isAndroid(): Boolean = plugins.hasPlugin("com.android.base")

public fun Project.isAndroidApp(): Boolean = plugins.hasPlugin("com.android.application")

public fun Project.isAndroidCompose(): Boolean {
  return extensions.findByType(CommonExtension::class)?.buildFeatures?.compose == true
}

public fun Project.isAndroidLib(): Boolean = plugins.hasPlugin("com.android.library")

public fun Project.isKotlinJvm(): Boolean = plugins.hasPlugin("org.jetbrains.kotlin.jvm")

public fun Project.isKotlinAndroid(): Boolean = plugins.hasPlugin("org.jetbrains.kotlin.android")

public fun Project.isJava(): Boolean = plugins.hasPlugin(JavaPlugin::class.java)
