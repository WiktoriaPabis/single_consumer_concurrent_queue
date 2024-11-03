package org.example

import kotlinx.atomicfu.*

/**
 *Single-consumer concurrent queue implementation using atomic operations.
 * This queue is designed for single-consumer scenarios where one thread is responsible for
 * dequeuing, but multiple threads can concurrently enqueue items
 *
 * The queue maintains atomic references to its head and taiil nodes, ensuring safe
 * operations in a multi-threaded environment
 *
 * @param T The type of elements stored in the queue
 */
class SingleConsumerQueue<T> {
    //atomic reference
    private val head = atomic<Node<T>?>(null)
    private val tail = atomic<Node<T>?>(null)

    init {
        // Initializes the queue with a dummy node to avoid special cases for empty queues
        val dummyNode = Node<T>(null)
        head.value = dummyNode
        tail.value = dummyNode
    }

    /**
     * Adds an element to the end of the queue. This method is thread-safe and can be called
     * by multiple producer threads simultaneously. The enqueue operation attempts to append
     * a new node at the end of the queue and updates the tail pointer if successful
     *
     * @param value The value to be added to the queue
     */
    fun enqueue(value: T) {
        val newNode = Node(value)

        while (true) {
            val currentTail = tail.value
            val next = currentTail?.next?.value

            if (currentTail == tail.value) { //check if the tail hasn't been modified by another thread
                if (next == null) {
                    // in case there's no node after the current tail, attempt to attach newNode here
                    if (currentTail?.next?.compareAndSet(null, newNode) == true) {
                        //successfully appended the new node; attempt to move the tail pointer forward
                        tail.compareAndSet(currentTail, newNode)
                        return
                    }
                } else {
                    // in case another node is already linked after the current tail, move the tail forward
                    tail.compareAndSet(currentTail, next)
                }
            }
        }
    }

    /**
     * Removes an element from the front of the queue. This method is intended for a single consumer
     * thread and may only be safely used by one thread to dequeue items
     *
     * The dequeue operation removes the head node, retrieves its value, and shifts the head pointer
     * to the next node. If the queue is empty, it returns null
     *
     * @return The dequeued value, or null if the queue is empty
     */
    fun dequeue(): T? {
        while (true) {
            val currentHead = head.value
            val currentTail = tail.value
            val nextHead = currentHead?.next?.value

            if (currentHead == currentTail) { // check if head and tail are pointing to the same node
                if (nextHead == null) {
                    return null // queue  empty
                }
                // in case the queue is in an inconsistent state, move the tail pointer forward
                tail.compareAndSet(currentTail, nextHead)
            } else {
                // attempt to move the head to the next node, effectively dequeuing the current head
                if (head.compareAndSet(currentHead, nextHead)) {
                    return nextHead?.value
                }
            }
        }
    }

    /**
     * A helper class representing a node in the queue. Each node holds a value and a reference
     * to the next node. The next reference is atomic, allowing safe updates in a concurrent environment
     *
     * @param T The type of the value contained in the node
     * @property value The value stored in this node
     */
    private class Node<T>(val value: T?) {
        //atomic reference to the next node in the queue
        val next = atomic<Node<T>?>(null)
    }
}
