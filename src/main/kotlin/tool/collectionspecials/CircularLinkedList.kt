package tool.collectionspecials

fun <T>emptyCircularList(): CircularLinkedList<T> {
    return CircularLinkedList<T>()
}

class CircularLinkedList<T>:Iterable<T> {

    private var first: Node? = null
    fun firstOrNull() = first

    var size = 0
        private set

    fun isEmpty() = size == 0

    /**
     * add an element before the first element ever inserted
     * if list is empty, then it will be the first element
     *
     * returns: the node including the element
     */
    fun add(element: T): Node {
        return if (first == null) {
            addFirst(element)
        } else {
            add(firstOrNull()!!, element)
        }
    }

    private fun addFirst(element: T): Node {
        size++
        first = Node(element)
        return first!!
    }

    /**
     * add an element before the node referred by 'node'
     * assumption: the node is in the list.
     *
     * returns: the new node including the element
     */
    fun add(node: Node, element: T): Node {
        val new = Node(element, node.prev, node)
        val tmpPrev = new.prev
        val tmpNext = new.next
        tmpPrev.next = new
        tmpNext.prev = new
        size++
        return new
    }

    /**
     * removes 'nodeToBeRemoved'
     */
    fun removeAt(nodeToBeRemoved: Node): Boolean {
        nodeToBeRemoved.prev.next = nodeToBeRemoved.next
        nodeToBeRemoved.next.prev = nodeToBeRemoved.prev
        if (first == nodeToBeRemoved) {
            first = nodeToBeRemoved.next
        }
        size--
        if (size == 0) {
            first = null
        }
        return true
    }

    /**
     * returns the data of the given node
     */
    fun get(p: Node): T {
        return p.data
    }

    override fun toString() = this.joinToString(" ")

    inner class Node(val data: T, pprev: Node?=null, pnext: Node?=null) {
        var prev: Node = pprev ?: this
        var next: Node = pnext ?: this

        fun plus(steps: Int): Node {
            var current = this
            if (steps >= 0) {
                repeat(steps % size) { current = current.next }
            } else {
                repeat(-(steps % size)) { current = current.prev }
            }
            return current
        }

        fun minus(steps: Int): Node {
            return plus(-steps)
        }

        override fun toString() = data.toString()
    }

    override fun iterator(): Iterator<T>  = CircularLinkedListIterator(this)

    inner class CircularLinkedListIterator(private val cll: CircularLinkedList<T>): Iterator<T> {
        private var current = firstOrNull()
        private var neverIterated = true

        override fun hasNext(): Boolean {
            if (cll.isEmpty())
                return false
            if (neverIterated)
                return true
            return current !== cll.firstOrNull()
        }

        override fun next(): T {
            if (!hasNext())
                throw Exception("No next on CycledLinkedList iterator")
            neverIterated = false
            val data = current!!.data
            current = current!!.next
            return data
        }
    }
}


