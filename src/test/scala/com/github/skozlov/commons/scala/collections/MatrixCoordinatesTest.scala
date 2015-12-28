package com.github.skozlov.commons.scala.collections

import com.github.skozlov.commons.scala.collections.Matrix.Coordinates
import org.scalatest.{Matchers, FlatSpec}

class MatrixCoordinatesTest extends FlatSpec with Matchers{
	"Matrix.Coordinates" should "return north neighbor for row>0" in {
		Coordinates(row = 1, column = 2).northNeighborCoordinates shouldBe Some(Coordinates(row = 0, column = 2))
	}

	it should "return None as north neighbor for row=0" in {
		Coordinates(row = 0, column = 2).northNeighborCoordinates shouldBe None
	}

	it should "return south neighbor for row < rowsCount - 1" in {
		Coordinates(row = 0, column = 2).southNeighborCoordinates(rowsCount = 3) shouldBe
			Some(Coordinates(row = 1, column = 2))
	}

	it should "return None as south neighbor for row = rowsCount - 1" in {
		Coordinates(row = 0, column = 2).southNeighborCoordinates(rowsCount = 1) shouldBe None
	}

	it should "return west neighbor for column>0" in {
		Coordinates(row = 0, column = 2).westNeighborCoordinates shouldBe Some(Coordinates(row = 0, column = 1))
	}

	it should "return None as west neighbor for column=0" in {
		Coordinates(row = 1, column = 0).westNeighborCoordinates shouldBe None
	}

	it should "return east neighbor for column < columnsCount - 1" in {
		Coordinates(row = 1, column = 0).eastNeighborCoordinates(columnsCount = 2) shouldBe
			Some(Coordinates(row = 1, column = 1))
	}

	it should "return None as east neighbor for column = columnsCount - 1" in {
		Coordinates(row = 1, column = 0).eastNeighborCoordinates(columnsCount = 1) shouldBe None
	}

	it should "return 0 as distance to itself" in {
		Coordinates(1, 2).distanceTo(Coordinates(1, 2)) shouldBe 0
	}

	it should "return positive integer as distance to different cell" in {
		Coordinates(0, 0).distanceTo(Coordinates(2, 3)) shouldBe 5
		Coordinates(2, 3).distanceTo(Coordinates(0, 0)) shouldBe 5
		Coordinates(2, 0).distanceTo(Coordinates(0, 3)) shouldBe 5
	}
}
