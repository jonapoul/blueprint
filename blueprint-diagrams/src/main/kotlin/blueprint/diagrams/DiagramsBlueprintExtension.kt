@file:Suppress("MagicNumber")

package blueprint.diagrams

import blueprint.diagrams.internal.DefaultModuleType
import blueprint.diagrams.internal.DefaultModuleTypeFinder
import guru.nidi.graphviz.attribute.Rank
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import javax.inject.Inject

public open class DiagramsBlueprintExtension @Inject constructor(
  target: Project,
  objects: ObjectFactory,
) {
  public val generateModules: Property<Boolean> = objects
    .property(Boolean::class.java)
    .convention(true)

  public val generateDependencies: Property<Boolean> = objects
    .property(Boolean::class.java)
    .convention(true)

  public val showLegend: Property<Boolean> = objects
    .property(Boolean::class.java)
    .convention(true)

  public val legendBackground: Property<String> = objects
    .property(String::class.java)
    .convention("#bbbbbb")

  public val legendTitleFontSize: Property<Int> = objects
    .property(Int::class.java)
    .convention(20)

  public val legendFontSize: Property<Int> = objects
    .property(Int::class.java)
    .convention(15)

  public val nodeFontSize: Property<Int> = objects
    .property(Int::class.java)
    .convention(30)

  public val removeModulePrefix: Property<String> = objects
    .property(String::class.java)
    .convention(":modules:")

  public val replacementModulePrefix: Property<String> = objects
    .property(String::class.java)
    .convention(":")

  public val rankDir: Property<Rank.RankDir> = objects
    .property(Rank.RankDir::class.java)
    .convention(Rank.RankDir.TOP_TO_BOTTOM)

  public val rankSeparation: Property<Double> = objects
    .property(Double::class.java)
    .convention(1.5)

  public val topLevelProject: Property<String> = objects
    .property(String::class.java)
    .convention(target.path)

  public val checkModulesDotfile: Property<Boolean> = objects
    .property(Boolean::class.java)
    .convention(true)

  public val checkDependenciesDotfile: Property<Boolean> = objects
    .property(Boolean::class.java)
    .convention(false)

  public val moduleTypes: SetProperty<ModuleType> = objects
    .setProperty(ModuleType::class.java)
    .convention(DefaultModuleType.entries.toSet())

  public val moduleTypeFinder: Property<ModuleType.Finder> = objects
    .property(ModuleType.Finder::class.java)
    .convention(DefaultModuleTypeFinder())
}
