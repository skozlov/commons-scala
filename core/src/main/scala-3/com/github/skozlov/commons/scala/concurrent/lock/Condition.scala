package com.github.skozlov.commons.scala.concurrent.lock

import com.github.skozlov.commons.scala.time.Deadline

import java.util.concurrent.TimeoutException
import java.util.concurrent.locks.Condition
import scala.annotation.tailrec

extension (condition: Condition) {
  def awaitUninterruptibly(exitCondition: () => Boolean): Unit = {
    while (!exitCondition()) {
      condition.awaitUninterruptibly()
    }
  }

  @throws[InterruptedException]
  @throws[TimeoutException]
  @tailrec
  def await(exitCondition: () => Boolean)(implicit deadline: Deadline): Unit = {
    if (!exitCondition()) {
      if deadline.isOver then throw new TimeoutException(s"Condition was never true before $deadline")
      if (deadline == Deadline.Inf) {
        condition.await()
      } else {
        condition.awaitNanos(deadline.toTimeout.toNanos)
      }
      await(exitCondition)(deadline)
    }
  }
}
