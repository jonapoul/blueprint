import blueprint.properties.*

buildscript {
  repositories {
    maven(url = "file://${projectDir.absolutePath}/../../../../../build/mavenTest")
    mavenCentral()
  }

  dependencies {
    val blueprintVersion by properties
    classpath("dev.jonpoulton.blueprint:properties:$blueprintVersion")
    classpath(libs.plugin.kotlin)
  }
}

plugins {
  `base`
}

val propertyValue = doubleProperty("testProperty")
println("testProperty = $propertyValue")
