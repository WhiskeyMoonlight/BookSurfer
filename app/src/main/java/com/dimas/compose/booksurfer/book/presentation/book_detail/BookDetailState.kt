package com.dimas.compose.booksurfer.book.presentation.book_detail

import androidx.compose.runtime.Immutable
import com.dimas.compose.booksurfer.book.domain.Book

@Immutable
data class BookDetailState(
    val isLoading: Boolean = true,
    val isFavorite: Boolean = false,
    val book: Book? = null
)
