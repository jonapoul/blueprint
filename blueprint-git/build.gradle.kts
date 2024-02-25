@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
  id("convention-kotlin")
  alias(libs.plugins.dokka)
  alias(libs.plugins.publish)
}

dependencies {
  compileOnly(gradleApi())
  api(libs.plugin.turtle)
}
