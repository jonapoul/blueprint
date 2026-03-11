package blueprint.detekt

import blueprint.detekt.rules.AvoidAfterEvaluate
import blueprint.detekt.rules.AvoidCrossProjectConfiguration
import blueprint.detekt.rules.AvoidExtraProperties
import blueprint.detekt.rules.AvoidProjectEquality
import blueprint.detekt.rules.AvoidRootProjectAccess
import blueprint.detekt.rules.AvoidStringTaskReferences
import blueprint.detekt.rules.LazyCollectionOperators
import blueprint.detekt.rules.PreferGradlePropertyProvider
import blueprint.detekt.rules.PreferNamedOverGet
import blueprint.detekt.rules.PreferRegisterOverCreate
import blueprint.detekt.rules.UsePluginManager
import dev.detekt.api.RuleSet
import dev.detekt.api.RuleSetId
import dev.detekt.api.RuleSetProvider

public class BlueprintRuleSetProvider : RuleSetProvider {
  override val ruleSetId: RuleSetId = RuleSetId("gradle")

  override fun instance(): RuleSet =
    RuleSet(
      id = ruleSetId,
      rules =
        listOf(
          ::AvoidAfterEvaluate,
          ::AvoidExtraProperties,
          ::AvoidCrossProjectConfiguration,
          ::AvoidProjectEquality,
          ::AvoidRootProjectAccess,
          ::AvoidStringTaskReferences,
          ::LazyCollectionOperators,
          ::PreferGradlePropertyProvider,
          ::PreferNamedOverGet,
          ::PreferRegisterOverCreate,
          ::UsePluginManager,
        ),
    )
}
