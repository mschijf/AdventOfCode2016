package tool.collectionspecials

class CircularLinkedList<T>: LinkedList<T>() {

    override fun toString() =
        "@[ ${this.joinToString(" <-> ")} ]@"

    override fun newNode(data: T, pprev: LinkedListNode?, pnext: LinkedListNode?) =
        CircularLinkedListNode(data, pprev, pnext, this)

}

class CircularLinkedListNode<T>(data: T, prev: LinkedListNode?, next: LinkedListNode?, owner: CircularLinkedList<T>): DataNode<T>(data, prev, next, owner) {

    override fun next(steps: Int): LinkedListPointer {
        var current: LinkedListNode? = this
        if (steps >= 0) {
            var stepsToDo = steps % owner.size
            while (current != null && stepsToDo > 0) {
                current = if (current.next == null) owner.first else current.next
                stepsToDo--
            }
        } else {
            var stepsToDo = -steps
            while (current != null && stepsToDo > 0) {
                current = if (current.prev == null) owner.last else current.prev
                stepsToDo--
            }
        }
        return current!!
    }

    override fun prev(steps: Int): LinkedListPointer =
        super.prev(steps % owner.size)

}
