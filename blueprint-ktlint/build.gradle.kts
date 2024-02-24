@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
  id("convention-kotlin")
  alias(libs.plugins.buildConfig)
  alias(libs.plugins.dokka)
  alias(libs.plugins.publish)
  `java-gradle-plugin`
}

dependencies {
  compileOnly(gradleApi())
  api(projects.blueprintCore)
  api(libs.plugin.ktlint)
}

gradlePlugin {
  plugins {
    create("blueprint-ktlint") {
      id = "dev.jonpoulton.blueprint.ktlint"
      displayName = "Ktlint Blueprint"
      description = "Gradle plugin to apply and configure Ktlint"
      implementationClass = "blueprint.ktlint.KtlintBlueprintPlugin"
    }
  }
}

buildConfig {
  packageName("blueprint.ktlint")
  buildConfigField("String", "KTLINT_CLI_VERSION", "\"${libs.versions.ktlint.cli.get()}\"")
  useKotlinOutput { internalVisibility = true }
}
