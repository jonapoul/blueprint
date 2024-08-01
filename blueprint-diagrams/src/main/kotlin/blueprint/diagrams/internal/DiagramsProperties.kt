package blueprint.diagrams.internal

import blueprint.core.boolPropertyOrElse
import blueprint.core.doublePropertyOrElse
import blueprint.core.intPropertyOrElse
import blueprint.core.stringPropertyOrElse
import org.gradle.api.Project

internal class DiagramsProperties(project: Project) {
  private val keyPrefix = "blueprint.diagrams"

  val generateModules = project.boolPropertyOrElse(key = "$keyPrefix.generateModules", default = true)
  val generateDependencies = project.boolPropertyOrElse(key = "$keyPrefix.generateDependencies", default = true)

  val showLegend = project.boolPropertyOrElse(key = "$keyPrefix.legend.show", default = true)
  val legendBackground = project.stringPropertyOrElse(key = "$keyPrefix.legend.background", default = "#bbbbbb")

  val legendTitleFontSize = project.intPropertyOrElse(key = "$keyPrefix.legend.fontSize.title", default = 20)
  val legendFontSize = project.intPropertyOrElse(key = "$keyPrefix.fontSize.legend.rows", default = 15)
  val nodeFontSize = project.intPropertyOrElse(key = "$keyPrefix.fontSize.nodes", default = 30)

  val rankSeparation = project.doublePropertyOrElse(key = "$keyPrefix.rankSeparation", default = 1.5)

  val removeModulePrefix = project.stringPropertyOrElse(key = "$keyPrefix.removeModulePrefix", default = ":modules:")
  val replacementModulePrefix = project.stringPropertyOrElse(key = "$keyPrefix.replacementModulePrefix", default = ":")
}
