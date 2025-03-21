package com.dimas.compose.booksurfer.book.presentation.book_detail

sealed interface BookDetailSideEffect {
    data object OnBackClick : BookDetailSideEffect
}