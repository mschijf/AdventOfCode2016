package tool.collectionspecials

//todo: build circular linked list as subclass of linkedlist

open class LinkedList<T>: MutableCollection<T> {

    internal var first: LinkedListNode? = null
    internal var last: LinkedListNode? = null

    //todo: improve this
    override var size = 0
//        private set

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
        linkedListPointer.asDataNode().data

    operator fun set(linkedListPointer: LinkedListPointer, element:T): T {
        val node = linkedListPointer.asDataNode()
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
        val node = linkedListPointer.asDataNode()

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
        val node = linkedListPointer.asDataNode()

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

    private fun firstIndexOfOrNullAfter(node: LinkedListNode?, element: T) : LinkedListPointer? {
        var walker = node
        while (walker != null && this[walker] != element)
            walker = walker.next
        return walker
    }


    protected open fun newNode(data: T, pprev: LinkedListNode?, pnext: LinkedListNode?) =
        DataNode(data, pprev, pnext, this)

    private fun LinkedListPointer.asDataNode() : DataNode<T> {
        val linkedListNode: LinkedListNode = this as LinkedListNode
        if (!linkedListNode.pointsToListItem()) {
            throw LinkedListException ("Pointer does not point to a node (LinkedListNullPointer)")
        }

        @Suppress("UNCHECKED_CAST")
        val dataNode = linkedListNode as DataNode<T>
        if (dataNode.owner != this@LinkedList) {
            throw LinkedListException ("Pointer does not point to a node on this List")
        }
        return dataNode
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

    //todo: link tussen nodes en list weghalen
    final override fun clear() {
        size = 0
        first = null
        last = null
    }

    override fun addAll(elements: Collection<T>): Boolean {
        elements.forEach { element -> add(element) }
        return true
    }

    override fun containsAll(elements: Collection<T>) =
        elements.toSet().all{ item -> this.contains(item) }

}

//==================================================================================================================

open class DataNode<T>(var data: T, prev: LinkedListNode?, next: LinkedListNode?, val owner: LinkedList<T>): LinkedListNode(prev, next) {
    override fun toString() =
        data.toString()

    override fun pointsToListItem() =
        owner != null

    internal fun decouple() {
        next = null
        prev = null
    }
}


