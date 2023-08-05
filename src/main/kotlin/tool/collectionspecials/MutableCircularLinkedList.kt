package tool.collectionspecials

class MutableCircularLinkedList<T>:Iterable<T> {

    private var first: Node? = null
    fun firstNodeOrNull() = first

    var size = 0
        private set

    fun isEmpty() = size == 0

    /**
     * add an element at the end of the list, which is the same as before the first element ever inserted
     *
     * if list is empty, then it will be the first element
     *
     * returns: the node including the element
     */
    fun add(element: T): Node {
        return if (first == null) {
            addFirst(element)
        } else {
            addBefore(firstNodeOrNull()!!, element)
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
    fun addBefore(node: Node, element: T): Node {
        val new = Node(element, node.prev, node)
        val tmpPrev = new.prev
        val tmpNext = new.next
        tmpPrev.next = new
        tmpNext.prev = new
        size++
        return new
    }

    /**
     * add an element after the node referred by 'node'
     * assumption: the node is in the list.
     *
     * returns: the new node including the element
     */
    fun addAfter(node: Node, element: T): Node {
        val new = Node(element, node, node.next)
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
        nodeToBeRemoved.prev = nodeToBeRemoved
        nodeToBeRemoved.next = nodeToBeRemoved

        return true
    }

    override fun toString() = this.joinToString(" ")

    inner class Node(var data: T, pprev: Node?=null, pnext: Node?=null) {
        var prev: Node = pprev ?: this
        var next: Node = pnext ?: this

        operator fun plus(steps: Int): Node {
            if (size == 0)
                throw Exception("List is empty")

            var current = this
            if (steps >= 0) {
                repeat(steps % size) { current = current.next }
            } else {
                repeat(-(steps % size)) { current = current.prev }
            }
            return current
        }

        operator fun minus(steps: Int): Node {
            return plus(-steps)
        }

        override fun toString() = data.toString()
    }

    override fun iterator(): Iterator<T>  = CircularLinkedListIterator(this)

    inner class CircularLinkedListIterator(private val cll: MutableCircularLinkedList<T>): Iterator<T> {
        private var current = firstNodeOrNull()
        private var neverIterated = true

        override fun hasNext(): Boolean {
            if (cll.isEmpty())
                return false
            if (neverIterated)
                return true
            return current !== cll.firstNodeOrNull()
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


