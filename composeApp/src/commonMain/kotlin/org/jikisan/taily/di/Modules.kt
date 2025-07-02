package org.jikisan.cmpecommerceapp.di

import org.jikisan.taily.viewmodel.PetApi
import org.jikisan.taily.domain.home.HomeRepository
import org.jikisan.taily.domain.home.HomeRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    single {}
    single{ PetApi(get()) }


    singleOf(::HomeRepositoryImpl).bind<HomeRepository>()
//    singleOf(::HomeRepository).bind<HomeRepository>()

//    singleOf(::HomeRepository).bind<HomeRepository>()
//    viewModelOf(::HomeViewModel)
//
//    singleOf(::ProductDetailRepository).bind<ProductDetailRepository>()
//    viewModelOf(::ProductDetailViewModel)
//
//    singleOf(::CartRepository).bind<CartRepository>()
//    viewModelOf(::CartViewModel)
}
