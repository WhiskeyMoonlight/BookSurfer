package com.dimas.compose.booksurfer.book.presentation.book_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.dimas.compose.booksurfer.app.Route
import com.dimas.compose.booksurfer.book.domain.Book
import com.dimas.compose.booksurfer.book.domain.BookRepository
import com.dimas.compose.booksurfer.core.domain.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class BookDetailViewModel(
    private val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<BookDetailState, BookDetailSideEffect> {

    private val bookId = savedStateHandle.toRoute<Route.BookDetail>().id

    override val container =
        container<BookDetailState, BookDetailSideEffect>(BookDetailState()) {
            fetchBookDescription()
            observeFavoriteStatus()
        }

    fun onAction(action: BookDetailAction) {
        when (action) {
            BookDetailAction.OnFavoriteClick -> {
                handleOnFavoriteClick()
            }

            is BookDetailAction.OnSelectedBookChange -> {
                handleOnSelectedBookChange(action.book)
            }

            BookDetailAction.OnBackClick -> {
                handleOnBackClick()
            }
        }
    }

    private fun handleOnSelectedBookChange(book: Book?) = intent {
        reduce { state.copy(book = book) }
    }

    private fun handleOnFavoriteClick() = intent {
        if (state.isFavorite)
            bookRepository.deleteFromFavorites(bookId)
        else state.book?.let { book ->
            bookRepository.markAsFavorite(book)
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(BookDetailSideEffect.OnBackClick)
    }

    private fun observeFavoriteStatus() = intent {
        bookRepository
            .isBookFavorite(bookId)
            .onEach { isFavorite ->
                reduce { state.copy(isFavorite = isFavorite) }
            }
            .launchIn(viewModelScope)
    }

    private fun fetchBookDescription() = intent {
        viewModelScope.launch {
            bookRepository
                .getBookDescription(bookId)
                .onSuccess { description ->
                    reduce {
                        state.copy(
                            book = state.book?.copy(
                                description = description
                            ),
                            isLoading = false
                        )
                    }
                }
        }
    }

}