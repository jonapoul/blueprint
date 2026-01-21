package blueprint.core

import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.NONE
import java.util.Properties

public fun Project.localProperties(
  filename: String = "local.properties",
): Provider<Map<String, String>> = providers.of(LocalPropertiesValueSource::class.java) { spec ->
  spec.parameters { params ->
    @Suppress("UnstableApiUsage")
    val propsFile = rootProject.isolated.projectDirectory.file(filename)
    params.propertiesFile.set(propsFile)
  }
}

public fun Provider<Map<String, String>>.getOptional(key: String): String? =
  map { it[key] ?: error("No '$key' in ${it.keys}") }
    .orNull
    ?.takeIf { it.isNotEmpty() }

private abstract class LocalPropertiesValueSource :
  ValueSource<Map<String, String>, LocalPropertiesValueSource.Parameters> {
  interface Parameters : ValueSourceParameters {
    @get:[InputFile PathSensitive(NONE)] val propertiesFile: RegularFileProperty
  }

  override fun obtain(): Map<String, String> {
    val file = parameters.propertiesFile.asFile.get()
    if (!file.exists()) return emptyMap()
    return Properties()
      .apply { file.reader().use(::load) }
      .run { stringPropertyNames().associateWith(::getProperty) }
  }
}
