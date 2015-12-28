package com.github.skozlov.commons.scala.random

object Random {
	def elementFrom[A](sequence: Seq[A]): A = sequence(util.Random.nextInt(sequence.size))
}
