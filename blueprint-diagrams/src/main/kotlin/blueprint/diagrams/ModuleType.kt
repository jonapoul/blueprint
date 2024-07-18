package blueprint.diagrams

import blueprint.core.isAndroidApp
import blueprint.core.isAndroidCompose
import blueprint.core.isAndroidLib
import blueprint.core.isAtak
import blueprint.core.isJava
import blueprint.core.isKotlinAndroid
import blueprint.core.isKotlinJvm
import guru.nidi.graphviz.attribute.Color
import org.gradle.api.Project

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
  isAtak() && isAndroidApp() -> ModuleType.AtakPlugin
  isAtak() && isAndroidLib() -> ModuleType.AtakLibrary
  isAndroidApp() -> ModuleType.AndroidApp
  isAndroidCompose() -> ModuleType.AndroidCompose
  isAndroidLib() && !isKotlinAndroid() -> ModuleType.AndroidResources
  isAndroidLib() -> ModuleType.AndroidLibrary
  isKotlinJvm() -> ModuleType.Kotlin
  isJava() -> ModuleType.Java
  else -> ModuleType.Empty
}

internal fun Project.projColor(): Color = Color.rgb(moduleType().color).fill()
