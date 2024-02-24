package blueprint.properties

import groovy.lang.MissingPropertyException
import org.gradle.api.Project

public fun Project.stringPropertyOrNull(key: String): String? {
  return properties[key]?.toString()
}

@Throws(MissingPropertyException::class)
public fun Project.stringProperty(key: String): String {
  return stringPropertyOrNull(key) ?: throw MissingPropertyException("No string property", key, String::class.java)
}

public fun Project.intPropertyOrNull(key: String): Int? {
  return stringPropertyOrNull(key)?.toIntOrNull()
}

@Throws(MissingPropertyException::class)
public fun Project.intProperty(key: String): Int {
  return intPropertyOrNull(key) ?: throw MissingPropertyException("No int property", key, Int::class.java)
}

public fun Project.floatPropertyOrNull(key: String): Float? {
  return stringPropertyOrNull(key)?.toFloatOrNull()
}

@Throws(MissingPropertyException::class)
public fun Project.floatProperty(key: String): Float {
  return floatPropertyOrNull(key) ?: throw MissingPropertyException("No float property", key, Float::class.java)
}

public fun Project.boolPropertyOrNull(key: String): Boolean? {
  return stringPropertyOrNull(key)?.toBooleanStrictOrNull()
}

@Throws(MissingPropertyException::class)
public fun Project.boolProperty(key: String): Boolean {
  return boolPropertyOrNull(key) ?: throw MissingPropertyException("No bool property", key, Boolean::class.java)
}

public fun Project.doublePropertyOrNull(key: String): Double? {
  return stringPropertyOrNull(key)?.toDoubleOrNull()
}

@Throws(MissingPropertyException::class)
public fun Project.doubleProperty(key: String): Double {
  return doublePropertyOrNull(key) ?: throw MissingPropertyException("No double property", key, Double::class.java)
}
