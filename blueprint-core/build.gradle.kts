plugins {
  id("blueprint.convention")
}

dependencyGuard {
  configuration("compileClasspath")
  configuration("runtimeClasspath")
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(kotlin("gradle-plugin"))
}
