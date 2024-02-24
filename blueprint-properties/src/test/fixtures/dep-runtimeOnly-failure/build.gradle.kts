buildscript {
  repositories {
    maven(url = "file://${projectDir.absolutePath}/../../../../../build/mavenTest")
    mavenCentral()
  }

  dependencies {
    val blueprintVersion by properties
    val kotlinVersion by properties
    classpath("dev.jonpoulton.blueprint:properties:$blueprintVersion")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
  }
}

apply(plugin = "org.jetbrains.kotlin.jvm")

val runtimeOnly by configurations
dependencies {
  runtimeOnly(libs.kotlinx.coroutines)
}
