package com.github.skozlov.commons.scala.concurrent.collection.queue
import com.github.skozlov.commons.scala.concurrent.lock.SafeLock
import com.github.skozlov.commons.scala.time.Deadline

import java.lang.Math.min
import java.util
import scala.reflect.ClassTag

class ConcurrentArrayQueue[A : ClassTag] extends ConcurrentQueue[A] {
  import ConcurrentArrayQueue.*

  private val dequeueLock = SafeLock()

  private val enqueueLock = SafeLock()
  private var missingElements = 0
  private val enqueueCondition = enqueueLock.newCondition()

  private var enqueuedElements = new Buffer[A]()
  private var elementsToDequeue = new Buffer[A](0)

  override def enqueue(newElements: Seq[_ <: A])(implicit deadline: Deadline): Unit = {
    if (newElements.nonEmpty) {
      val newElementsArray = newElements.toArray // preparing an array for fast copying under lock
      enqueueLock.locking(() => {
        enqueuedElements.enqueue(newElementsArray)
        if (missingElements > 0 && (enqueuedElements.size >= missingElements)) {
          enqueueCondition.signalAll()
        }
      })
    }
  }

  override def dequeue(minElements: Int, maxElements: Int)(implicit deadline: Deadline): Seq[A] = {
    require(minElements >= 1)
    require(maxElements >= minElements)

    val result: Array[A] = {
      dequeueLock.locking(() => {
        if (elementsToDequeue.size >= minElements) {
          elementsToDequeue.dequeue(min(elementsToDequeue.size, maxElements))
        } else {
          enqueueLock.locking(() => {
            missingElements = minElements - elementsToDequeue.size
            try {
              enqueueCondition.await(() => enqueuedElements.size >= missingElements)
              val resultSize = min(maxElements, elementsToDequeue.size + enqueuedElements.size)
              val result = new Array[A](resultSize)
              val oldToDequeueSize = elementsToDequeue.size
              val oldEnqueuedSize = enqueuedElements.size
              elementsToDequeue.dequeue(number = oldToDequeueSize, dest = result, destStart = 0)
              enqueuedElements.dequeue(number = resultSize - oldToDequeueSize, dest = result, destStart = oldToDequeueSize)
              ???
            } finally {
              missingElements = 0
            }
          })
          ???
        }
      })
    }
    result // todo convert Array to Seq explicitly
  }
}

object ConcurrentArrayQueue {
  private val BufferDefaultCapacity: Int = 16

  private class Buffer[A : ClassTag](initialCapacity: Int = BufferDefaultCapacity) {
    val primitive: Boolean = implicitly[ClassTag[A]].runtimeClass.isPrimitive

    var array: Array[A] = new Array[A](initialCapacity)
    var startIndex: Int = 0
    var size: Int = 0

    def enqueue(elements: Array[A]): Unit = {
      val oldSize = size
      val newSize = oldSize + elements.length
      if (newSize > array.length) {
        val newCapacity = LazyList.iterate(array.length) {
            _ * 2
          }
          .filter {
            _ > newSize
          }
          .head
        val oldArray = array
        val newArray = new Array[A](newCapacity)
        Array.copy(src = oldArray, srcPos = 0, dest = newArray, destPos = 0, length = oldSize)

        array = newArray
      }

      size = newSize
    }

    def dequeue(number: Int): Array[A] = ???

    def dequeue(number: Int, dest: Array[A], destStart: Int): Unit = ???

    @deprecated
    def dequeueOld(number: Int): Seq[A] = { // todo delete
      val result = new Array[A](number)
      Array.copy(src = array, srcPos = startIndex, dest = result, destPos = 0, length = number)

      // for GC:
      if (number == size) {
        array = null
      } else if (!primitive) {
        util.Arrays.fill(array.asInstanceOf[Array[AnyRef]], startIndex, startIndex + number, null)
      }

      startIndex += number
      size -= number
      result // todo convert Array to Seq explicitly
    }
  }
}
