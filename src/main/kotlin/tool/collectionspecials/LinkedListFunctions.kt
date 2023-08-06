package tool.collectionspecials

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

