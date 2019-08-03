package io.scalac.tezos.translator.micheline

import org.scalatest.{FlatSpec, Matchers}

class MichelineTranslatorSpec extends FlatSpec with Matchers {

  "Translator" should "translate Michelson to Micheline" in {

    val a = """
      |{ "script":
      |      [ { "prim": "parameter", "args": [ { "prim": "int" } ] },
      |        { "prim": "storage", "args": [ { "prim": "int" } ] },
      |        { "prim": "code",
      |          "args":
      |            [ [ { "prim": "CAR" },
      |                { "prim": "PUSH",
      |                  "args": [ { "prim": "int" }, { "int": "1" } ] },
      |                { "prim": "ADD" },
      |                { "prim": "NIL", "args": [ { "prim": "operation" } ] },
      |                { "prim": "PAIR" } ] ] } ] }
      |""".stripMargin

    val b =
      """
        paramdddeter int;
        storage int;
        code {CAR;                      # Get the parameter
              PUSH int 1;               # We're adding 1, so we need to put 1 on the stack
              ADD;                      # Add the two numbers
              NIL operation;            # We put an empty list of operations on the stack
              PAIR}                     # Create the end value
        """

    MichelineTranslator.michelsonToMicheline(b) shouldEqual a
  }

}
