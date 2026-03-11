package blueprint.core

import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency

public fun Provider<PluginDependency>.toDependency(): Provider<String> = map {
  "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}
