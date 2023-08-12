package tool.collectionspecials

/**
 * Returns an empty [CircularLinkedList] list.
 */
fun <T> emptyCircularLinkedList() =
    CircularLinkedList<T>()

/**
 * Returns a new [CircularLinkedList] filled with all elements of this collection.
 */
fun <T> Iterable<T>.toCircularLinkedList(): CircularLinkedList<T> {
    val cll = emptyCircularLinkedList<T>()
    this.forEach { item -> cll.add(item) }
    return cll
}

fun <T> emptyLinkedList() =
    LinkedList<T>()

fun <T> Iterable<T>.toLinkedList(): LinkedList<T> {
    val ll = emptyLinkedList<T>()
    this.forEach { item -> ll.add(item) }
    return ll
}
