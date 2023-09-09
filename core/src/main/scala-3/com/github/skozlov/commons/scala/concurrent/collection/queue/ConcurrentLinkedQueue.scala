package com.github.skozlov.commons.scala.concurrent.collection.queue

import com.github.skozlov.commons.scala.concurrent.lock.SafeLock
import com.github.skozlov.commons.scala.time.Deadline

import java.lang.Math.min
import java.util.concurrent.atomic.AtomicInteger
import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer
import com.github.skozlov.commons.scala.concurrent.lock.awaitWithDeadline

class ConcurrentLinkedQueue[A] extends ConcurrentQueue[A] {
  import ConcurrentLinkedQueue.*

  private val enqueueLock = SafeLock()
  private val dequeueLock = SafeLock()

  private val commonLock = SafeLock()
  private val condition = commonLock.newCondition()

  private val elements = LinkedList[A]() // elements.size does not contain any sane value
  private val size = new AtomicInteger(0)

  def enqueue(newElements: Seq[_ <: A])(implicit deadline: Deadline): Unit = {
    if (newElements.nonEmpty) {
      val suffix = LinkedList[A](newElements)
      enqueueLock.locking(() => {
        if (size.get() > 1) {
          elements.append(suffix) // todo fix: what if already dequeued all?
          size.addAndGet(suffix.size)
        } else {
          commonLock.locking(() => {
            elements.append(suffix)
            size.addAndGet(suffix.size)
            condition.signalAll()
          })
        }
      })
    }
  }

  def dequeue(minElements: Int, maxElements: Int)(implicit deadline: Deadline): Seq[A] = {
    require(minElements >= 1)
    require(maxElements >= minElements)

    val dequeued = LinkedList[A]()
    dequeueLock.locking(() => {
      while (dequeued.size < minElements) {
        val oldSize = size.get()
        if (oldSize > 1) {
          val toDequeue = min(oldSize - 1, maxElements - dequeued.size)
          dequeued.append(elements.removePrefix(lastRemoved = elements.findFromFirst(toDequeue - 1), toDequeue))
          size.addAndGet(toDequeue)
        } else {
          commonLock.locking(() => {
            @tailrec
            def waitForSize(): Int = {
              val size = this.size.get()
              if (size >= 2 || size >= maxElements - dequeued.size) {
                size
              } else {
                condition.unsafe.awaitWithDeadline
                waitForSize()
              }
            }

            val oldSize = waitForSize()
            ???
          })
        }
      }
    })
    dequeued.toSeq
  }
}

object ConcurrentLinkedQueue {
  private class Node[A](val value: A, var previous: Node[A] = null, var next: Node[A] = null)

  private class LinkedList[A] private(var first: Node[A], var last: Node[A], var size: Int) { // `size` contains a sane value only for a not-yet-enqueued or dequeued sublist
    def append(that: LinkedList[A]): Unit = {
      if (that.first != null) {
        if (last == null) {
          first = that.first
          last = that.last
          size = that.size
        } else {
          that.first.previous = last
          last.next = that.first
          last = that.last
          size += that.size
        }
      }
    }

    def toSeq: Seq[A] = {
      val buffer = new ArrayBuffer[A](size)

      @tailrec
      def loop(node: Node[A]): Unit = {
        if (node != null) {
          buffer append node.value
          loop(node.next)
        }
      }

      buffer.toSeq
    }

    def removePrefix(lastRemoved: Node[A], number: Int): LinkedList[A] = {
      val removedList = new LinkedList[A](first = first, last = lastRemoved, size = number)
      size -= number
      first = lastRemoved.next
      if (first == null) {
        last = null
      }
      lastRemoved.next = null
      removedList
    }

    def findFromFirst(index: Int): Node[A] = {
      @tailrec
      def find(first: Node[A], index: Int): Node[A] = {
        if (index == 0) {
          first
        } else {
          find(first.next, index - 1)
        }
      }

      find(first, index)
    }

    def findFromLast(indexFromLast: Int): Node[A] = {
      @tailrec
      def find(last: Node[A], indexFromLast: Int): Node[A] = {
        if (indexFromLast == 0) {
          last
        } else {
          find(last.previous, indexFromLast - 1)
        }
      }

      find(last, indexFromLast)
    }
  }

  private object LinkedList {
    def apply[A](): LinkedList[A] = new LinkedList[A](first = null, last = null, size = 0)

    def apply[A](elements: Seq[_ <: A]): LinkedList[A] = {
      val iterator = elements.iterator
      if (iterator.hasNext) {
        val list: LinkedList[A] = {
          val node = new Node[A](iterator.next())
          new LinkedList[A](first = node, last = node, size = 1)
        }
        while (iterator.hasNext) {
          val newNode = new Node[A](value = iterator.next(), previous = list.last)
          list.last.next = newNode
          list.last = newNode
          list.size += 1
        }
        list
      } else {
        LinkedList()
      }
    }
  }
}
