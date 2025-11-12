package ru.itmo.calls.port.model

class PagedCollection<E>(
    val limit: Int,
    val offset: Int,
    val total: Int,
    val collection: Collection<E>,
) {
    companion object {
        fun <E> empty(limit: Int, offset: Int): PagedCollection<E> {
            return PagedCollection(
                limit = limit,
                offset = offset,
                total = 0,
                collection = emptyList()
            )
        }
    }
}
