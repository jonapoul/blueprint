import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.report.ReportMergeTask

plugins {
  alias(libs.plugins.buildConfig) apply false
  alias(libs.plugins.dokka) apply false
  alias(libs.plugins.kotlin) apply false
  alias(libs.plugins.kotlinAbi) apply false
  alias(libs.plugins.publish) apply false

  alias(libs.plugins.dependencyAnalysis)
  alias(libs.plugins.dependencyGuard)
  alias(libs.plugins.detekt)
  alias(libs.plugins.publishReport)
}

dependencyGuard {
  configuration("classpath")
}

dependencyAnalysis {
  issues {
    all {
      onAny { severity("fail") }
    }
  }
}

val detektReportMergeSarif by tasks.registering(ReportMergeTask::class) {
  output = layout.buildDirectory.file("reports/detekt/merge.sarif.json")
}

tasks.named("check").configure { dependsOn(detektReportMergeSarif) }

allprojects {
  detektReportMergeSarif.configure {
    input.from(tasks.withType<Detekt>().map { it.reports.sarif.outputLocation })
  }
}
