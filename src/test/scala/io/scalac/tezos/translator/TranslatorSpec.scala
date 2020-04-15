package io.scalac.tezos.translator

import io.scalac.tezos.translator.micheline.MichelineTranslator
import io.scalac.tezos.translator.micheline.MichelineTranslatorSpecHelpers._
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class TranslatorSpec
    extends FlatSpec
    with TableDrivenPropertyChecks
    with Matchers {

  "Translator" should "translate from Michelson to Micheline" ignore {
    forAll(Samples.translations) { (michelson: String, micheline: String) =>
      val translation = MichelineTranslator.michelsonToMicheline(michelson)
      translation.map(_.noSpaces) shouldEqual Right(micheline.noSpaces)
    }
  }

}
