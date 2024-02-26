@file:Suppress("DSL_SCOPE_VIOLATION")

buildscript {
  repositories {
    mavenLocal()
    mavenCentral()
  }

  dependencies {
    val blueprintVersion by properties
    classpath("dev.jonpoulton.blueprint:core:$blueprintVersion")
  }
}

plugins {
  alias(libs.plugins.kotlin.jvm) apply false
}
