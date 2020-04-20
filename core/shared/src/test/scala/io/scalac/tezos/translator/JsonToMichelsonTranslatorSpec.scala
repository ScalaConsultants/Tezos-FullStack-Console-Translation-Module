package io.scalac.tezos.translator

import Helpers._
import io.scalac.tezos.translator.michelson.JsonToMichelson
import io.scalac.tezos.translator.michelson.dto.MichelsonSchema
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class JsonToMichelsonTranslatorSpec
    extends FlatSpec
    with TableDrivenPropertyChecks
    with Matchers {

  "JsonToMichelson" should "translate from Micheline to Michelson" in {
    forAll(Samples.translations) { (michelson: String, micheline: String) =>
      val translation = JsonToMichelson.convert[MichelsonSchema](micheline)
      translation.map(_.noSpaces) shouldEqual Right(michelson.noSpaces)
    }
  }

}
