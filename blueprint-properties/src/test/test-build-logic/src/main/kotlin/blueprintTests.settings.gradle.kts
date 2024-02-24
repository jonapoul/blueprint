pluginManagement {
  repositories {
    maven(url = "file://${settingsDir.absolutePath}/../../../../../build/mavenTest")
    mavenCentral()
    google()
  }
}

dependencyResolutionManagement {
  versionCatalogs.register("libs") {
    from(files("../../../../../gradle/libs.versions.toml"))
  }

  repositories {
    mavenCentral()
    google()
  }
}
