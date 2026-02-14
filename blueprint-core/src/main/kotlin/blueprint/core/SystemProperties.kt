package blueprint.core

import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory

@Suppress("NonBooleanPropertyPrefixedWithIs")
public val ProviderFactory.isIntellijSyncing: Provider<Boolean>
  get() = systemProperty("idea.sync.active").map(String::toBoolean)
