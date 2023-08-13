package tool.collectionspecials

abstract class Node(open var prev: Node?, open var next: Node?): LinkedListPointer {

    override fun next(steps: Int): LinkedListPointer {
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
        return current?:emptyNode
    }

    override fun prev(steps: Int): LinkedListPointer =
        next(-steps)

    companion object {
        //todo: improve this node ??
        private val emptyNode = EmptyNode()
        private class EmptyNode: Node(null, null) {
            override fun pointsToListItem() = false
        }
    }
}
