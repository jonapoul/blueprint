package blueprint.detekt

import dev.detekt.api.RuleSet
import dev.detekt.api.RuleSetId
import dev.detekt.api.RuleSetProvider

public class BlueprintRuleSetProvider : RuleSetProvider {
  override val ruleSetId: RuleSetId = RuleSetId("gradle")

  override fun instance(): RuleSet =
    error(
      "The 'gradle' detekt ruleset has moved out of blueprint. Replace the " +
        "dev.jonpoulton.blueprint:detekt-rules dependency with " +
        "dev.jonpoulton.detekt:gradle-detekt-rules - see " +
        "https://github.com/jonapoul/gradle-detekt-rules"
    )
}
