package org.example

/**
 * Entry point of the application demonstrating the usage of the SingleConsumerQueue.
 * This example creates a queue, enqueues several integer values, and dequeues them,
 * printing each dequeued value to show the order and behavior of the queu
 */
fun main() {
    //
    val queue = SingleConsumerQueue<Int>()

    queue.enqueue(1)
    queue.enqueue(2)
    queue.enqueue(3)

    println("Dequeued: ${queue.dequeue()}")
    println("Dequeued: ${queue.dequeue()}")
    println("Dequeued: ${queue.dequeue()}")
    println("Dequeued: ${queue.dequeue()}")
}
