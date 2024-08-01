package blueprint.diagrams

import guru.nidi.graphviz.attribute.Color
import org.gradle.api.Project

public interface ModuleType {
  public val string: String
  public val color: String

  public fun interface Finder {
    public fun find(project: Project): ModuleType
  }
}

public fun ModuleType.Finder.color(project: Project): Color = Color.rgb(find(project).color).fill()
