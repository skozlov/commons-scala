package com.github.skozlov.commons.scala.reactivex

import rx.lang.scala.Observable
import rx.lang.scala.subjects.BehaviorSubject

class Property[T](initValue: T){
	private var _value = initValue
	private val subject = BehaviorSubject(initValue)

	def value: T = _value

	def value_=(newValue: T): Unit ={
		if(_value != newValue){
			_value = newValue
			subject.onNext(newValue)
		}
	}
}

object Property{
	def apply[T](initValue: T): Property[T] = new Property[T](initValue)

	implicit def property2Observable[T](property: Property[T]): Observable[T] = property.subject
}