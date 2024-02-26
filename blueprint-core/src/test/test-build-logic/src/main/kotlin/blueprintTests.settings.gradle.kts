pluginManagement {
  repositories {
    mavenLocal()
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
