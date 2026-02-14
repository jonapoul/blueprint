import dev.detekt.gradle.report.ReportMergeTask

plugins {
  alias(libs.plugins.buildConfig) apply false
  alias(libs.plugins.detekt) apply false
  alias(libs.plugins.dokka) apply false
  alias(libs.plugins.kotlin) apply false
  alias(libs.plugins.publish) apply false

  base
  alias(libs.plugins.dependencyAnalysis)
  alias(libs.plugins.dependencyGuard)
  alias(libs.plugins.publishReport)
}

dependencyGuard { configuration("classpath") }

dependencyAnalysis { issues { all { onAny { severity("fail") } } } }

val detektReportMergeSarif by
  tasks.registering(ReportMergeTask::class) {
    output = layout.buildDirectory.file("reports/detekt/merge.sarif.json")
  }

tasks.named("check").configure { dependsOn(detektReportMergeSarif) }
