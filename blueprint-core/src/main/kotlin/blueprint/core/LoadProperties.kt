package blueprint.core

import org.gradle.api.Project
import org.gradle.internal.extensions.core.extra
import java.util.Properties

public fun Project.loadPropertiesFrom(relativePath: String): Properties {
  val directory = projectDir.resolve(relativePath)
  val propsFile = directory.resolve("gradle.properties")
  if (!propsFile.exists()) {
    error("No properties found at $propsFile")
  }

  val loadedProps = Properties()
  propsFile.inputStream().use { loadedProps.load(it) }
  loadedProps.forEach { k, v ->
    extra.set(k.toString(), v)
  }

  return loadedProps
}
