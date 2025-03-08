package blueprint.diagrams.internal

import blueprint.core.isAndroidApp
import blueprint.core.isAndroidCompose
import blueprint.core.isAndroidLib
import blueprint.core.isJava
import blueprint.core.isKotlinAndroid
import blueprint.core.isKotlinJvm
import blueprint.diagrams.ModuleType
import org.gradle.api.Project

internal enum class DefaultModuleType(override val string: String, override val color: String) : ModuleType {
  AndroidApp(string = "Android App", color = "#00a300"), // darker green
  AndroidCompose(string = "Android Compose", color = "#00CC00"), // dark green
  AndroidLibrary(string = "Android Library", color = "#99FF99"), // green
  AndroidResources(string = "Android Resources", color = "#E6FFE6"), // light green
  Kotlin(string = "Kotlin", color = "#A17EFF"), // purple
  Java(string = "Java", color = "#FFBB00"), // orange
  Empty(string = "Other", color = "#E3E3E3"), // grey
}

internal class DefaultModuleTypeFinder : ModuleType.Finder {
  override fun find(project: Project): ModuleType = when {
    project.isAndroidApp() -> DefaultModuleType.AndroidApp
    project.isAndroidCompose() -> DefaultModuleType.AndroidCompose
    project.isAndroidLib() && !project.isKotlinAndroid() -> DefaultModuleType.AndroidResources
    project.isAndroidLib() -> DefaultModuleType.AndroidLibrary
    project.isKotlinJvm() -> DefaultModuleType.Kotlin
    project.isJava() -> DefaultModuleType.Java
    else -> DefaultModuleType.Empty
  }
}
