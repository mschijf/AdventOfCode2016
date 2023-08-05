package tool.collectionspecials

interface ListIndex {
    operator fun plus(steps: Int): ListIndex
    operator fun minus(steps: Int): ListIndex

    operator fun inc(): ListIndex
    operator fun dec(): ListIndex
}

fun <T> emptyMutableCircularList() =
    MutableCircularLinkedList<T>()

fun <T> Iterable<T>.toMutableCircularLinkedList(): MutableCircularLinkedList<T> {
    val cll = emptyMutableCircularList<T>()
    this.forEach { item -> cll.add(item) }
    return cll
}

class MutableCircularLinkedList<T> :Iterable<T> {

    private var first: Node? = null
    fun firstIndexOrNull(): ListIndex? = first
    fun firstIndex(): ListIndex = if (first != null) first!! else throw Exception("Circular list is Empty")

    var size = 0
        private set

    fun isEmpty() =
        size == 0
    fun isNotEmpty() =
        !isEmpty()

    operator fun get(listIndex: ListIndex) =
        listIndex.asNode().data

    operator fun set(listIndex: ListIndex, element:T): T {
        val node = listIndex.asNode()
        val prevElement = node.data
        node.data = element
        return prevElement
    }

    /**
     * add an element at the end of the list, which is the same as before the first element ever inserted
     *
     * if list is empty, then it will be the first element
     *
     * returns: the node including the element
     */
    fun add(element: T) {
        if (first == null) {
            addFirst(element)
        } else {
            add(firstIndexOrNull()!!, element)
        }
    }

    private fun addFirst(element: T) {
        size++
        first = Node(element)
    }

    /**
     * Inserts an element into the list at the specified listIndex
     * assumption: the listIndex exists
     *
     */
    fun add(listIndex: ListIndex, element: T) {
        val node = listIndex.asNode()

        val new = Node(element, node.prev, node)
        val tmpPrev = new.prev
        val tmpNext = new.next
        tmpPrev.next = new
        tmpNext.prev = new
        size++
    }

    /**
     * removes the element at 'listIndex'
     * assumption: the listIndex exists
     *
     * returns the data that was on this listIndex
     */
    fun removeAt(listIndex: ListIndex): T {
        val node = listIndex.asNode()

        node.prev.next = node.next
        node.next.prev = node.prev
        if (first == node) {
            first = node.next
        }
        size--
        if (size == 0) {
            first = null
        }
        node.prev = node
        node.next = node

        return node.data
    }

    override fun toString() =
        this.joinToString(" ")

    override fun iterator(): Iterator<T>  =
        CircularLinkedListIterator(this)

    private fun ListIndex.asNode() =
        this as MutableCircularLinkedList<T>.Node


    private inner class Node(var data: T, pprev: Node?=null, pnext: Node?=null): ListIndex {
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
    }

    inner class CircularLinkedListIterator(private val cll: MutableCircularLinkedList<T>): Iterator<T> {
        private var current = firstIndexOrNull()
        private var neverIterated = true

        override fun hasNext(): Boolean {
            if (cll.isEmpty())
                return false
            if (neverIterated)
                return true
            return current !== cll.firstIndexOrNull()
        }

        override fun next(): T {
            if (!hasNext())
                throw Exception("No next on CycledLinkedList iterator")
            neverIterated = false
            val data = cll[current!!]
            current = current!! + 1
            return data
        }
    }
}


