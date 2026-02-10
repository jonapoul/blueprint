plugins {
  id("blueprint.convention")
}

dependencies {
  api(libs.assertk)
  api(project(":blueprint-test-runtime"))
  compileOnly(gradleTestKit())

  testCompileOnly(libs.junit.api)
  testImplementation(kotlin("stdlib"))
  testImplementation(kotlin("test"))
  testRuntimeOnly(libs.junit.launcher)
}
