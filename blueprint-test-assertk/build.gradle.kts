plugins { id("blueprint.convention") }

dependencies {
  api(project(":blueprint-test-runtime"))
  api(libs.assertk)
  compileOnly(gradleTestKit())
  testImplementation(kotlin("stdlib"))
  testImplementation(kotlin("test"))
  testCompileOnly(libs.junit.api)
  testRuntimeOnly(libs.junit.launcher)
}
