package blueprint.diagrams

import guru.nidi.graphviz.attribute.Color
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

internal enum class ModuleType(val string: String, val color: String) {
  AtakPlugin(string = "ATAK Plugin", color = "#0091E6"), // dark blue
  AtakLibrary(string = "ATAK Library", color = "#7ACEFF"), // blue
  AndroidApp(string = "App", color = "#66CCFF"), // cyan
  AndroidCompose(string = "Compose", color = "#00C00"), // dark green
  AndroidResources(string = "Resources", color = "#E6ffE6"), // light green
  Android(string = "Android", color = "#99FF99"), // green
  Kotlin(string = "Kotlin", color = "#A17EFF"), // purple
  Java(string = "Java", color = "#FFBB00"), // orange
}

internal fun Project.moduleType(): ModuleType = when {
  plugins.hasPlugin("dev.jonpoulton.blueprint.atak.app") -> ModuleType.AtakPlugin
  plugins.hasPlugin("dev.jonpoulton.blueprint.atak.lib") -> ModuleType.AtakLibrary
  plugins.hasPlugin("dev.jonpoulton.blueprint.android.app") -> ModuleType.AndroidApp
  plugins.hasPlugin("dev.jonpoulton.blueprint.android.compose") -> ModuleType.AndroidCompose
  plugins.hasPlugin("dev.jonpoulton.blueprint.android.resources") -> ModuleType.AndroidResources
  plugins.hasPlugin("dev.jonpoulton.blueprint.android.base") -> ModuleType.Android
  plugins.hasPlugin("dev.jonpoulton.blueprint.kotlin.base") -> ModuleType.Kotlin
  plugins.hasPlugin(JavaPlugin::class.java) -> ModuleType.Java
  else -> error("Unknown module type: $this")
}

internal fun Project.projColor(): Color = Color.rgb(moduleType().color).fill()
