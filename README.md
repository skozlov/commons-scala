# commons-scala
Some useful scala code to use in JVM-based projects

## Features

### Matrix

`Matrix` is a two-dimensional immutable collection with `n` rows and `m` columns (`n>0`, `m>0`) which contains `n*m` elements.
`MatrixBuilder` is a mutable matrix basically used to build an immutable one.

Example:

```scala
import com.github.skozlov.commons.scala.collections.{Matrix, Matrix.Coordinates, MatrixBuilder}

val builder = new MatrixBuilder[Int](rowsCount = 2, columnsCount = 2)
builder(Coordinates(0, 0)) = 1
builder(Coordinates(0, 1)) = 2
builder(Coordinates(1, 0)) = 3
builder(Coordinates(1, 1)) = 4

val matrix = builder.toMatrix
println(matrix.rowsCount) //2
println(matrix.columnsCount) //2
println(matrix.containsCellWithCoordinates(Coordinates(0, 0))) //true
println(matrix.containsCellWithCoordinates(Coordinates(2, 2))) //false
println(matrix(Coordinates(0, 0))) //1
```

### `Random.elementFrom(sequence: Seq[A])`

`Random.elementFrom(sequence: Seq[A])` returns a random element of the given sequence.
Each element has probability of 1/${sequence.size}.

Example:

```scala
import com.github.skozlov.commons.scala.random.Random._

val randomElement = elementFrom(List(1, 2, 3)) //1 or 2 or 3
```

### Reactive property

Reactive property is a mutable single-value container which can be treated as [Observable](http://reactivex.io/rxscala/scaladoc/#rx.lang.scala.Observable).

Example:

```scala
import com.github.skozlov.commons.scala.reactivex.Property

val property = Property(1)
property.foreach{println(_)} // Assign callback to the stream of changes of the property. Prints `1` immediately.
property.value = 1 // Prints nothing, because the value was not changed.
property.value = 2 // Prints `2`.
property.value = 1 // Prints `1`.
```
