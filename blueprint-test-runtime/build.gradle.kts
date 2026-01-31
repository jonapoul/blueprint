plugins {
  id("blueprint.convention")
}

dependencies {
  api(libs.junit.api)
  compileOnly(gradleTestKit())
}
