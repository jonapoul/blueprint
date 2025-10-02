plugins {
  alias(libs.plugins.dependencyAnalysis)
  alias(libs.plugins.dokka) apply false
  alias(libs.plugins.kotlin) apply false
  alias(libs.plugins.publish) apply false
  alias(libs.plugins.publishReport)
}

dependencyAnalysis {
  useTypesafeProjectAccessors(true)

  usage {
    analysis {
      checkSuperClasses(true)
    }
  }

  structure {
    ignoreKtx(ignore = true)
    bundle(name = "kotlin") { includeGroup("org.jetbrains.kotlin") }
    bundle(name = "android") { includeGroup("com.android.tools.build") }
    bundle(name = "spotless") { includeGroup("com.diffplug.spotless") }
  }

  reporting {
    printBuildHealth(true)
    onlyOnFailure(true)
  }

  abi {
    exclusions {
      ignoreInternalPackages()
      ignoreGeneratedCode()
    }
  }

  issues {
    all {
      onAny { severity(value = "fail") }
      onRuntimeOnly { severity(value = "ignore") }
    }
  }
}
