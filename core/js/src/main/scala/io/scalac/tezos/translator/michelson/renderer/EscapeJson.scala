package io.scalac.tezos.translator.michelson.renderer

import scala.scalajs.js.JSON

trait EscapeJson {
  def escapeJsonString(s: String): String =
    JSON.stringify(s)
}
