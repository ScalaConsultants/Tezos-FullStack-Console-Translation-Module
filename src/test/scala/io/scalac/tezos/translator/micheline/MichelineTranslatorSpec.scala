package io.scalac.tezos.translator.micheline

import org.scalatest.{FlatSpec, Matchers}

class MichelineTranslatorSpec extends FlatSpec with Matchers {
  import MichelineTranslatorSpecHelpers._

  "Translator" should "translate Michelson to Micheline" in {

    val micheline =
      """
        |[ { "prim": "parameter", "args": [ { "prim": "int" } ] },
        |  { "prim": "storage", "args": [ { "prim": "int" } ] },
        |  { "prim": "code",
        |    "args":
        |    [ [ { "prim": "CAR" },
        |        { "prim": "PUSH",
        |          "args": [ { "prim": "int" }, { "int": "1" } ] },
        |        { "prim": "ADD" },
        |        { "prim": "NIL", "args": [ { "prim": "operation" } ] },
        |        { "prim": "PAIR" } ] ] } ]
        |""".stripMargin

    val michelson =
      """
        |parameter int;
        |        storage int;
        |        code {CAR;                      # Get the parameter
        |              PUSH int 1;               # We're adding 1, so we need to put 1 on the stack
        |              ADD;                      # Add the two numbers
        |              NIL operation;            # We put an empty list of operations on the stack
        |              PAIR}                     # Create the end value
        |""".stripMargin

    MichelineTranslator.michelsonToMicheline(michelson).map(_.noSpaces) shouldEqual Right(micheline.noSpaces)
  }

}

object MichelineTranslatorSpecHelpers {

  implicit class StringWithNoSpaces(str: String) {
    def noSpaces: String = str.filterNot((c: Char) => c.isWhitespace)
  }

}
