package io.scalac.tezos.translator.michelson

import io.scalac.tezos.translator.michelson.dto.MichelsonSchema

import scala.scalajs.js
import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }

@JSExportTopLevel("Translator")
object JSApi {
  @JSExport
  def translate(json: String): js.Object =
    JsonToMichelson.convert[MichelsonSchema](json) match {
      case Left(value) =>
        js.Dynamic.literal("status" -> "failure", "reason" -> value.getMessage)
      case Right(value) =>
        js.Dynamic.literal("status" -> "success", "result" -> value)
    }
}
