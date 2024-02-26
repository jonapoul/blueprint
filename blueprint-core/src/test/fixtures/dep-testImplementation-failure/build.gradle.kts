buildscript {
  repositories {
    mavenLocal()
    mavenCentral()
  }

  dependencies {
    val blueprintVersion by properties
    val kotlinVersion by properties
    classpath("dev.jonpoulton.blueprint:core:$blueprintVersion")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
  }
}

apply(plugin = "org.jetbrains.kotlin.jvm")

val testImplementation by configurations
dependencies {
  testImplementation(libs.kotlinx.coroutines)
}
