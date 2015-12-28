package com.github.skozlov.commons.scala.reactivex

import rx.lang.scala.Observable
import rx.lang.scala.subjects.BehaviorSubject

/**
  * Instances of this class are reactive properties.
  * They can be read, modified, interpreted as {@link rx.lang.scala.Observable}.
  * @param initValue the initial value
  * @tparam T the type of values represented by the property
  */
class Property[T](initValue: T){
	private var _value = initValue
	private val subject = BehaviorSubject(initValue)

	/**
	  * @return The current value of the property. If the property has never been modified, it's the initial value.
	  */
	def value: T = _value

	/**
	  * Assigns a value to the property
	  * @param newValue the value to be assigned to the property
	  */
	def value_=(newValue: T): Unit ={
		if(_value != newValue){
			_value = newValue
			subject.onNext(newValue)
		}
	}
}

object Property{
	/**
	  * Creates a property with an initial value
	  * @param initValue the initial value
	  * @tparam T the type of values represented by the property
	  */
	def apply[T](initValue: T): Property[T] = new Property[T](initValue)

	/**
	  * Converts a property to an observable.
	  * The observable emits the initial value and then emits the value every time when it changes.
	  */
	implicit def property2Observable[T](property: Property[T]): Observable[T] = property.subject
}