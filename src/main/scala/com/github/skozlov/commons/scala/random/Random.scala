package com.github.skozlov.commons.scala.random

/**
  * Utilities to generate random values
  */
object Random {
	/**
	  * Returns a random element of the given sequence. Each element has probability of 1/${sequence.size}.
	  */
	def elementFrom[A](sequence: Seq[A]): A = sequence(util.Random.nextInt(sequence.size))
}
