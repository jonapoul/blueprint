package blueprint.core

import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory

public fun ProviderFactory.intProperty(key: String): Provider<Int> =
  gradleProperty(key).map(String::toInt)

public fun ProviderFactory.floatProperty(key: String): Provider<Float> =
  gradleProperty(key).map(String::toFloat)

public fun ProviderFactory.boolProperty(key: String): Provider<Boolean> =
  gradleProperty(key).map(String::toBoolean)

public fun ProviderFactory.doubleProperty(key: String): Provider<Double> =
  gradleProperty(key).map(String::toDouble)

public fun ProviderFactory.stringListProperty(
  key: String,
  delimiter: String = ",",
): Provider<List<String>> =
  gradleProperty(key).map { string ->
    if (string.isBlank()) {
      emptyList()
    } else {
      string.split(delimiter)
    }
  }
