package tool.collectionspecials

abstract class LinkedListNode(var prev: LinkedListNode?, var next: LinkedListNode?): LinkedListPointer {

    override fun next(steps: Int): LinkedListPointer {
        var current: LinkedListNode? = this
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
        private class EmptyNode: LinkedListNode(null, null) {
            override fun pointsToListItem() = false
        }
    }
}


