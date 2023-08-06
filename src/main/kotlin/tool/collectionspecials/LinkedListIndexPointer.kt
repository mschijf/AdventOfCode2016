package tool.collectionspecials

interface LinkedListIndexPointer {
    operator fun plus(steps: Int): LinkedListIndexPointer
    operator fun minus(steps: Int): LinkedListIndexPointer

    operator fun inc(): LinkedListIndexPointer
    operator fun dec(): LinkedListIndexPointer
}