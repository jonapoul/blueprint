package blueprint.core

import org.gradle.api.NamedDomainObjectContainer
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

public fun KotlinMultiplatformExtension.multiplatformDependencies(
  name: String,
  handler: KotlinDependencyHandler.() -> Unit,
) {
  multiplatformDependencies(
    sourceSet = { getByName(name) },
    handler = handler,
  )
}

public fun KotlinMultiplatformExtension.multiplatformDependencies(
  sourceSet: NamedDomainObjectContainer<KotlinSourceSet>.() -> KotlinSourceSet,
  handler: KotlinDependencyHandler.() -> Unit,
) {
  sourceSets.apply {
    sourceSet().apply {
      dependencies(handler)
    }
  }
}
