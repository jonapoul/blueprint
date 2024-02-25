package blueprint.core

import org.gradle.api.Project

public fun Project.isAndroid(): Boolean = plugins.hasPlugin("com.android.base")

public fun Project.isAndroidApp(): Boolean = plugins.hasPlugin("com.android.application")

public fun Project.isAndroidLib(): Boolean = plugins.hasPlugin("com.android.library")

public fun Project.isAtak(): Boolean = plugins.hasPlugin("atak-takdev-plugin")
