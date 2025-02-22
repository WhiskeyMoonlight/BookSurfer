package com.dimas.compose.booksurfer.book.domain

import com.dimas.compose.booksurfer.core.domain.DataError
import com.dimas.compose.booksurfer.core.domain.EmptyResult
import com.dimas.compose.booksurfer.core.domain.Result
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote>
    suspend fun getBookDescription(bookId: String): Result<String?, DataError>

    fun getFavoriteBooks(): Flow<List<Book>>
    fun isBookFavorite(id: String): Flow<Boolean>
    suspend fun markAsFavorite(book: Book): EmptyResult<DataError.Local>
    suspend fun deleteFromFavorites(id: String)
}