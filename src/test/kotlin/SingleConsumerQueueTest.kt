package org.example

import junit.framework.TestCase.assertEquals
import org.jetbrains.kotlinx.lincheck.*
import org.jetbrains.kotlinx.lincheck.annotations.*
import org.jetbrains.kotlinx.lincheck.strategy.stress.*
//import org.junit.*
import org.junit.jupiter.api.Test

/**
 * Test class for the SingleConsumerQueue implementation
 *This class uses multiple test methods to verify the functionality,
 * performance, and consistency of the SingleConsumerQueue
 */
class SingleConsumerQueueTest {
    private val queue = SingleConsumerQueue<Int>()

    /**
     * Operation to add an element to the queue
     * This is used by Lincheck for concurrency testing
     *
     * @param value The value to enqueue
     */
    @Operation
    fun enqueue(value: Int) = queue.enqueue(value)

    /**
     * Operation to remove and return an element from the queue.
     * This is used by Lincheck for concurrency testing
     *
     * @return The dequeued value, or null if the queue is empty
     */
    @Operation
    fun dequeue(): Int? = queue.dequeue()

    /**
     * Test for the queue using default Lincheck settings
     */
    @Test
    fun stressTest() = StressOptions().check(this::class)

    /**
     * The current test is not suitable for the existing implementation of the code,
     * as it fails to complete successfully when subjected to a higher number of threads and
     * iterations. This issue is likely due to synchronization problems in the dequeue() method,
     * where the current version of the program does not include locks for multi-threaded access.
     * For more information, please refer to the README file.
     *
     *
     * Stress test for the queue with customized options
     * - 10 iterations
     * - 2 operations per iteration
     * - 5 concurrent threads
     */
    @Test
    fun stressTestWithDifferentOptions() = StressOptions()
        .iterations(10)
        .invocationsPerIteration(2)
        .threads(5)
        .check(this::class)

    /**
     * Test for enqueue and dequeue operations.
     * Enqueues two elements and then dequeues them, verifying that
     * they are returned in FIFO order
     */
    @Test
    fun testEnqueueDequeue() {
        queue.enqueue(1)
        queue.enqueue(2)
        assert(queue.dequeue() == 1)
        assert(queue.dequeue() == 2)
        assert(queue.dequeue() == null)
    }

    /**
     * Test that enqueues one element, dequeues it, and verifies
     * that dequeuing from an empty queue returns nul
     */
    @Test
    fun simpleTest() {
        queue.enqueue(1)
        assertEquals(1, queue.dequeue())
        assertEquals(null, queue.dequeue())
    }

    /**
     * Test that alternates enqueue and dequeue operations.
     * Verifies that items are dequeued in the correct order after each
     * enqueue operation
     */
    @Test
    fun alternatingEnqueueDequeueTest() {
        queue.enqueue(1)
        assertEquals(1, queue.dequeue())
        queue.enqueue(2)
        assertEquals(2, queue.dequeue())
        queue.enqueue(3)
        queue.enqueue(4)
        assertEquals(3, queue.dequeue())
        assertEquals(4, queue.dequeue())
        assertEquals(null, queue.dequeue())
    }

    /**
     * Test to enqueue and dequeue a large number of elements.
     * Enqueues 1000 elements and then dequeues them, verifying
     * that they are dequeued in the correct order
     */
    @Test
    fun bulkEnqueueDequeueTest() {
        for (i in 1..1000) {
            queue.enqueue(i)
        }
        for (i in 1..1000) {
            assertEquals(i, queue.dequeue())
        }
        assertEquals(null, queue.dequeue())
    }

    /**
     * Test that checks the behavior of dequeuing from an empty queue.
     * Verifies that dequeue() returns null when the queue is empty
     */
    @Test
    fun emptyQueueDequeueTest() {
        assertEquals(null, queue.dequeue())
    }

    /**
     * Test for multiple enqueue operations followed by dequeue operations.
     * Enqueues three elements, then dequeues them to verify that they are
     * returned in the correct order
     */
    @Test
    fun multipleEnqueueTest() {
        queue.enqueue(10)
        queue.enqueue(20)
        queue.enqueue(30)
        assertEquals(10, queue.dequeue())
        assertEquals(20, queue.dequeue())
        assertEquals(30, queue.dequeue())
        assertEquals(null, queue.dequeue())
    }

    /**
     * Test that performs multiple enqueue and dequeue operations.
     * This test verifies that the queue remains consistent across multiple
     * operations and that elements are dequeued in the correct order
     */
    @Test
    fun consistencyTest() {
        queue.enqueue(1)
        queue.enqueue(2)
        assertEquals(1, queue.dequeue())
        queue.enqueue(3)
        assertEquals(2, queue.dequeue())
        queue.enqueue(4)
        assertEquals(3, queue.dequeue())
        assertEquals(4, queue.dequeue())
        assertEquals(null, queue.dequeue())
    }
}


