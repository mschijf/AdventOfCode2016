package tool.collectionspecials

import java.util.Currency

class CircularLinkedList<T>: LinkedList<T>() {

    override fun toString() =
        "[@]" + super.toString()

    override fun newNode(data: T, pprev: Node?, pnext: Node?) =
        CircularLinkedListNode(data, pprev, pnext, this)

}

class CircularLinkedListNode<T>(data: T, prev: Node?, next: Node?, private val cll: CircularLinkedList<T>): DataNode<T>(data, prev, next, cll) {

    override fun next(steps: Int): LinkedListPointer {
        var current: Node? = this
        if (steps >= 0) {
            var stepsToDo = steps
            while (current != null && stepsToDo > 0) {
                current = if (current == cll.last) cll.first else current.next
                stepsToDo--
            }
        } else {
            var stepsToDo = -steps
            while (current != null && stepsToDo > 0) {
                current = if (current == cll.first) cll.last else current.prev
                stepsToDo--
            }
        }
        return current!!
    }
}



