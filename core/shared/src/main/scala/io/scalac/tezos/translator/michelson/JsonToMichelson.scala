package io.scalac.tezos.translator.michelson

import io.scalac.tezos.translator.michelson.dto.MichelsonElement
import io.scalac.tezos.translator.michelson.parser.JsonParser
import io.scalac.tezos.translator.michelson.parser.JsonParser.Parser
import io.scalac.tezos.translator.michelson.renderer.MichelsonRenderer._

/* Converts Michelson schema from JSON to its native format */
object JsonToMichelson {

  type Result[T] = Either[Throwable, T]

  def convert[T <: MichelsonElement: Parser](json: String): Result[String] =
    JsonParser.parse[T](json).map(_.render())
}
