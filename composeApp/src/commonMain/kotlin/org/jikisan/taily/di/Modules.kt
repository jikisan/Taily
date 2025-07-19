package org.jikisan.cmpecommerceapp.di

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import org.jikisan.cmpecommerceapp.util.Constant
import org.jikisan.taily.data.remote.supabase.config.SupabaseConfig
import org.jikisan.taily.data.remote.supabase.storage.StorageManager
import org.jikisan.taily.domain.home.HomeRepository
import org.jikisan.taily.domain.home.HomeRepositoryImpl
import org.jikisan.taily.domain.pet.PetRepository
import org.jikisan.taily.domain.pet.PetRepositoryImpl
import org.jikisan.taily.ui.screens.addpet.AddPetViewModel
import org.jikisan.taily.ui.screens.editpet.EditPetViewModel
import org.jikisan.taily.ui.screens.home.HomeViewModel
import org.jikisan.taily.ui.screens.pet.PetViewModel
import org.jikisan.taily.ui.screens.petdetails.PetDetailsViewModel
import org.jikisan.taily.ui.screens.petpassport.PetPassportViewModel
import org.jikisan.taily.viewmodel.PetApiService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    single {
        createSupabaseClient(
            supabaseUrl = Constant.SUPABASE_URL,
            supabaseKey = SupabaseConfig.anonKey
        ) {
            install(Auth)
            install(Storage)
        }
    }

    single { StorageManager(get()) }
    single { PetApiService(get()) }

    singleOf(::HomeRepositoryImpl).bind<HomeRepository>()
    singleOf(::PetRepositoryImpl).bind<PetRepository>()

    viewModelOf(::HomeViewModel)
    viewModelOf(::PetViewModel)
    viewModelOf(::PetDetailsViewModel)
    viewModelOf(::AddPetViewModel)
    viewModelOf(::EditPetViewModel)
    viewModelOf(::PetPassportViewModel)


}
