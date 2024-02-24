package blueprint.core

import org.gradle.api.Project

public abstract class BlueprintProperties {
  protected abstract val keyPrefix: String
  protected abstract val project: Project

  protected fun boolProperty(key: String, default: Boolean): Boolean =
    project.boolPropertyOrElse(key.prefixed, default)

  protected fun stringProperty(key: String, default: String): String =
    project.stringPropertyOrElse(key.prefixed, default)

  protected fun stringPropertyOrNull(key: String): String? =
    project.stringPropertyOrNull(key.prefixed)

  protected fun stringListPropertyOrNull(key: String): List<String>? {
    val string = project.stringPropertyOrNull(key.prefixed)
    if (string.isNullOrBlank()) return null
    return string.split(COMMA)
  }

  protected fun stringListProperty(key: String, default: List<String>): List<String> =
    stringListPropertyOrNull(key) ?: default

  private val String.prefixed: String get() = "$keyPrefix.$this"

  private companion object {
    const val COMMA = ","
  }
}
