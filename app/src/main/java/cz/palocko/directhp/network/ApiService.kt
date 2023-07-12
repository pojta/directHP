package cz.palocko.directhp.network

import cz.palocko.directhp.data.Character
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("api/characters")
    suspend fun characters(): Response<List<Character>>

    @GET("api/character/{id}")
    suspend fun character(
        @Path("id") id: String,
    ): Response<List<Character>>

    @GET("api/characters/students")
    suspend fun students(): Response<Any>

    @GET("api/characters/stuff")
    suspend fun stuff(): Response<Any>

    @GET("api/characters/house/{house}")
    suspend fun charactersByHouse(
        @Path("house") house: String,
    ): Response<Any>

    @GET("api/spells")
    suspend fun spells(): Response<Any>
}
