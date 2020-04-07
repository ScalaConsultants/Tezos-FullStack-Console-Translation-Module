package io.scalac.tezos.translator

import io.scalac.tezos.translator.micheline.MichelineTranslator
import io.scalac.tezos.translator.micheline.MichelineTranslatorSpecHelpers._
import io.scalac.tezos.translator.michelson.JsonToMichelson
import io.scalac.tezos.translator.michelson.dto.MichelsonSchema
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers, PropSpec}

class TranslatorSpec
    extends FlatSpec
    with TableDrivenPropertyChecks
    with Matchers {

  val translations =
    Table(
      ("Michelson", "Micheline"),
      (
      """parameter (or (lambda %do unit (list operation)) (unit %default));
        |storage key_hash;
        |code { { { DUP ; CAR ; DIP { CDR } } } ;
        |       IF_LEFT { PUSH mutez 0 ;
        |                 AMOUNT ;
        |                 { { COMPARE ; EQ } ; IF {} { { UNIT ; FAILWITH } } } ;
        |                 { DIP { DUP } ; SWAP } ;
        |                 IMPLICIT_ACCOUNT ;
        |                 ADDRESS ;
        |                 SENDER ;
        |                 { { COMPARE ; EQ } ; IF {} { { UNIT ; FAILWITH } } } ;
        |                 UNIT ;
        |                 EXEC ;
        |                 PAIR }
        |               { DROP ;
        |                 NIL operation ;
        |                 PAIR } }""".stripMargin
      ,
      """{"code":[{"prim":"parameter","args":[{"prim":"or","args":[{"prim":"lambda","args":[{"prim":"unit"},{"prim":"list","args":[{"prim":"operation"}]}],"annots":["%do"]},{"prim":"unit","annots":["%default"]}]}]},{"prim":"storage","args":[{"prim":"key_hash"}]},{"prim":"code","args":[[[[{"prim":"DUP"},{"prim":"CAR"},{"prim":"DIP","args":[[{"prim":"CDR"}]]}]],{"prim":"IF_LEFT","args":[[{"prim":"PUSH","args":[{"prim":"mutez"},{"int":"0"}]},{"prim":"AMOUNT"},[[{"prim":"COMPARE"},{"prim":"EQ"}],{"prim":"IF","args":[[],[[{"prim":"UNIT"},{"prim":"FAILWITH"}]]]}],[{"prim":"DIP","args":[[{"prim":"DUP"}]]},{"prim":"SWAP"}],{"prim":"IMPLICIT_ACCOUNT"},{"prim":"ADDRESS"},{"prim":"SENDER"},[[{"prim":"COMPARE"},{"prim":"EQ"}],{"prim":"IF","args":[[],[[{"prim":"UNIT"},{"prim":"FAILWITH"}]]]}],{"prim":"UNIT"},{"prim":"EXEC"},{"prim":"PAIR"}],[{"prim":"DROP"},{"prim":"NIL","args":[{"prim":"operation"}]},{"prim":"PAIR"}]]}]]}]"""
      ),
      (
        """parameter key_hash; storage (pair key_hash timestamp); code {DROP ;NIL operation ; PUSH key_hash 0x00a937407f7e58665122c07d946783ed3face47ae7 ; IMPLICIT_ACCOUNT ; PUSH mutez 510000000 ; UNIT ; TRANSFER_TOKENS ; CONS}""",
        """[{"prim":"DROP"},{"prim":"NIL","args":[{"prim":"operation"}]},{"prim":"PUSH","args":[{"prim":"key_hash"},{"bytes":"00a937407f7e58665122c07d946783ed3face47ae7"}]},{"prim":"IMPLICIT_ACCOUNT"},{"prim":"PUSH","args":[{"prim":"mutez"},{"int":"510000000"}]},{"prim":"UNIT"},{"prim":"TRANSFER_TOKENS"},{"prim":"CONS"}]"""
      ),
      (
        "parameter key_hash; storage (pair key_hash timestamp);code { DROP ; NIL operation ; PUSH key_hash \"tz1dDSoB4Gep55bvYXFWNdtFFBuzPTYxDuKc\" ; IMPLICIT_ACCOUNT ; PUSH mutez 10155841 ; UNIT ; TRANSFER_TOKENS ; CONS }",
        """[{"prim":"DROP"},{"prim":"NIL","args":[{"prim":"operation"}]},{"prim":"PUSH","args":[{"prim":"key_hash"},{"string":"tz1dDSoB4Gep55bvYXFWNdtFFBuzPTYxDuKc"}]},{"prim":"IMPLICIT_ACCOUNT"},{"prim":"PUSH","args":[{"prim":"mutez"},{"int":"10155841"}]},{"prim":"UNIT"},{"prim":"TRANSFER_TOKENS"},{"prim":"CONS"}]"""
      )
    )

  "Translator" should "translate from Michelson to Micheline" in  {
    forAll(translations) { (michelson: String, micheline: String) =>
      val translation = MichelineTranslator.michelsonToMicheline(michelson)
      translation shouldBe 'right
      translation shouldEqual Right(micheline)

    }
  }

  "Translator" should "translate from Micheline to Michelson" in {
    forAll(translations) { (michelson: String, micheline: String) =>
      val translation = JsonToMichelson.convert[MichelsonSchema](micheline)
      translation shouldBe 'right
      translation shouldEqual Right(michelson)

    }
  }

}
