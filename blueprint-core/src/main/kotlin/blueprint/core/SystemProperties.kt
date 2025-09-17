package blueprint.core

public val isIntellijSyncing: Boolean
  get() = System.getProperty("idea.sync.active") == "true"
