package blueprint.diagrams

import guru.nidi.graphviz.attribute.Color
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

internal enum class ModuleType(val string: String, val color: String) {
  AtakPlugin(string = "ATAK Plugin", color = "#0091E6"), // dark blue
  AtakLibrary(string = "ATAK Library", color = "#7ACEFF"), // blue
  AndroidApp(string = "Android App", color = "#00a300"), // darker green
  AndroidCompose(string = "Android Compose", color = "#00CC00"), // dark green
  AndroidLibrary(string = "Android Library", color = "#99FF99"), // green
  AndroidResources(string = "Android Resources", color = "#E6FFE6"), // light green
  Kotlin(string = "Kotlin", color = "#A17EFF"), // purple
  Java(string = "Java", color = "#FFBB00"), // orange
  Empty(string = "Other", color = "#E3E3E3"), // grey
}

internal fun Project.moduleType(): ModuleType = when {
  plugins.hasPlugin("dev.jonpoulton.blueprint.atak.plugin") -> ModuleType.AtakPlugin
  plugins.hasPlugin("dev.jonpoulton.blueprint.atak.library") -> ModuleType.AtakLibrary
  plugins.hasPlugin("dev.jonpoulton.blueprint.android.app") -> ModuleType.AndroidApp
  plugins.hasPlugin("dev.jonpoulton.blueprint.android.compose") -> ModuleType.AndroidCompose
  plugins.hasPlugin("dev.jonpoulton.blueprint.android.resources") -> {
    if (plugins.hasPlugin("dev.jonpoulton.blueprint.kotlin.base")) {
      ModuleType.AndroidLibrary
    } else {
      ModuleType.AndroidResources
    }
  }
  plugins.hasPlugin("dev.jonpoulton.blueprint.android.base") -> ModuleType.AndroidLibrary
  plugins.hasPlugin("dev.jonpoulton.blueprint.kotlin.base") -> ModuleType.Kotlin
  plugins.hasPlugin(JavaPlugin::class.java) -> ModuleType.Java
  else -> ModuleType.Empty
}

internal fun Project.projColor(): Color = Color.rgb(moduleType().color).fill()
