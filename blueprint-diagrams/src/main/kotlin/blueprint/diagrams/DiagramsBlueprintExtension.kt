package blueprint.diagrams

import blueprint.diagrams.internal.DefaultModuleType
import blueprint.diagrams.internal.DefaultModuleTypeFinder
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import javax.inject.Inject

public open class DiagramsBlueprintExtension @Inject constructor(objects: ObjectFactory) {
  public val moduleTypes: SetProperty<ModuleType> = objects
    .setProperty(ModuleType::class.java)
    .convention(DefaultModuleType.values().toSet())

  public val moduleTypeFinder: Property<ModuleType.Finder> = objects
    .property(ModuleType.Finder::class.java)
    .convention(DefaultModuleTypeFinder())
}
