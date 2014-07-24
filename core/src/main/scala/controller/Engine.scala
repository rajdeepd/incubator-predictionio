package io.prediction.controller

import io.prediction.core.BaseDataSource
import io.prediction.core.BasePreparator
import io.prediction.core.BaseAlgorithm
import io.prediction.core.BaseServing

class Engine[TD, DP, PD, Q, P, A](
    val dataSourceClass: Class[_ <: BaseDataSource[_ <: Params, DP, TD, Q, A]],
    val preparatorClass: Class[_ <: BasePreparator[_ <: Params, TD, PD]],
    val algorithmClassMap: 
      Map[String, Class[_ <: BaseAlgorithm[_ <: Params, PD, _, Q, P]]],
    val servingClass: Class[_ <: BaseServing[_ <: Params, Q, P]]
  ) extends Serializable

class EngineParams(
    val dataSourceParams: Params = EmptyParams(),
    val preparatorParams: Params = EmptyParams(),
    val algorithmParamsList: Seq[(String, Params)] = Seq(),
    val servingParams: Params = EmptyParams()) extends Serializable

// SimpleEngine has only one algorithm, and uses default preparator and serving
// layer.
class SimpleEngine[TD, DP, Q, P, A](
    dataSourceClass: Class[_ <: BaseDataSource[_ <: Params, DP, TD, Q, A]],
    algorithmClass: Class[_ <: BaseAlgorithm[_ <: Params, TD, _, Q, P]])
  extends Engine(
      dataSourceClass,
      IdentityPreparator(dataSourceClass),
      Map("" -> algorithmClass),
      FirstServing(algorithmClass))

class SimpleEngineParams(
    dataSourceParams: Params = EmptyParams(),
    algorithmParams: Params = EmptyParams())
  extends EngineParams(
      dataSourceParams = dataSourceParams,
      algorithmParamsList = Seq(("", algorithmParams)))

trait IEngineFactory {
  def apply(): Engine[_, _, _, _, _, _]
}
