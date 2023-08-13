package tool.collectionspecials

fun main() {
    val ll = (1..10).toLinkedList()

    var p = ll.firstIndex()
    while (p.pointsToListItem()) {
        println(ll[p])
        p = p.next(3)
    }


}
