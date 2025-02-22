package com.dimas.compose.booksurfer.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dimas.compose.booksurfer.book.presentation.SelectedBookViewModel
import com.dimas.compose.booksurfer.book.presentation.book_detail.BookDetailAction
import com.dimas.compose.booksurfer.book.presentation.book_detail.BookDetailScreenRoot
import com.dimas.compose.booksurfer.book.presentation.book_detail.BookDetailViewModel
import com.dimas.compose.booksurfer.book.presentation.book_list.BookListScreenRoot
import com.dimas.compose.booksurfer.book.presentation.book_list.BookListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.BookGraph
    ) {
        navigation<Route.BookGraph>(
            startDestination = Route.BookList
        ) {
            composable<Route.BookList> {
                val viewModel = koinViewModel<BookListViewModel>()
                val selectedBookViewModel =
                    it.sharedKoinViewModel<SelectedBookViewModel>(
                        navController
                    )

                LaunchedEffect(true) {
                    selectedBookViewModel.onSelectBook(null)
                }

                BookListScreenRoot(
                    viewModel = viewModel,
                    onBookClick = { book ->
                        selectedBookViewModel.onSelectBook(book)
                        navController.navigate(
                            Route.BookDetail(book.id)
                        )
                    }
                )
            }

            composable<Route.BookDetail> {
                val selectedBookViewModel =
                    it.sharedKoinViewModel<SelectedBookViewModel>(
                        navController
                    )

                val selectedBook by selectedBookViewModel
                    .selectedBook
                    .collectAsStateWithLifecycle()

                val viewModel = koinViewModel<BookDetailViewModel>()

                LaunchedEffect(selectedBook) {
                    selectedBook?.let { currentBook ->
                        viewModel.onAction(
                            BookDetailAction.OnSelectedBookChange(currentBook)
                        )
                    }
                }

                BookDetailScreenRoot(
                    viewModel = viewModel,
                    onBackClick = { navController.navigateUp() }
                )
            }
        }
    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel<T>(
        viewModelStoreOwner = parentEntry
    )
}