package com.github.skozlov.commons.scala.reactivex

import org.scalatest.{Matchers, FlatSpec}
import rx.lang.scala.Observable

import scala.collection.mutable.ListBuffer

class PropertyTest extends FlatSpec with Matchers{
	"Property" should "return current value" in {
		Property(5).value shouldBe 5
	}

	"Property" should "be mutable" in {
		val property = Property(5)
		property.value = 6
		property.value shouldBe 6
	}

	"Property" should "notify about changes" in {
		val property = Property(1)
		val stream: Observable[Int] = property
		val events = new ListBuffer[Int]
		stream.foreach{events append _}
		property.value = 2
		property.value = 3
		events.toList shouldBe List(1, 2, 3)
	}

	"Property" should "not notify about setting current value" in {
		val property = Property(1)
		val stream: Observable[Int] = property
		val events = new ListBuffer[Int]
		stream.foreach{events append _}
		property.value = 1
		property.value = 2
		property.value = 2
		events.toList shouldBe List(1, 2)
	}
}
