package tool.collectionspecials

import java.io.UncheckedIOException
import kotlin.math.absoluteValue
import kotlin.random.Random

//todo: conceptual thinking: what to do after you move your pointer next to last (return null? throw exception? return special Node?)

//todo: unit testing node++ for empty list
//todo: build circular linked list as subclass of linkedlist

class LinkedList<T>: MutableCollection<T> {
    private var cllId: Int = 0
    private var first: Node? = null
    private var last: Node? = null

    override var size = 0
        private set

    init { clear() }

    fun firstIndexOrNull(): LinkedListPointer? =
        first

    fun lastIndexOrNull(): LinkedListPointer? =
        last

    fun firstIndex(): LinkedListPointer =
        if (first != null) first!! else throw LinkedListException("List is Empty")
    fun lastIndex(): LinkedListPointer =
        if (last != null) last!! else throw LinkedListException("List is Empty")

    override fun isEmpty() =
        size == 0

    operator fun get(linkedListPointer: LinkedListPointer) =
        linkedListPointer.asNode().data

    operator fun set(linkedListPointer: LinkedListPointer, element:T): T {
        val node = linkedListPointer.asNode()
        val prevElement = node.data
        node.data = element
        return prevElement
    }

    override fun contains(element: T) =
        this.any { elt -> element == elt}

    override fun add(element: T) =
        if (first == null) addFirst(element) else addAfterLast(element)

    private fun addFirst(element: T): Boolean {
        check(first == null)

        first = newNode(element, null, null)
        last = first
        size++
        return true
    }

    private fun addAfterLast(element: T): Boolean {
        check (last != null)

        last!!.next = newNode(element, last, null)
        last = last!!.next
        size++
        return true
    }

    /**
     * Inserts an element into the list at the specified listIndex
     * assumption: the listIndex exists
     *
     */
    fun add(linkedListPointer: LinkedListPointer, element: T): Boolean {
        val node = linkedListPointer.asNode()

        val new = newNode(element, node.prev, node)
        node.prev = new
        val tmpPrev = new.prev
        if (tmpPrev != null) {
            tmpPrev.next = new
        } else {
            first = new
        }

        size++
        return true
    }

    /**
     * removes the element at 'listIndex'
     *
     * returns the data that was on this listIndex
     */
    fun removeAt(linkedListPointer: LinkedListPointer): T {
        val node = linkedListPointer.asNode()

        if (node == first) {
            if (node == last) {
                first = null
                last = null
            } else {
                node.next!!.prev = null
                first = node.next
            }
        } else if (node == last) {
            node.prev!!.next = null
            last = node.prev
        } else {
            node.prev!!.next = node.next
            node.next!!.prev = node.prev
        }
        size--

        node.decouple()

        return node.data
    }

    override fun remove(element: T): Boolean {
        val tobeRemoved = firstIndexOfOrNull(element)
        if (tobeRemoved != null) {
            removeAt(tobeRemoved)
            return true
        }
        return false
    }

    fun firstIndexOfOrNull(element: T)
        = firstIndexOfOrNullAfter(first, element)

    private fun firstIndexOfOrNullAfter(node: Node?, element: T) : LinkedListPointer? {
        var walker = node
        while (walker != null && this[walker] != element)
            walker = walker.next
        return walker
    }


    private fun newNode(data: T, pprev: Node?, pnext: Node?) =
        Node(data, pprev, pnext, cllId)

    private fun LinkedListPointer.asNode() : Node {
        @Suppress("UNCHECKED_CAST")
        val node: Node = this as LinkedList<T>.Node
        if (node.cllId != this@LinkedList.cllId) {
            throw LinkedListException ("List Index not belonging to (Circular)LinkedList")
        }
        return node
    }

    override fun toString() =
        "[${this.joinToString(" <-> ")}]"

    override fun iterator(): MutableIterator<T> =
        LinkedListIterator(this)

    override fun retainAll(elements: Collection<T>): Boolean {
        var result = false
        val retainElements = elements.toSet()

        var walker = first
        while (walker != null) {
            val check = walker
            walker = walker.next
            if (this[check] !in retainElements) {
                removeAt(check)
                result = true
            }
        }
        return result
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var result = false
        val removeElements = elements.toSet()

        var walker = first
        while (walker != null) {
            val check = walker
            walker = walker.next
            if (this[check] in removeElements) {
                removeAt(check)
                result = true
            }
        }
        return result
    }

    override fun clear() {
        size = 0
        first = null
        last = null
        cllId = Random.nextInt().absoluteValue
    }

    override fun addAll(elements: Collection<T>): Boolean {
        elements.forEach { element -> add(element) }
        return true
    }

    override fun containsAll(elements: Collection<T>) =
        elements.toSet().all{ item -> this.contains(item) }

    //==================================================================================================================

    private inner class Node(var data: T, var prev: Node?, var next: Node?, var cllId: Int): LinkedListPointer {

        override operator fun plus(steps: Int): LinkedListPointer {
            var current: Node? = this
            if (steps >= 0) {
                var stepsToDo = steps
                while (current != null && stepsToDo > 0) {
                    current = current.next
                    stepsToDo--
                }
            } else {
                var stepsToDo = -steps
                while (current != null && stepsToDo > 0) {
                    current = current.prev
                    stepsToDo--
                }
            }
            return current?:throw LinkedListException("LinkedListIndexPointer out of bounds")
        }

        override operator fun minus(steps: Int) =
            plus(-steps)

        override fun inc() =
            plus(1)

        override fun dec() =
            minus(1)

        override fun toString() =
            data.toString()

        fun decouple() {
            next = null
            prev = null
            cllId = -1
        }


    }

    //==================================================================================================================

    inner class LinkedListIterator(private val cll: LinkedList<T>): MutableIterator<T> {
        private var cursor:Node? = cll.first
        private var lastReturned:Node? = null

        override fun hasNext() =
            cursor != null

        override fun next(): T {
            if (!hasNext())
                throw NoSuchElementException()
            val data = cll[cursor!!]
            lastReturned = cursor
            cursor = cursor!!.next
            return data
        }

        override fun remove() {
            check(lastReturned != null)
            cll.removeAt(lastReturned!!)
            lastReturned = null
        }
    }
}


