package blueprint.core

import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.NamedDomainObjectProvider
import kotlin.reflect.KProperty

public operator fun <T : Any> NamedDomainObjectCollection<T>.provideDelegate(
  thisRef: Any?,
  property: KProperty<*>,
): NamedDomainObjectProvider<T> = named(property.name)

public inline operator fun <T : Any, reified U : T> NamedDomainObjectProvider<out T>.getValue(
  thisRef: Any?,
  property: KProperty<*>,
): U = get().let {
  it as? U
    ?: throw IllegalArgumentException("Expected ${U::class} for ${property.name}, but got ${it::class}")
}
