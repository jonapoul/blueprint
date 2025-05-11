package blueprint.core

private val isGradleSync: Boolean
  get() = System.getProperty("idea.sync.active") == "true"
