package com.dimas.compose.booksurfer.book.presentation.book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimas.compose.booksurfer.book.domain.Book
import com.dimas.compose.booksurfer.book.domain.BookRepository
import com.dimas.compose.booksurfer.core.domain.onError
import com.dimas.compose.booksurfer.core.domain.onSuccess
import com.dimas.compose.booksurfer.core.presentation.toUiText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class BookListViewModel(
    private val bookRepository: BookRepository
) : ViewModel(), ContainerHost<BookListState, BookListSideEffect> {

    private var cachedBooks = emptyList<Book>()

    private var searchJob: Job? = null
    private var observeFavoriteJob: Job? = null

    override val container =
        container<BookListState, BookListSideEffect>(BookListState()) {
            if (cachedBooks.isEmpty()) {
                observeSearchQuery()
            }
            observeFavoriteBooks()
        }

    fun onAction(action: BookListAction) {
        when (action) {
            is BookListAction.OnBookClick -> handleOnBookClick(action.book)

            is BookListAction.OnSearchQueryChange -> {
                handleOnSearchQueryChange(action.query)
            }

            is BookListAction.OnTabSelected -> {
                handleOnTabSelected(action.index)
            }
        }
    }

    private fun handleOnBookClick(book: Book) = intent {
        postSideEffect(BookListSideEffect.OnBookClick(book))
    }

    private fun handleOnSearchQueryChange(query: String) = intent {
        reduce { state.copy(searchQuery = query) }
    }

    private fun handleOnTabSelected(index: Int) = intent {
        reduce { state.copy(selectedTabIndex = index) }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery(): Job = intent {
        container.stateFlow
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(500L)
            .onEach { query ->
                when {
                    query.isBlank() -> reduce {
                        state.copy(
                            errorMessage = null,
                            searchResults = cachedBooks
                        )
                    }

                    query.length >= 2 -> {
                        searchJob?.cancel()
                        searchJob = searchBooks(query)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun searchBooks(query: String) = intent {
        reduce { state.copy(isLoading = true) }
        bookRepository
            .searchBooks(query)
            .onSuccess { searchResults ->
                reduce {
                    state.copy(
                        isLoading = false,
                        errorMessage = null,
                        searchResults = searchResults
                    )
                }
            }
            .onError { error ->
                reduce {
                    state.copy(
                        isLoading = false,
                        errorMessage = error.toUiText(),
                        searchResults = emptyList()
                    )
                }
            }
    }

    private fun observeFavoriteBooks() = intent {
        observeFavoriteJob?.cancel()
        observeFavoriteJob = bookRepository
            .getFavoriteBooks()
            .onEach { favoriteBooks ->
                reduce {
                    state.copy(
                        favoriteBooks = favoriteBooks
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}