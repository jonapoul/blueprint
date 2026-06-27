package blueprint.gradle

import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension

// ExtraPropertiesExtension is IP safe and contains properties from both the root gradle.properties
// and the subproject's gradle.properties.
// https://github.com/gradle/gradle/issues/29600#issuecomment-3580868326
fun Project.findOptionalProperty(propertyName: String): String? {
  val extras = checkNotNull(extensions.findByType(ExtraPropertiesExtension::class.java))
  return if (extras.has(propertyName)) extras.get(propertyName)?.toString() else null
}
