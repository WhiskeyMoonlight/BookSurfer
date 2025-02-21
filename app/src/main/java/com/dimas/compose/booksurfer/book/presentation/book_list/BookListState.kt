package com.dimas.compose.booksurfer.book.presentation.book_list

import androidx.compose.runtime.Immutable
import com.dimas.compose.booksurfer.book.domain.Book
import com.dimas.compose.booksurfer.core.presentation.UiText

@Immutable
data class BookListState(
    val searchQuery: String = "Christine",
    val searchResults: List<Book> = emptyList(),
    val favoriteBooks: List<Book> = emptyList(),
    val isLoading: Boolean = true,
    val selectedTabIndex: Int = 0,
    val errorMessage: UiText? = null
)
