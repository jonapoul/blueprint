plugins {
  id("blueprint.convention")
  alias(libs.plugins.dependencyGuard)
}

dependencyGuard {
  configuration("compileClasspath")
  configuration("runtimeClasspath")
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(kotlin("gradle-plugin"))
}
