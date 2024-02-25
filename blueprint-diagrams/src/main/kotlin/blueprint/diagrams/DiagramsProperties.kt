package blueprint.diagrams

import blueprint.core.BlueprintProperties
import guru.nidi.graphviz.attribute.Shape
import org.gradle.api.Project

internal class DiagramsProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix = "blueprint.diagrams"

  val generateModules = boolProperty(key = "generateModules", default = true)
  val generateDependencies = boolProperty(key = "generateDependencies", default = true)

  val showLegend = boolProperty(key = "legend.show", default = true)
  val legendBackground = stringProperty(key = "legend.background", default = "#bbbbbb")

  val legendTitleFontSize = intProperty(key = "legend.fontSize.title", default = 20)
  val legendFontSize = intProperty(key = "fontSize.legend.rows", default = 15)
  val nodeFontSize = intProperty(key = "fontSize.nodes", default = 30)

  val rankSeparation = doubleProperty(key = "rankSeparation", default = 1.5)
}
