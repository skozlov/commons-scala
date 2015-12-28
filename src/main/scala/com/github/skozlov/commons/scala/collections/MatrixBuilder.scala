package com.github.skozlov.commons.scala.collections

import com.github.skozlov.commons.scala.collections.Matrix.Coordinates

class MatrixBuilder[C: Manifest](rowsCount: Int, columnsCount: Int){
	require(rowsCount > 0)
	require(columnsCount > 0)

	private val rows: Array[Array[C]] = Array.fill(rowsCount)(new Array[C](columnsCount))

	def apply(coordinates: Coordinates): C = {
		require(containsCellWithCoordinates(coordinates))
		rows(coordinates.row)(coordinates.column)
	}

	def update(coordinates: Coordinates, value: C): Unit ={
		require(containsCellWithCoordinates(coordinates))
		rows(coordinates.row)(coordinates.column) = value
	}

	private def containsCellWithCoordinates(coordinates: Coordinates): Boolean = {
		coordinates.row < rowsCount && coordinates.column < columnsCount
	}

	def toMatrix: Matrix[C] = new Matrix[C](rows)
}