package com.github.skozlov.commons.scala.collections

import java.lang.Math._

import scala.util.Random

import Matrix._

class Matrix[+C: Manifest](rows: Array[Array[C]]) extends Equals{
	require(rows.nonEmpty)

	val rowsCount: Int = rows.length

	val columnsCount: Int = rows(0).length

	for(row <- rows){
		require(row.length == columnsCount)
	}

	private val _rows: Array[Array[_]] = rows map (_.clone())
	private lazy val flatten: IndexedSeq[_] = rows.deep

	def containsCellWithCoordinates(coordinates: Coordinates): Boolean = {
		coordinates.row < rowsCount && coordinates.column < columnsCount
	}

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
	def apply[C: Manifest](rows: Array[Array[C]]): Matrix[C] = new Matrix(rows)

	case class Coordinates(row: Int, column: Int){
		require(row >= 0)
		require(column >= 0)

		lazy val northNeighborCoordinates: Option[Coordinates] =
			if(row == 0) None else Some(Coordinates(row - 1, column))

		def southNeighborCoordinates(rowsCount: Int): Option[Coordinates] = {
			require(row < rowsCount)
			if(row == rowsCount - 1) None else Some(Coordinates(row + 1, column))
		}

		lazy val westNeighborCoordinates: Option[Coordinates] =
			if(column == 0) None else Some(Coordinates(row, column - 1))

		def eastNeighborCoordinates(columnsCount: Int): Option[Coordinates] = {
			require(column < columnsCount)
			if(column == columnsCount - 1) None else Some(Coordinates(row, column + 1))
		}

		def distanceTo(other: Coordinates): Int = abs(this.row - other.row) + abs(this.column - other.column)
	}

	object Coordinates{
		def random(rowsCount: Int, columnsCount: Int): Coordinates = {
			require(rowsCount > 0)
			require(columnsCount > 0)
			Coordinates(Random.nextInt(rowsCount), Random.nextInt(columnsCount))
		}
	}
}