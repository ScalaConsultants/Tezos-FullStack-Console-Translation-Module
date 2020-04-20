package io.scalac.tezos.translator.michelson.renderer
import org.apache.commons.text.StringEscapeUtils

trait EscapeJson {
  def escapeJsonString(s: String): String = "\"%s\"".format(StringEscapeUtils.escapeJson(s))
}
