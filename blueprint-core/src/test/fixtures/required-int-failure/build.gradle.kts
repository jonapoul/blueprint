import blueprint.core.*

buildscript {
  repositories {
    mavenLocal()
    mavenCentral()
  }

  dependencies {
    val blueprintVersion by properties
    classpath("dev.jonpoulton.blueprint:core:$blueprintVersion")
    classpath(libs.plugin.kotlin)
  }
}

plugins {
  `base`
}

val propertyValue = intProperty("testProperty")
println("testProperty = $propertyValue")
