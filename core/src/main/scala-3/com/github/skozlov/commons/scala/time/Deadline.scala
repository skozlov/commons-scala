package com.github.skozlov.commons.scala.time

import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit.NANOS
import java.time.{Clock, Instant}
import scala.concurrent.duration.Duration

enum Deadline extends Ordered[Deadline] {
  /**
   * Deadline at the given Instant.
   * Clock is used to get the current Instant when converting this deadline to a timeout.
   */
  case At(instant: Instant, clock: Clock)

  /**
   * Special deadline that never occurs.
   */
  case Inf

  /**
   * Special deadline that is considered over at any moment.
   */
  case MinusInf

  /**
   * Calculates the amount of time left until this deadline.
   * If this deadline is a [[Deadline.At]] then [[toTimeout]] returns the amount of time
   * left until this deadline by the [[clock]]
   * (a negative duration if this deadline is in the past).
   * For [[Deadline.Inf]] and [[Deadline.MinusInf]], returns [[Duration.Inf]] and [[Duration.MinusInf]] respectively.
   */
  def toTimeout: Duration = this match {
    case At(instant: Instant, clock: Clock) => Duration.fromNanos(clock.instant().until(instant, NANOS))
    case Inf => Duration.Inf
    case MinusInf => Duration.MinusInf
  }

  /**
   * Returns [[false]] if this deadline is in the future, [[true]] otherwise.
   * If this deadline is a [[Deadline.At]] then [[isOver]] tells if it is in the future by the [[clock]].
   * [[Deadline.Inf]] is never over, [[Deadline.MinusInf]] is always over.
   */
  def isOver: Boolean = toTimeout <= Duration.Zero

  /**
   * Compares this deadline to [[that]], the one which is earlier than another is considered less.
   * [[Deadline.At]] instances are compared by [[instant]].
   * [[Deadline.Inf]] is considered greater than any other deadline.
   * [[Deadline.MinusInf]] is considered less than any other deadline.
   * @return a negative, zero, or positive integer
   *         if [[this]] is respectively less than, equal to, or greater than [[that]].
   */
  override def compare(that: Deadline): Int = this match {
    case At(instant: Instant, clock: Clock) => that match {
      case At(thatInstant: Instant, _) => instant compareTo thatInstant
      case Inf => -1
      case MinusInf => 1
    }
    case Inf => if that == Inf then 0 else 1
    case MinusInf => if that == MinusInf then 0 else -1
  }
}
