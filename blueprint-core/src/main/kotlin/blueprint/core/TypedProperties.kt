@file:Suppress("TooManyFunctions")

package blueprint.core

import groovy.lang.MissingPropertyException
import org.gradle.api.Project

public fun Project.stringPropertyOrNull(key: String): String? {
  return properties[key]?.toString()
}

@Throws(MissingPropertyException::class)
public fun Project.stringProperty(key: String): String {
  return stringPropertyOrNull(key) ?: throw MissingPropertyException(key, String::class.java)
}

public fun Project.stringPropertyOrElse(key: String, default: String): String {
  return stringPropertyOrNull(key) ?: default
}

public fun Project.intPropertyOrNull(key: String): Int? {
  return stringPropertyOrNull(key)?.toIntOrNull()
}

@Throws(MissingPropertyException::class)
public fun Project.intProperty(key: String): Int {
  return intPropertyOrNull(key) ?: throw MissingPropertyException(key, Int::class.java)
}

public fun Project.intPropertyOrElse(key: String, default: Int): Int {
  return intPropertyOrNull(key) ?: default
}

public fun Project.floatPropertyOrNull(key: String): Float? {
  return stringPropertyOrNull(key)?.toFloatOrNull()
}

@Throws(MissingPropertyException::class)
public fun Project.floatProperty(key: String): Float {
  return floatPropertyOrNull(key) ?: throw MissingPropertyException(key, Float::class.java)
}

public fun Project.floatPropertyOrElse(key: String, default: Float): Float {
  return floatPropertyOrNull(key) ?: default
}

public fun Project.boolPropertyOrNull(key: String): Boolean? {
  return stringPropertyOrNull(key)?.toBooleanStrictOrNull()
}

@Throws(MissingPropertyException::class)
public fun Project.boolProperty(key: String): Boolean {
  return boolPropertyOrNull(key) ?: throw MissingPropertyException(key, Boolean::class.java)
}

public fun Project.boolPropertyOrElse(key: String, default: Boolean): Boolean {
  return boolPropertyOrNull(key) ?: default
}

public fun Project.doublePropertyOrNull(key: String): Double? {
  return stringPropertyOrNull(key)?.toDoubleOrNull()
}

@Throws(MissingPropertyException::class)
public fun Project.doubleProperty(key: String): Double {
  return doublePropertyOrNull(key) ?: throw MissingPropertyException(key, Double::class.java)
}

public fun Project.doublePropertyOrElse(key: String, default: Double): Double {
  return doublePropertyOrNull(key) ?: default
}

public fun Project.stringListPropertyOrNull(key: String): List<String>? {
  val string = stringPropertyOrNull(key)
  if (string.isNullOrBlank()) return null
  return string.split(",")
}

@Throws(MissingPropertyException::class)
public fun Project.stringListProperty(key: String): List<String> {
  return stringListPropertyOrNull(key)
    ?: throw MissingPropertyException(key, List::class.java)
}

public fun Project.stringListPropertyOrElse(key: String, default: List<String>): List<String> {
  return stringListPropertyOrNull(key) ?: default
}
