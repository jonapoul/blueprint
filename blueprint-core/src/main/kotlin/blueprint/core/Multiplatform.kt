package blueprint.core

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension as KMPExtension

public fun KMPExtension.multiplatformDependencies(
  name: String,
  handler: KotlinDependencyHandler.() -> Unit,
): Unit = sourceSets {
  named(name) {
    dependencies(handler)
  }
}

public fun KMPExtension.commonMainDependencies(handler: KotlinDependencyHandler.() -> Unit): Unit =
  multiplatformDependencies(name = "commonMain", handler)

public fun KMPExtension.commonTestDependencies(handler: KotlinDependencyHandler.() -> Unit): Unit =
  multiplatformDependencies(name = "commonTest", handler)

public fun KMPExtension.jvmMainDependencies(handler: KotlinDependencyHandler.() -> Unit): Unit =
  multiplatformDependencies(name = "jvmMain", handler)

public fun KMPExtension.jvmTestDependencies(handler: KotlinDependencyHandler.() -> Unit): Unit =
  multiplatformDependencies(name = "jvmTest", handler)

public fun KMPExtension.androidMainDependencies(handler: KotlinDependencyHandler.() -> Unit): Unit =
  multiplatformDependencies(name = "androidMain", handler)

public fun KMPExtension.androidUnitTestDependencies(handler: KotlinDependencyHandler.() -> Unit): Unit =
  multiplatformDependencies(name = "androidUnitTest", handler)

private fun KMPExtension.sourceSets(configure: Action<NamedDomainObjectContainer<KotlinSourceSet>>) =
  (this as ExtensionAware).extensions.configure("sourceSets", configure)
