import groovy.lang.Closure

plugins { id("blueprint.convention") }

dependencies {
  compileOnly(libs.detekt.api)
  testImplementation(kotlin("compiler"))
  testImplementation(kotlin("test"))
  testImplementation(libs.assertk)
  testImplementation(libs.detekt.api)
  testImplementation(libs.detekt.test)
  testImplementation(libs.detekt.testJunit)
  testImplementation(libs.detekt.testUtils)
  testImplementation(libs.jetbrains.annotations)
}

buildConfig {
  sourceSets.named("test") {
    buildConfigField(
      name = "GRADLE_JARS",
      value =
        listOf(
            Action::class, // base-services
            Closure::class, // groovy
            DependencyHandlerScope::class, // kotlin-dsl
            Project::class, // core-api
            AntBuilder::class, // ant-api (supertype of Configuration since Gradle 9.5)
          )
          .map { it.java.protectionDomain.codeSource.location.path },
    )
  }
}
