package blueprint.core

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import kotlin.jvm.optionals.getOrNull

public val Project.libs: VersionCatalog
  get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

public fun VersionCatalog.getLibrary(alias: String): Provider<MinimalExternalModuleDependency> =
  findLibraryOrNull(alias)
    ?: throw NoSuchElementException("No library found with ID '${alias}' in libs.versions.toml")

public fun VersionCatalog.getVersion(alias: String): VersionConstraint =
  findVersion(alias).getOrNull()
    ?: throw NoSuchElementException("No version found with ID '${alias}' in libs.versions.toml")

public fun VersionCatalog.findLibraryOrNull(id: String): Provider<MinimalExternalModuleDependency>? =
  findLibrary(id).getOrNull()
