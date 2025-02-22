package com.dimas.compose.booksurfer.book.data.network

import com.dimas.compose.booksurfer.book.data.dto.BookWorkDto
import com.dimas.compose.booksurfer.book.data.dto.SearchResponseDto
import com.dimas.compose.booksurfer.core.domain.DataError
import com.dimas.compose.booksurfer.core.domain.Result

interface RemoteBookDataSource {
    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null
    ): Result<SearchResponseDto, DataError.Remote>

    suspend fun getBookDetails(bookWorkId: String): Result<BookWorkDto, DataError.Remote>
}