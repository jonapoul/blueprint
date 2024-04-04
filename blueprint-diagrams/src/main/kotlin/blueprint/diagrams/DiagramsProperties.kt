package blueprint.diagrams

import blueprint.core.boolPropertyOrElse
import blueprint.core.doublePropertyOrElse
import blueprint.core.intPropertyOrElse
import blueprint.core.stringPropertyOrElse
import org.gradle.api.Project

internal class DiagramsProperties(project: Project) {
  private val keyPrefix = "blueprint.diagrams"

  val generateModules = project.boolPropertyOrElse(key = "generateModules", default = true)
  val generateDependencies = project.boolPropertyOrElse(key = "generateDependencies", default = true)

  val showLegend = project.boolPropertyOrElse(key = "legend.show", default = true)
  val legendBackground = project.stringPropertyOrElse(key = "legend.background", default = "#bbbbbb")

  val legendTitleFontSize = project.intPropertyOrElse(key = "legend.fontSize.title", default = 20)
  val legendFontSize = project.intPropertyOrElse(key = "fontSize.legend.rows", default = 15)
  val nodeFontSize = project.intPropertyOrElse(key = "fontSize.nodes", default = 30)

  val rankSeparation = project.doublePropertyOrElse(key = "rankSeparation", default = 1.5)
}
