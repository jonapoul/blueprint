package blueprint.kotlin

import blueprint.core.BlueprintProperties
import org.gradle.api.Project

public class KotlinProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix: String = "blueprint.kotlin"

  public val javaVersion: Int = intProperty(key = "javaVersion", default = 11)
  public val explicitApi: Boolean = boolProperty(key = "explicitApi", default = false)

  public val experimentalCoroutines: Boolean = boolProperty(key = "flags.experimentalCoroutines", default = false)
  public val allCompatibility: Boolean = boolProperty(key = "flag.allCompatibility", default = true)
}
