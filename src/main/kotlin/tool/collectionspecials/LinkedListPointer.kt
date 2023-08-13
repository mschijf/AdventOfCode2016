package tool.collectionspecials

interface LinkedListPointer {
    operator fun plus(steps: Int): LinkedListPointer
    operator fun minus(steps: Int): LinkedListPointer

    operator fun inc(): LinkedListPointer
    operator fun dec(): LinkedListPointer
}