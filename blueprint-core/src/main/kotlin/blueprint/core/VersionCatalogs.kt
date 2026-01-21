package blueprint.core

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider

public val Project.libs: VersionCatalog
  get() = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

public operator fun VersionCatalog.invoke(alias: String): Provider<MinimalExternalModuleDependency> =
  findLibrary(alias).get()

public operator fun VersionCatalog.get(alias: String): Provider<MinimalExternalModuleDependency> =
  invoke(alias)

public fun VersionCatalog.version(alias: String): VersionConstraint = findVersion(alias).get()
