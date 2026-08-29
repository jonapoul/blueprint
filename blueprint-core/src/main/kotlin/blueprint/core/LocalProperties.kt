@file:Suppress("ExplicitCollectionElementAccessMethod", "UnstableApiUsage")

package blueprint.core

import java.util.Properties
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.initialization.Settings
import org.gradle.api.logging.Logging
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive

public fun Project.localProperties(
  filename: String = "local.properties"
): Provider<Map<String, String>> = LocalPropertiesService.registerOrGet(this, filename).properties

public fun Settings.localProperties(
  filename: String = "local.properties"
): Provider<Map<String, String>> =
  providers.of(LocalPropertiesValueSource::class.java) { spec ->
    spec.parameters { params ->
      val propsFile = rootProject.projectDir.resolve(filename)
      params.propertiesFile.set(propsFile)
    }
  }

public fun Provider<Map<String, String>>.getOptional(key: String): String? = map { props ->
  props[key]
}
  .orNull
  ?.takeIf { it.isNotEmpty() }

private interface LocalPropertiesService : BuildService<LocalPropertiesService.Parameters> {
  interface Parameters : BuildServiceParameters {
    val properties: MapProperty<String, String>
  }

  val properties: Provider<Map<String, String>>
    get() = parameters.properties

  companion object {
    fun registerOrGet(project: Project, filename: String): LocalPropertiesService =
      project.gradle.sharedServices
        .registerIfAbsent(
          "localPropertiesService-$filename",
          LocalPropertiesService::class.java,
        ) { spec ->
          val propsFile = project.rootProject.isolated.projectDirectory.file(filename)
          spec.parameters.properties.set(
            project.providers.of(LocalPropertiesValueSource::class.java) { source ->
              source.parameters.propertiesFile.set(propsFile)
            }
          )
        }
        .get()
  }
}

private abstract class LocalPropertiesValueSource :
  ValueSource<Map<String, String>, LocalPropertiesValueSource.Parameters> {
  private val logger = Logging.getLogger(LocalPropertiesValueSource::class.java)

  interface Parameters : ValueSourceParameters {
    @get:InputFile @get:PathSensitive(RELATIVE) val propertiesFile: RegularFileProperty
  }

  override fun obtain(): Map<String, String> {
    val file = parameters.propertiesFile.asFile.get()
    logger.info("Reading local properties from {}", file)
    if (!file.isFile) return emptyMap()
    return Properties()
      .apply { file.reader().use(::load) }
      .run { stringPropertyNames().associateWith(::getProperty) }
  }
}
