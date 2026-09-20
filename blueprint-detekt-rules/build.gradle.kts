plugins { id("blueprint.convention") }

dependencies {
  compileOnly(libs.detekt.api)
}

tasks.withType(Test::class).configureEach {
  enabled = false
}
