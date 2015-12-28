package com.github.skozlov.commons.scala.collections

import com.github.skozlov.commons.scala.collections.Matrix.Coordinates

/**
  * A mutable matrix basically used to build an immutable one.
  * @param rowsCount a number of rows
  * @param columnsCount a number of columns
  * @tparam C a type of the values stored in the matrix
  */
class MatrixBuilder[C: Manifest](rowsCount: Int, columnsCount: Int){
	require(rowsCount > 0)
	require(columnsCount > 0)

	private val rows: Array[Array[C]] = Array.fill(rowsCount)(new Array[C](columnsCount))

	/**
	  * @return a value stored in a cell with the provided coordinates
	  */
	def apply(coordinates: Coordinates): C = {
		require(containsCellWithCoordinates(coordinates))
		rows(coordinates.row)(coordinates.column)
	}

	/**
	  * Stores `value` in the cell with the provided `coordinates`.
	  */
	def update(coordinates: Coordinates, value: C): Unit ={
		require(containsCellWithCoordinates(coordinates))
		rows(coordinates.row)(coordinates.column) = value
	}

	private def containsCellWithCoordinates(coordinates: Coordinates): Boolean = {
		coordinates.row < rowsCount && coordinates.column < columnsCount
	}

	/**
	  * @return an immutable matrix containing the same number of rows and columns and the same values as the builder
	  */
	def toMatrix: Matrix[C] = new Matrix[C](rows)
}