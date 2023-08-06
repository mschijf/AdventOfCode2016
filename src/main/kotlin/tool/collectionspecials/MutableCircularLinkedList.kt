package tool.collectionspecials

import kotlin.math.absoluteValue
import kotlin.random.Random

interface ListIndex {
    operator fun plus(steps: Int): ListIndex
    operator fun minus(steps: Int): ListIndex

    operator fun inc(): ListIndex
    operator fun dec(): ListIndex
}

/**
 * Returns an empty [MutableCircularLinkedList] list.
 */
fun <T> emptyMutableCircularList() =
    MutableCircularLinkedList<T>()

/**
 * Returns a new [MutableCircularLinkedList] filled with all elements of this collection.
 */
fun <T> Iterable<T>.toMutableCircularLinkedList(): MutableCircularLinkedList<T> {
    val cll = emptyMutableCircularList<T>()
    this.forEach { item -> cll.add(item) }
    return cll
}

class MutableCircularLinkedList<T>: MutableCollection<T> {
    private var cllId: Int = 0
    private var first: Node? = null

    override var size = 0
        private set

    init { clear() }

    fun firstIndexOrNull(): ListIndex? =
        first

    fun firstIndex(): ListIndex =
        if (first != null) first!! else throw Exception("Circular list is Empty")

    override fun isEmpty() =
        size == 0

    operator fun get(listIndex: ListIndex) =
        listIndex.asNode().data

    operator fun set(listIndex: ListIndex, element:T): T {
        val node = listIndex.asNode()
        val prevElement = node.data
        node.data = element
        return prevElement
    }

    override fun contains(element: T) =
        this.any { elt -> element == elt}

    override fun add(element: T) =
        if (first == null) addFirst(element) else add(firstIndex(), element)

    private fun addFirst(element: T): Boolean {
        first = newNode(element, null, null)
        size++
        return true
    }

    /**
     * Inserts an element into the list at the specified listIndex
     * assumption: the listIndex exists
     *
     */
    fun add(listIndex: ListIndex, element: T): Boolean {
        val node = listIndex.asNode()

        val new = newNode(element, node.prev, node)
        val tmpPrev = new.prev
        val tmpNext = new.next
        tmpPrev.next = new
        tmpNext.prev = new
        size++
        return true
    }

    /**
     * removes the element at 'listIndex'
     *
     * returns the data that was on this listIndex
     */
    fun removeAt(listIndex: ListIndex): T {
        val node = listIndex.asNode()

        node.prev.next = node.next
        node.next.prev = node.prev
        if (node == first) {
            first = node.next
        }
        size--
        if (size == 0) {
            first = null
        }
        node.prev = node
        node.next = node
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

    fun firstIndexOfOrNull(element: T) : ListIndex? {
        if (isEmpty())
            return null
        if (this[firstIndex()] == element)
            return firstIndex()

        var walker = firstIndex() + 1
        while (walker != first && this[walker] != element)
            walker++
        return walker
    }

    private fun newNode(data: T, pprev: Node?, pnext: Node?) =
        Node(data, pprev, pnext, cllId)

    private fun ListIndex.asNode() : Node {
        val node = this as MutableCircularLinkedList<T>.Node
        if (node.cllId != this@MutableCircularLinkedList.cllId) {
            throw Exception ("List Index not belonging to (Circular)LinkedList")
        }
        return node
    }

    override fun toString() =
        "[${this.joinToString(", ")}]"

    override fun iterator(): MutableIterator<T> =
        CircularLinkedListIterator(this)

    override fun retainAll(elements: Collection<T>): Boolean {
        var result = false
        if (isEmpty())
            return false
        val retainElements = elements.toSet()

        var walker = firstIndex() + 1
        while (walker != firstIndex()) {
            if (this[walker] in retainElements) {
                walker++
            } else {
                val toBeRemoved = walker
                walker++
                removeAt(toBeRemoved)
                result = true
            }
        }

        if (this[firstIndex()] !in retainElements) {
            removeAt(firstIndex())
            result = true
        }

        return result
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var atLeastOneRemoved = false
        elements.forEach { element ->
            val removed = remove(element)
            atLeastOneRemoved = atLeastOneRemoved || removed
        }
        return atLeastOneRemoved
    }

    override fun clear() {
        size = 0
        first = null
        cllId = Random.nextInt().absoluteValue
    }

    override fun addAll(elements: Collection<T>): Boolean {
        elements.forEach { element -> add(element) }
        return true
    }

    override fun containsAll(elements: Collection<T>) =
        this.toSet().isNotEmpty()

    //==================================================================================================================

    private inner class Node(var data: T, pprev: Node?, pnext: Node?, var cllId: Int): ListIndex {
        var prev: Node = pprev ?: this
        var next: Node = pnext ?: this

        override operator fun plus(steps: Int): ListIndex {
            if (this@MutableCircularLinkedList.isEmpty())
                throw Exception("List is empty")

            var current = this
            if (steps >= 0) {
                repeat(steps % size) { current = current.next }
            } else {
                repeat(-(steps % size)) { current = current.prev }
            }
            return current
        }

        override operator fun minus(steps: Int) =
            plus(-steps)

        override fun inc() =
            next

        override fun dec() =
            prev

        override fun toString() =
            data.toString()

        fun decouple() {
            cllId = -1
        }

    }

    //==================================================================================================================

    inner class CircularLinkedListIterator(private val cll: MutableCircularLinkedList<T>): MutableIterator<T> {
        private var cursor:ListIndex? = null
        private var lastReturned:ListIndex? = null

        override fun hasNext() =
            (cll.size > 0) && (cursor == null || cursor !== cll.firstIndexOrNull())

        override fun next(): T {
            if (!hasNext())
                throw NoSuchElementException()
            if (cursor == null)
                cursor = cll.firstIndexOrNull()
            val data = cll[cursor!!]
            lastReturned = cursor
            cursor = cursor!! + 1
            return data
        }

        override fun remove() {
            check(lastReturned != null)
            if (lastReturned == firstIndexOrNull())
                cursor = null
            cll.removeAt(lastReturned!!)
            lastReturned = null
        }
    }
}


