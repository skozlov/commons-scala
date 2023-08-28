package com.github.skozlov.commons.scala.concurrent.lock

import com.github.skozlov.commons.scala.time.Deadline

import java.util.concurrent.locks.Condition

case class SafeCondition(unsafe: Condition) {
  def await(exitCondition: () => Boolean)(implicit deadline: Deadline): Unit = unsafe.await(exitCondition)(deadline)

  def signalAll(): Unit = unsafe.signalAll()
}
