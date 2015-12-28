package com.github.skozlov.commons.scala.collections

import com.github.skozlov.commons.scala.collections.Matrix.Coordinates
import org.scalatest.{Matchers, FlatSpec}

class MatrixBuilderTest extends FlatSpec with Matchers{
	"MatrixBuilder" should "return cell value by coordinates" in {
		val builder = new MatrixBuilder[Int](rowsCount = 3, columnsCount = 2)
		val coordinates = Coordinates(row = 2, column = 1)
		val value = 5
		builder(coordinates) = value
		builder(coordinates) shouldBe value
	}

	it should "return matrix" in {
		val builder = new MatrixBuilder[Int](rowsCount = 2, columnsCount = 2)
		builder(Coordinates(0, 0)) = 1
		builder(Coordinates(0, 1)) = 2
		builder(Coordinates(1, 0)) = 3
		builder(Coordinates(1, 1)) = 4
		builder.toMatrix shouldBe Matrix[Int](Array(Array(1, 2), Array(3, 4)))
	}
}
