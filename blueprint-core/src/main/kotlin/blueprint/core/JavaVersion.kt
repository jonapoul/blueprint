package blueprint.core

import org.gradle.api.JavaVersion
import org.gradle.api.Project

public fun Project.javaVersionInt(): Int = intProperty(key = "javaVersion")

public fun Project.javaVersionString(): String = stringProperty(key = "javaVersion")

public fun Project.javaVersion(): JavaVersion = JavaVersion.toVersion(javaVersionInt())
