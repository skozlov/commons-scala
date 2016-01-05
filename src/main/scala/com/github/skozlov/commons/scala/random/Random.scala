package com.github.skozlov.commons.scala.random

/**
  * Utilities to generate random values
  */
object Random {
	/**
	  * Returns a random element of the given finite traversable.
	  * Each element has probability of 1/${traversable.size}.
	  */
	def elementFrom[A](traversable: Traversable[A]): A = {
		val index = util.Random.nextInt(traversable.size)
		(traversable drop index).head
	}
}
