package com.dimas.compose.booksurfer.book.presentation.book_detail

import com.dimas.compose.booksurfer.book.domain.Book

sealed interface BookDetailAction {
    data object OnBackClick: BookDetailAction
    data object OnFavoriteClick : BookDetailAction
    data class OnSelectedBookChange(val book: Book) : BookDetailAction
}