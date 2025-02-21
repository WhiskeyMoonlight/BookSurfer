package com.dimas.compose.booksurfer.book.data.repository

import com.dimas.compose.booksurfer.book.data.mappers.toBook
import com.dimas.compose.booksurfer.book.data.network.RemoteBookDataSource
import com.dimas.compose.booksurfer.book.domain.Book
import com.dimas.compose.booksurfer.book.domain.BookRepository
import com.dimas.compose.booksurfer.core.domain.DataError
import com.dimas.compose.booksurfer.core.domain.Result
import com.dimas.compose.booksurfer.core.domain.map

class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource
): BookRepository {
    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> {
        return remoteBookDataSource
            .searchBooks(query)
            .map { dto ->
                dto.results.map { it.toBook() }
            }
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        TODO("Not yet implemented")
    }

}