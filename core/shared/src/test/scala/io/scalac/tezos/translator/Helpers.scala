package io.scalac.tezos.translator

object Helpers {

  implicit class StringWithNoSpaces(str: String) {
    def noSpaces: String = str.filterNot((c: Char) => c.isWhitespace)
  }

}
