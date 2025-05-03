plugins {
  alias(libs.plugins.convention.kotlin)
  alias(libs.plugins.convention.publish)
  `java-gradle-plugin`
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(gradleKotlinDsl())
  compileOnly(libs.plugin.agp)
  compileOnly(libs.plugin.androidCacheFix)
  compileOnly(libs.plugin.compose)
  compileOnly(libs.plugin.dependencySort)
  compileOnly(libs.plugin.dependencyVersions)
  compileOnly(libs.plugin.detekt)
  compileOnly(libs.plugin.kotlin)
  compileOnly(libs.plugin.kover)
  compileOnly(libs.plugin.ktlint)
  compileOnly(libs.plugin.licensee)
  compileOnly(libs.plugin.powerAssert)
  compileOnly(libs.plugin.spotless)
  implementation(projects.blueprintCore)
  testImplementation(gradleTestKit())
  testImplementation(libs.test.junit)
  testImplementation(libs.test.kotlin.common)
  testImplementation(libs.test.kotlin.junit)
}

gradlePlugin {
  plugins {
    add(id = "android.base", impl = "AndroidBaseBlueprint")
    add(id = "android.desugaring", impl = "AndroidDesugaringBlueprint")
    add(id = "android.library", impl = "AndroidLibraryBlueprint")
    add(id = "android.resources", impl = "AndroidResourcesBlueprint")
    add(id = "compose", impl = "ComposeBlueprint")
    add(id = "detekt", impl = "DetektBlueprint")
    add(id = "idea", impl = "IdeaBlueprint")
    add(id = "kotlin.base", impl = "KotlinBaseBlueprint")
    add(id = "kotlin.jvm", impl = "KotlinJvmBlueprint")
    add(id = "kover", impl = "KoverBlueprint")
    add(id = "ktlint", impl = "KtlintBlueprint")
    add(id = "licensee", impl = "LicenseeBlueprint")
    add(id = "sortdependencies", impl = "SortDependenciesBlueprint")
    add(id = "spotless", impl = "SpotlessBlueprint")
    add(id = "test.base", impl = "TestBaseBlueprint")
    add(id = "test.powerassert", impl = "PowerAssertBlueprint")
    add(id = "versions", impl = "VersionsBlueprint")
  }
}

private fun NamedDomainObjectContainer<PluginDeclaration>.add(id: String, impl: String) {
  create(id) {
    this.id = "dev.jonpoulton.blueprint.$id"
    this.implementationClass = "blueprint.recipes.$impl"
  }
}
