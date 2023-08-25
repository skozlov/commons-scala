package com.github.skozlov.commons.scala.concurrent.lock

import com.github.skozlov.commons.scala.time.Deadline

import java.util.concurrent.TimeoutException
import java.util.concurrent.locks.ReentrantLock
import scala.concurrent.duration.Duration

case class SafeLock(unsafe: ReentrantLock) {
  @throws[InterruptedException]
  @throws[TimeoutException]
  def locking[R](timeout: Duration, f: () => R): R = unsafe.locking(timeout, f)

  @throws[InterruptedException]
  @throws[TimeoutException]
  def locking[R](f: () => R)(implicit deadline: Deadline): R = unsafe.locking(f)(deadline)
}
