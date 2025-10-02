/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("TooManyFunctions")

package blueprint.core

import groovy.lang.MissingPropertyException
import org.gradle.api.Project

public fun Project.stringPropertyOrNull(key: String): String? = properties[key]?.toString()

@Throws(MissingPropertyException::class)
public fun Project.stringProperty(key: String): String =
  stringPropertyOrNull(key) ?: throw MissingPropertyException(key, String::class.java)

public fun Project.stringPropertyOrElse(key: String, default: String): String =
  stringPropertyOrNull(key) ?: default

public fun Project.intPropertyOrNull(key: String): Int? = stringPropertyOrNull(key)?.toIntOrNull()

@Throws(MissingPropertyException::class)
public fun Project.intProperty(key: String): Int =
  intPropertyOrNull(key) ?: throw MissingPropertyException(key, Int::class.java)

public fun Project.intPropertyOrElse(key: String, default: Int): Int = intPropertyOrNull(key) ?: default

public fun Project.floatPropertyOrNull(key: String): Float? = stringPropertyOrNull(key)?.toFloatOrNull()

@Throws(MissingPropertyException::class)
public fun Project.floatProperty(key: String): Float =
  floatPropertyOrNull(key) ?: throw MissingPropertyException(key, Float::class.java)

public fun Project.floatPropertyOrElse(key: String, default: Float): Float = floatPropertyOrNull(key) ?: default

public fun Project.boolPropertyOrNull(key: String): Boolean? =
  stringPropertyOrNull(key)?.toBooleanStrictOrNull()

@Throws(MissingPropertyException::class)
public fun Project.boolProperty(key: String): Boolean =
  boolPropertyOrNull(key) ?: throw MissingPropertyException(key, Boolean::class.java)

public fun Project.boolPropertyOrElse(key: String, default: Boolean): Boolean = boolPropertyOrNull(key) ?: default

public fun Project.doublePropertyOrNull(key: String): Double? = stringPropertyOrNull(key)?.toDoubleOrNull()

@Throws(MissingPropertyException::class)
public fun Project.doubleProperty(key: String): Double =
  doublePropertyOrNull(key) ?: throw MissingPropertyException(key, Double::class.java)

public fun Project.doublePropertyOrElse(key: String, default: Double): Double = doublePropertyOrNull(key) ?: default

public fun Project.stringListPropertyOrNull(key: String, delimiter: String = ","): List<String>? {
  val string = stringPropertyOrNull(key)
  if (string.isNullOrBlank()) return null
  return string.split(delimiter)
}

@Throws(MissingPropertyException::class)
public fun Project.stringListProperty(key: String, delimiter: String = ","): List<String> =
  stringListPropertyOrNull(key, delimiter) ?: throw MissingPropertyException(key, List::class.java)

public fun Project.stringListPropertyOrElse(key: String, default: List<String>, delimiter: String = ","): List<String> =
  stringListPropertyOrNull(key, delimiter) ?: default
