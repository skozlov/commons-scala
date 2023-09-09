package com.github.skozlov.commons.scala.concurrent.collection.queue

import com.github.skozlov.commons.scala.time.Deadline

trait ConcurrentQueue[A] {
  def enqueue(newElements: Seq[_ <: A])(implicit deadline: Deadline): Unit

  def dequeue(minElements: Int, maxElements: Int)(implicit deadline: Deadline): Seq[A]
}
