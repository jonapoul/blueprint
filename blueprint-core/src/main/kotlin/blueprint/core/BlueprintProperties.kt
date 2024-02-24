package blueprint.core

import org.gradle.api.Project

public abstract class BlueprintProperties {
  protected abstract val keyPrefix: String
  protected abstract val project: Project

  protected fun boolProperty(key: String, default: Boolean): Boolean =
    project.boolPropertyOrElse(key.prefixed, default)

  protected fun stringProperty(key: String, default: String): String =
    project.stringPropertyOrElse(key.prefixed, default)

  private val String.prefixed: String get() = "$keyPrefix.$this"
}
