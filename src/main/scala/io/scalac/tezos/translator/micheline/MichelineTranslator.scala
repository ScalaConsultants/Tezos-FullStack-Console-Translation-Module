package io.scalac.tezos.translator.micheline

import scala.sys.process._
import scala.util.{Failure, Success, Try}

object MichelineTranslator {

  val NODE_PATH = "node"
  val MICHELSON_LEXER_PATH = "src/main/scala/io/scalac/tezos/translator/micheline/MichelsonTranslator.js"

  def michelsonToMicheline(input: String): Either[String, String] = {
    var errorLog: String = ""
    val logger = ProcessLogger(e =>
      errorLog += e + '\n'
    )
    Try {
      Seq(NODE_PATH, MICHELSON_LEXER_PATH, input).!!(logger)
    } match {
      case Failure(_) => Left(errorLog)
      case Success(value) => Right(value)
    }
  }

}
