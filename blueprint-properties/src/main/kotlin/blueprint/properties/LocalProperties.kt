package blueprint.properties

import org.gradle.api.Project
import java.io.File
import java.io.FileNotFoundException
import java.util.Properties

public fun Project.localProperties(filename: String = DEFAULT_FILENAME): Properties {
  val file = File(rootProject.projectDir, filename)
  if (!file.exists() || !file.isFile) {
    throw FileNotFoundException("No properties file found at ${file.absolutePath}")
  }
  val props = Properties()
  file.reader().use { props.load(it) }
  return props
}

public fun Project.localPropertiesOrNull(filename: String = DEFAULT_FILENAME): Properties? {
  return try {
    localProperties(filename)
  } catch (e: FileNotFoundException) {
    logger.warn(e.toString())
    null
  }
}

public fun Project.rootLocalProperties(filename: String = DEFAULT_FILENAME): Properties =
  rootProject.localProperties(filename)

public fun Project.rootLocalPropertiesOrNull(filename: String = DEFAULT_FILENAME): Properties? =
  rootProject.localPropertiesOrNull(filename)

private const val DEFAULT_FILENAME = "local.properties"
