package io.scalac.tezos.translator.michelson.parser

object CirceCommonDecoders {
  import io.circe.Decoder

  /** Provides deconding to an Either value, without needing a discrimination "tag" in the json source */
  implicit def decodeUntaggedEither[A, B](
    implicit leftDecoder: Decoder[A],
    rightDecoder: Decoder[B]
  ): Decoder[Either[A, B]] =
    leftDecoder.map(Left.apply) or rightDecoder.map(Right.apply)
}
