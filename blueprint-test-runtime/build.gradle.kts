plugins {
  id("blueprint.convention")
}

dependencies {
  api(libs.junit.api)
  compileOnly(gradleTestKit())

  testCompileOnly(libs.junit.api)
  testImplementation(kotlin("stdlib"))
  testImplementation(kotlin("test"))
  testRuntimeOnly(libs.junit.launcher)
}
