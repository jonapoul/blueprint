package blueprint.publish

import blueprint.core.BlueprintProperties
import org.gradle.api.Project

internal class PublishProperties(override val project: Project) : BlueprintProperties() {
  override val keyPrefix = "blueprint.publish"

  val mavenLocalPath = stringProperty(key = "mavenLocalPath", default = BuildConfig.MAVEN_LOCAL_PATH)
}
