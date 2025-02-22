package com.dimas.compose.booksurfer.di

import com.dimas.compose.booksurfer.book.data.database.FavoriteBookDatabase
import com.dimas.compose.booksurfer.book.data.network.KtorRemoteBookDataSource
import com.dimas.compose.booksurfer.book.data.network.RemoteBookDataSource
import com.dimas.compose.booksurfer.book.data.repository.DefaultBookRepository
import com.dimas.compose.booksurfer.book.domain.BookRepository
import com.dimas.compose.booksurfer.book.presentation.SelectedBookViewModel
import com.dimas.compose.booksurfer.book.presentation.book_detail.BookDetailViewModel
import com.dimas.compose.booksurfer.book.presentation.book_list.BookListViewModel
import com.dimas.compose.booksurfer.core.data.HttpClientFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single<HttpClientEngine> { OkHttp.create() }

    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteBookDataSource).bind(RemoteBookDataSource::class)
    singleOf(::DefaultBookRepository).bind(BookRepository::class)
    single<FavoriteBookDatabase> { FavoriteBookDatabase.getDatabase(androidApplication()) }

    single { get<FavoriteBookDatabase>().favoriteBookDao }

    viewModelOf(::BookListViewModel)
    viewModelOf(::SelectedBookViewModel)
    viewModelOf(::BookDetailViewModel)
}