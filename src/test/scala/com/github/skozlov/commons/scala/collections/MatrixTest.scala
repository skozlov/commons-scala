package com.github.skozlov.commons.scala.collections

import com.github.skozlov.commons.scala.collections.Matrix.Coordinates
import org.scalatest.{Matchers, FlatSpec}

class MatrixTest extends FlatSpec with Matchers{
	private val matrix = Matrix[Int](Array(Array(1, 2), Array(3, 4), Array(5, 6)))

	"Matrix" should "return rows count" in {
		matrix.rowsCount shouldBe 3
	}

	"Matrix" should "return columns count" in {
		matrix.columnsCount shouldBe 2
	}

	"Matrix" should "contain inner cell" in {
		matrix.containsCellWithCoordinates(Coordinates(0, 0)) shouldBe true
	}

	"Matrix" should "contain border cell" in {
		matrix.containsCellWithCoordinates(Coordinates(row = 2, column = 1)) shouldBe true
	}

	"Matrix" should "not contain overflowing row" in {
		matrix.containsCellWithCoordinates(Coordinates(row = 3, column = 1)) shouldBe false
	}

	"Matrix" should "not contain overflowing column" in {
		matrix.containsCellWithCoordinates(Coordinates(row = 2, column = 2)) shouldBe false
	}

	"Matrix" should "return cell value by coordinates" in {
		matrix(Coordinates(0, 0)) shouldBe 1
	}

	"Matrix" should "be equal to the other one with the same content" in {
		matrix shouldBe new Matrix[Int](Array(Array(1, 2), Array(3, 4), Array(5, 6)))
	}

	"Matrix" should "be not equal to the other one with different content" in {
		matrix should not be Matrix[Int](Array(Array(0)))
	}

	"Matrix" should "return the same hash code for equal instances" in {
		matrix.hashCode shouldBe new Matrix[Int](Array(Array(1, 2), Array(3, 4), Array(5, 6))).hashCode
	}
}
