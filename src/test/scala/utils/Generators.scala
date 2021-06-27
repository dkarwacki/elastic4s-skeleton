package utils

import features.clients.domain.Client
import org.scalacheck.Gen
import org.scalacheck.rng.Seed

object Generators {
  val clientGen: Gen[Client] = for {
    id <- Gen.option(Gen.alphaStr)
    firstName <- Gen.alphaStr
    lastName <- Gen.alphaStr
    age <- Gen.posNum[Int]
    address <- Gen.alphaStr
  } yield Client(firstName, lastName, age, address, id)

  implicit class GenOpt[T](gen: Gen[T]) {
    def one(): T = gen.pureApply(Gen.Parameters.default, Seed.random(), 1000)
    def take(n: Int): List[T] = List.fill(n)(one())
  }
}
