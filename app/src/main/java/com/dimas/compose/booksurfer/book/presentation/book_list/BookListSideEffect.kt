package com.dimas.compose.booksurfer.book.presentation.book_list

import com.dimas.compose.booksurfer.book.domain.Book

sealed interface BookListSideEffect {
    data class OnBookClick(val book: Book): BookListSideEffect
}