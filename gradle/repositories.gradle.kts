pluginManagement {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    maven("https://central.sonatype.com/repository/maven-snapshots/") {
      content { includeGroup("dev.detekt") }
    }
  }
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    maven("https://central.sonatype.com/repository/maven-snapshots/") {
      content { includeGroup("dev.detekt") }
    }
  }
}
