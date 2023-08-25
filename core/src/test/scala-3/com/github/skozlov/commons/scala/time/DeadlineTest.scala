package com.github.skozlov.commons.scala.time

import com.github.skozlov.commons.scala.Test

import java.time.{Clock, Instant, ZoneOffset}
import scala.concurrent.duration.{Duration, DurationInt}

class DeadlineTest extends Test {
  private val now = Instant.ofEpochSecond(1000)
  private val clock = Clock.fixed(now, ZoneOffset.UTC)
  private val pastDeadline = Deadline.At(now.minusSeconds(10), clock)
  private val deadlineNow = Deadline.At(now, clock)
  private val futureDeadline = Deadline.At(now.plusSeconds(20), clock)

  test("compare"){
    // keep this list short enough because we test all its permutations
    val sorted = Seq(Deadline.MinusInf, pastDeadline, deadlineNow, futureDeadline, Deadline.Inf)
    sorted.permutations foreach {
      _.sorted shouldBe sorted
    }
  }

  test("toTimeout"){
    deadlineNow.toTimeout shouldBe Duration.Zero
    futureDeadline.toTimeout shouldBe 20.seconds
    pastDeadline.toTimeout shouldBe -10.seconds
    Deadline.Inf.toTimeout shouldBe Duration.Inf
    Deadline.MinusInf.toTimeout shouldBe Duration.MinusInf
  }

  test("isOver"){
    pastDeadline.isOver shouldBe true
    futureDeadline.isOver shouldBe false
    deadlineNow.isOver shouldBe true
    Deadline.Inf.isOver shouldBe false
    Deadline.MinusInf.isOver shouldBe true
  }
}
