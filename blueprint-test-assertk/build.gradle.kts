plugins {
  id("blueprint.convention")
}

dependencies {
  api(libs.assertk)
  api(project(":blueprint-test-runtime"))
  compileOnly(gradleTestKit())
}
