package blueprint.core

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency
import kotlin.jvm.optionals.getOrNull

public val Project.libs: VersionCatalog
  get() = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

public operator fun VersionCatalog.get(alias: String): Provider<MinimalExternalModuleDependency> =
  requireNotNull(findLibrary(alias).getOrNull()) {
    "Library alias '$alias' not found in version catalog"
  }

public fun VersionCatalog.plugin(alias: String): Provider<PluginDependency> =
  requireNotNull(findPlugin(alias).getOrNull()) {
    "Plugin alias '$alias' not found in version catalog"
  }

public fun VersionCatalog.version(alias: String): VersionConstraint =
  requireNotNull(findVersion(alias).getOrNull()) {
    "Version alias '$alias' not found in version catalog"
  }
