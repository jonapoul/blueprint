plugins { id("blueprint.convention") }

dependencies {
  api(libs.junit.api)
  implementation(libs.jetbrains.annotations)
  compileOnly(gradleTestKit())
  testImplementation(kotlin("stdlib"))
  testImplementation(kotlin("test"))
  testCompileOnly(libs.junit.api)
  testRuntimeOnly(libs.junit.launcher)
}
