package blueprint.core

import org.gradle.api.Project
import org.gradle.api.provider.Provider

public fun Project.stringProperty(key: String): Provider<String> =
  providers.gradleProperty(key)

public fun Project.intProperty(key: String): Provider<Int> =
  stringProperty(key).map(String::toInt)

public fun Project.floatProperty(key: String): Provider<Float> =
  stringProperty(key).map(String::toFloat)

public fun Project.boolProperty(key: String): Provider<Boolean> =
  stringProperty(key).map(String::toBoolean)

public fun Project.doubleProperty(key: String): Provider<Double> =
  stringProperty(key).map(String::toDouble)

public fun Project.stringListProperty(key: String, delimiter: String = ","): Provider<List<String>> =
  stringProperty(key).map { string ->
    if (string.isNullOrBlank()) {
      emptyList()
    } else {
      string.split(delimiter)
    }
  }
