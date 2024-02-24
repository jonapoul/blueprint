@file:Suppress("DSL_SCOPE_VIOLATION")

buildscript {
  repositories {
    maven(url = "file://${projectDir.absolutePath}/../../../../../build/mavenTest")
    mavenCentral()
  }

  dependencies {
    val blueprintVersion by properties
    classpath("dev.jonpoulton.blueprint:properties:$blueprintVersion")
  }
}

plugins {
  alias(libs.plugins.kotlin.jvm) apply false
}
