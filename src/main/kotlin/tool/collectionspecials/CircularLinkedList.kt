package tool.collectionspecials

class CircularLinkedList<T>: LinkedList<T>() {

    override fun toString() =
        "[@]" + super.toString()

    override fun newNode(data: T, pprev: Node?, pnext: Node?) =
        CircularLinkedListNode(data, pprev, pnext, this)

}

class CircularLinkedListNode<T>(data: T, prev: Node?, next: Node?, owner: CircularLinkedList<T>): DataNode<T>(data, prev, next, owner) {

    override var prev: Node?
        get() = if (super.prev == null) owner.last else super.prev
        set (value) {
            super.prev = value
        }

    override var next: Node?
        get() = if (super.next == null) owner.first else super.next
        set (value) {
            super.next = value
        }

    override fun next(steps: Int): LinkedListPointer = super.next(steps % owner.size)
    override fun prev(steps: Int): LinkedListPointer = super.prev(steps % owner.size)
}



