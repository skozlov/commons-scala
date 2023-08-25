package com.github.skozlov.commons.scala.concurrent.lock

import com.github.skozlov.commons.scala.time.Deadline

import java.util.concurrent.{TimeUnit, TimeoutException}
import java.util.concurrent.TimeUnit.NANOSECONDS
import java.util.concurrent.locks.Lock
import scala.concurrent.duration.Duration

extension (lock: Lock) {
  @throws[InterruptedException]
  def tryLock(timeout: Duration): Boolean = {
    if (timeout <= Duration.Zero) {
      lock.tryLock()
    } else if (timeout.isFinite) {
      lock.tryLock(timeout.toNanos, NANOSECONDS)
    } else {
      lock.lockInterruptibly()
      true
    }
  }

  def locking[R](f: () => R): R = {
    lock.lock()
    try {
      f()
    } finally {
      lock.unlock()
    }
  }

  @throws[InterruptedException]
  @throws[TimeoutException]
  def locking[R](timeout: Duration, f: () => R): R = {
    if !lock.tryLock(timeout) then throw new TimeoutException(s"Count not lock within $timeout")
    try {
      f()
    } finally {
      lock.unlock()
    }
  }

  @throws[InterruptedException]
  @throws[TimeoutException]
  def locking[R](f: () => R)(implicit deadline: Deadline): R = locking(deadline.toTimeout, f)
}
