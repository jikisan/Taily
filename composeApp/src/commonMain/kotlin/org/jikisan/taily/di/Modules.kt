package org.jikisan.cmpecommerceapp.di

import org.jikisan.taily.domain.home.HomeRepository
import org.jikisan.taily.domain.home.HomeRepositoryImpl
import org.jikisan.taily.domain.pet.PetRepository
import org.jikisan.taily.domain.pet.PetRepositoryImpl
import org.jikisan.taily.ui.screens.home.HomeViewModel
import org.jikisan.taily.ui.screens.pet.PetViewModel
import org.jikisan.taily.viewmodel.PetApiService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    single {}
    single{ PetApiService(get()) }

    singleOf(::HomeRepositoryImpl).bind<HomeRepository>()
    singleOf(::PetRepositoryImpl).bind<PetRepository>()
    viewModelOf(::HomeViewModel)
    viewModelOf(::PetViewModel)

//    singleOf(::HomeRepository).bind<HomeRepository>()
//    viewModelOf(::HomeViewModel)
//
//    singleOf(::ProductDetailRepository).bind<ProductDetailRepository>()
//    viewModelOf(::ProductDetailViewModel)
//
//    singleOf(::CartRepository).bind<CartRepository>()
//    viewModelOf(::CartViewModel)
}
