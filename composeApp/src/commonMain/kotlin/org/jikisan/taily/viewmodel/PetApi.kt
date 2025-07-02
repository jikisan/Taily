package org.jikisan.taily.viewmodel

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.jikisan.cmpecommerceapp.util.ApiRoutes
import org.jikisan.taily.model.pet.PetItem

expect fun createHttpClient(): HttpClient

class PetApi {

    suspend fun fetchAllPets(): List<PetItem> {
        return createHttpClient().get(ApiRoutes.PETS).body()
    }

}

