package com.github.skozlov.commons.scala.collections

import java.lang.Math._

import scala.util.Random

import Matrix._

/**
  * A two-dimensional immutable collection with `n` rows and `m` columns (`n>0`, `m>0`) which contains `n*m` elements.
  * Each element can be accessed by {@link com.github.skozlov.commons.scala.collections.Matrix.Coordinates}.
  * @param rows an array of arrays treated as rows of the matrix. The matrix stores copies of the supplied arrays.
  * @tparam C a type of the values stored in the matrix
  */
class Matrix[+C: Manifest](rows: Array[Array[C]]) extends Equals{
	require(rows.nonEmpty)

	/**
	  * A number of rows in the matrix
	  */
	val rowsCount: Int = rows.length

	/**
	  * A number of columns in the matrix
	  */
	val columnsCount: Int = rows(0).length

	for(row <- rows){
		require(row.length == columnsCount)
	}

	private val _rows: Array[Array[_]] = rows map (_.clone())
	private lazy val flatten: IndexedSeq[_] = rows.deep

	/**
	  * @return `true` if the matrix contains a cell with the provided coordinates, `false` otherwise.
	  */
	def containsCellWithCoordinates(coordinates: Coordinates): Boolean = {
		coordinates.row < rowsCount && coordinates.column < columnsCount
	}

	/**
	  * @return a value stored in a cell with the provided coordinates
	  */
	def apply(coordinates: Coordinates): C = {
		require(containsCellWithCoordinates(coordinates))
		_rows(coordinates.row)(coordinates.column).asInstanceOf[C]
	}

	override lazy val hashCode: Int = flatten.hashCode()

	override def equals(obj: scala.Any): Boolean = {
		if(obj == null || !obj.isInstanceOf[Matrix[_]]) false
		else {
			val that = obj.asInstanceOf[Matrix[_]]
			(this eq that) || ((that canEqual this) && (this.flatten == that.flatten))
		}
	}

	override def canEqual(that: Any): Boolean = that.isInstanceOf[Matrix[_]]
}

object Matrix{
	/**
	  * @tparam C a type of the values stored in the matrix
	  * @return a matrix constructed from the supplied array of arrays, which is treated as rows of the matrix.
	  */
	def apply[C: Manifest](rows: Array[Array[C]]): Matrix[C] = new Matrix(rows)

	/**
	  * Coordinates of a matrix cell
	  * @param row an index of the row (beginning with `0`)
	  * @param column an index of the column (beginning with `0`)
	  */
	case class Coordinates(row: Int, column: Int){
		require(row >= 0)
		require(column >= 0)

		/**
		  * Coordinates of the north neighbor of the cell with the current coordinates.
		  * `None` if `row = 0`
		  */
		lazy val northNeighborCoordinates: Option[Coordinates] =
			if(row == 0) None else Some(Coordinates(row - 1, column))

		/**
		  * Coordinates of the south neighbor of the cell with the current coordinates.
		  * `None` if `row = rowsCount-1`
		  * @param rowsCount a number of rows in the matrix to find neighbor within
		  */
		def southNeighborCoordinates(rowsCount: Int): Option[Coordinates] = {
			require(row < rowsCount)
			if(row == rowsCount - 1) None else Some(Coordinates(row + 1, column))
		}

		/**
		  * Coordinates of the west neighbor of the cell with the current coordinates.
		  * `None` if `column = 0`
		  */
		lazy val westNeighborCoordinates: Option[Coordinates] =
			if(column == 0) None else Some(Coordinates(row, column - 1))

		/**
		  * Coordinates of the east neighbor of the cell with the current coordinates.
		  * `None` if `column = columnsCount-1`
		  * @param columnsCount a number of columns in the matrix to find neighbor within
		  */
		def eastNeighborCoordinates(columnsCount: Int): Option[Coordinates] = {
			require(column < columnsCount)
			if(column == columnsCount - 1) None else Some(Coordinates(row, column + 1))
		}

		/**
		  *
		  * @param other coordinates of the other cell
		  * @return distance from the cell with the current coordinates to the cell with the supplied coordinates, calculated by formula `r = |r1-r2| + |c1-c2|`, where `r1` and `r2` are indexes of the rows of the current and the other cells respectively, `c1` and `c2` are indexes of the columns of the current and the other cells respectively.
		  */
		def distanceTo(other: Coordinates): Int = abs(this.row - other.row) + abs(this.column - other.column)
	}

	object Coordinates{
		/**
		  * @return a random coordinates with `0 <= row < rowsCount` and `0 <= column < columnsCount`. Each value of `rowsCount*columnsCount` possible ones can be returned with probability of `1/(rowsCount*columnsCount)`.
		  */
		def random(rowsCount: Int, columnsCount: Int): Coordinates = {
			require(rowsCount > 0)
			require(columnsCount > 0)
			Coordinates(Random.nextInt(rowsCount), Random.nextInt(columnsCount))
		}
	}
}