package io.scalac.tezos.translator.micheline

import scala.sys.process._
import scala.util.{Failure, Success, Try}

object MichelineTranslator {

  val NODE_PATH = "node"
  val MICHELSON_LEXER_PATH = "src/main/scala/io/scalac/tezos/translator/micheline/Michelson.js"

  def michelsonToMicheline(input: String): String = {
    var errorLog: String = ""
    val logger = ProcessLogger(e =>
      errorLog += e + '\n'
    )
    Try {
      Seq(NODE_PATH, MICHELSON_LEXER_PATH, input).!!(logger)
    } match {
      case Failure(_) => errorLog
      case Success(value) => value
    }
  }

}
