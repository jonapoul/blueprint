package blueprint.core

import org.gradle.api.NamedDomainObjectContainer

public fun <T> NamedDomainObjectContainer<T>.getOrCreate(
  name: String,
): T = findByName(name) ?: create(name)

public fun <T> NamedDomainObjectContainer<T>.getOrCreate(
  name: String,
  action: T.() -> Unit,
): T = getOrCreate(name).apply(action)
