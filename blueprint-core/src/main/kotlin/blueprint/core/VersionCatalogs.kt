package blueprint.core

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

public val Project.libs: VersionCatalog
  get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

public fun VersionCatalog.getLibrary(alias: String): Provider<MinimalExternalModuleDependency> {
  return findLibrary(alias).get()
}

public fun VersionCatalog.getVersion(alias: String): VersionConstraint {
  return findVersion(alias).get()
}
