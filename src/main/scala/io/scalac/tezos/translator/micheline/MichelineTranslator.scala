package io.scalac.tezos.translator.micheline

import java.io.{ File, PrintWriter }

import scala.io.Source
import scala.sys.process._
import scala.util.{ Failure, Success, Try }

object MichelineTranslator {

  val NODE_PATH            = "node"
  val MICHELSON_LEXER_PATH = "src/main/scala/io/scalac/tezos/translator/micheline/MichelsonTranslator.js"

  lazy val michelsonSource = Source.fromResource("Michelson.js").mkString
  lazy val michelsonFile = {
    val file   = new File(".Michelson.js")
    val writer = new PrintWriter(file)
    writer.write(michelsonSource)
    writer.close()
    file.deleteOnExit()
    file
  }

  lazy val translatorSource = Source.fromResource("MichelsonTranslator.js").mkString
  lazy val translatorFile = {
    val file   = new File(".MichelsonTranslator.js")
    val writer = new PrintWriter(file)
    writer.write(translatorSource)
    writer.close()
    file.deleteOnExit()
    file
  }

  def michelsonToMicheline(input: String): Either[String, String] = {
    var errorLog: String = ""
    val logger = ProcessLogger(e =>
      if (!e.startsWith("    at ")) {
        errorLog += e + '\n'
      }
    )
    val michelsonSource  = michelsonFile.getAbsolutePath
    val translatorSource = translatorFile.getAbsolutePath
    Try {
      Seq(NODE_PATH, translatorSource, input).!!(logger)
    } match {
      case Failure(_)     => Left(errorLog)
      case Success(value) => Right(value)
    }
  }

}
