# Tezos FullStack Console Translation Module
Library for the Tezos Michelson/Micheline translator based on Conseil and ConseilJS by Cryptonomic.

## Running Translation Module

### Prerequisites

- JDK (> 8.x)
- Scala (> 2.12.8)
- SBT (> 1.2.8)
- Node.js with nearly

### Sbt

To include Translation Module into your project paste those lines into your `build.sbt` file:
```
resolvers += "Scalac" at "https://raw.githubusercontent.com/ScalaConsultants/mvn-repo/master/"

libraryDependencies += "io.scalac" %% "tezos-fullstack-console-translation-module" % "0.1"
```

## Translating Michelson to JSON

To use translator in code import:
```
import io.scalac.tezos.translator.micheline.MichelineTranslator
```

And then call `MichelineTranslator.michelsonToMicheline(michelsonString)`.

Mind it can fail and the result type is `Either[String, String]` where the
`Left` side contains the exception message thrown by JavaScript and
`Right` contains correctly parsed `String`.

For example:
```
val michelsonString ="""
         |parameter int;
         |storage int;
         |code {
         |	CAR;
         |	PUSH int 1;
         |	ADD;
         |	NIL operation;
         |	PAIR
         |}
       """.stripMargin

val micheline = MichelineTranslator.michelsonToMicheline(michelsonString).getOrElse("")

println(micheline)
```

Will print out:
```
[ { "prim": "parameter", "args": [ { "prim": "int" } ] }, { "prim": "storage", "args": [ { "prim": "int" } ] }, { "prim": "code", "args": [ [ { "prim": "CAR" },{ "prim": "PUSH", "args": [ { "prim": "int" }, { "int": "1" } ] },{ "prim": "ADD" },{ "prim": "NIL", "args": [ { "prim": "operation" } ] },{ "prim": "PAIR" } ] ] } ]
```

## Translating JSON to Michelson

To use translator in code import:
```
import io.scalac.tezos.translator.michelson.JsonToMichelson
import io.scalac.tezos.translator.michelson.dto.MichelsonSchema
```

And then call `JsonToMichelson.convert[MichelsonSchema](jsonString).getOrElse("")`

Mind it can fail and the result type is `Either[Throwable, String]` where the
`Left` side contains the thrown exception and
`Right` contains correctly parsed `String`.

For example:
```
val jsonString ="""
  |[
  |    {
  |      "prim": "parameter",
  |      "args": [
  |        {
  |          "prim": "int"
  |        }
  |      ]
  |    },
  |    {
  |      "prim": "storage",
  |      "args": [
  |        {
  |          "prim": "int"
  |        }
  |      ]
  |    },
  |    {
  |      "prim": "code",
  |      "args": [
  |        [
  |          {
  |            "prim": "CAR"
  |          },
  |          {
  |            "prim": "PUSH",
  |            "args": [
  |              {
  |                "prim": "int"
  |              },
  |              {
  |                "int": "1"
  |              }
  |            ]
  |          },
  |          {
  |            "prim": "ADD"
  |          },
  |          {
  |            "prim": "NIL",
  |            "args": [
  |              {
  |                "prim": "operation"
  |              }
  |            ]
  |          },
  |          {
  |            "prim": "PAIR"
  |          }
  |        ]
  |      ]
  |    }
  |]
""".stripMargin

val michelson = JsonToMichelson.convert[MichelsonSchema](jsonString).getOrElse("")

println(michelson)
```

Will print out:
```
parameter int;
storage int;
code { CAR ;
       PUSH int 1 ;
       ADD ;
       NIL operation ;
       PAIR }
```

###References
Conseil repository:
https://github.com/Cryptonomic/Conseil

ConseilJS repository:
https://github.com/Cryptonomic/ConseilJS