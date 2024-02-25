package blueprint.atak

public val isTakDevPipeline: Boolean
  get() = System.getenv("ATAK_CI")?.toIntOrNull() == 1
