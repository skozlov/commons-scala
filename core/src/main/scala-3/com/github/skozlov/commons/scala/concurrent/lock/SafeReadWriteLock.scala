package com.github.skozlov.commons.scala.concurrent.lock

import com.github.skozlov.commons.scala.time.Deadline

import java.util.concurrent.TimeoutException
import java.util.concurrent.locks.ReadWriteLock
import scala.concurrent.duration.Duration

case class SafeReadWriteLock(unsafe: ReadWriteLock) {
  private val readLock = SafeLock(unsafe.readLock())
  private val writeLock = SafeLock(unsafe.writeLock())
  
  @throws[InterruptedException]
  @throws[TimeoutException]
  def readLocking[R](timeout: Duration, f: () => R): R = readLock.locking(timeout, f)

  @throws[InterruptedException]
  @throws[TimeoutException]
  def writeLocking[R](timeout: Duration, f: () => R): R = writeLock.locking(timeout, f)

  @throws[InterruptedException]
  @throws[TimeoutException]
  def readLocking[R](f: () => R)(implicit deadline: Deadline): R = readLock.locking(f)(deadline)

  @throws[InterruptedException]
  @throws[TimeoutException]
  def writeLocking[R](f: () => R)(implicit deadline: Deadline): R = writeLock.locking(f)(deadline)

  def newCondition(): SafeCondition = writeLock.newCondition()
}
