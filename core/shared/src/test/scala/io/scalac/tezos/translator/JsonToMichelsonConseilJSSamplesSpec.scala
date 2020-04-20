package io.scalac.tezos.translator

import io.scalac.common.PlatformSpecificFiles
import io.scalac.tezos.translator.michelson.JsonToMichelson
import io.scalac.tezos.translator.michelson.dto.MichelsonSchema
import org.scalatest.{ FlatSpec, Matchers }

class JsonToMichelsonConseilJSSamplesSpec extends FlatSpec with Matchers with PlatformSpecificFiles {

  "JsonToMichelson" should "translate all samples from Micheline to Michelson" in {

    val samplesDir = "./core/shared/src/test/samples"

    val testFiles: Vector[String] = recursiveFiles(samplesDir).filter(_.endsWith(".micheline"))

    if (testFiles.isEmpty) fail("No test samples were loaded")

    testFiles.foreach { f: String =>
      println(s"Running test sample: $f")
      JsonToMichelson.convert[MichelsonSchema](loadFileContent(f)).isRight shouldBe true
    }
  }

}
