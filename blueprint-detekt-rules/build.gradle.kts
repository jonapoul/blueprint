import groovy.lang.Closure

plugins { id("blueprint.convention") }

kotlin { compilerOptions { freeCompilerArgs.add("-Xcontext-parameters") } }

dependencies {
  compileOnly(libs.detekt.api)
  testImplementation(kotlin("scripting-jvm"))
  testImplementation(kotlin("test"))
  testImplementation(libs.assertk)
  testImplementation(libs.detekt.test)
  testImplementation(libs.detekt.testJunit)
  testImplementation(libs.detekt.testUtils)
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
          )
          .map { it.java.protectionDomain.codeSource.location.path },
    )
  }
}
