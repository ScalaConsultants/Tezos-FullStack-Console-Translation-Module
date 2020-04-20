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

## JS build for JsonToMichelson

Core module is cross-compiled with scala.js which allows us to publish JsonToMichelson translator as a javascript library.

Library API is very simple and defined in `JSApi.scala`. The only object it exports at the moment is `Translator`, which has a `translate` method that accepts a String with json input and returns conversion result as a raw js object:

```
def translate(json: String): js.Object
```

You can see example usage in `core/js/test.html`.

### NPM package

There are two ways you can get this library packaged: NPM package and a browser-compatible js file.

To get an NPM package, run this command in sbt shell ```buildJS```. This will compile the code, emit it as JS and bundle into an NPM package under `core/js/target/build`.

You can then test the package locally by running following commands in the root project folder:

```
npm install
node
require("scalac-tezos-contract-translator").Translator.translate("{}")
```

### Browser Library

If you run ```buildJS```, browser library will also be generated. You can find it at `core/js/target/scala-2.12/scalajs-bundler/main/core-opt-bundle.js`.

There's an html page you can open to test it: `core/js/test.html`. It uses the library file referenced above, so make sure to run `buildJS` beforehand.

### How the build works

See the [sbt-crossproject](https://github.com/portable-scala/sbt-crossproject) plugin for details on how the project is structured. In short:

* most of the code is in the `shared` folder, which means it's compiled to both JVM and JS targets;
* there's some platform specific code, which is placed in respective folders (`jvm` or `js`);
* there are actually two sbt projects for core: `coreJS` and `coreJVM`. You can reference either of them if you want to do something with specific platform target.
* `JSApi.scala` defines public interface for our javascript library. No other classes/values will be available;
* tests are also cross-compiled, so we test code on both platforms. when `coreJS/test` is launched, node.js runs all the tests.
* `buildJS` is actually a custom task that is defined in `build.sbt`. It just copies files, produced by the main scalajs-budler task. It can be called like this: `coreJS/fullOptJS::webpack`.
* There's no automation for NPM package publishing, and it's not yet published at all. You can controll additional package.json parameters using `additionalNpmConfig in Compile` setting.

## References

Conseil repository:
https://github.com/Cryptonomic/Conseil

ConseilJS repository:
https://github.com/Cryptonomic/ConseilJS
