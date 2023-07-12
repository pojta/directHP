package cz.palocko.directhp.data

import cz.palocko.directhp.exceptions.EntityNotFoundException
import cz.palocko.directhp.exceptions.ServerErrorException
import cz.palocko.directhp.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CharacterRepository @Inject constructor(private val apiService: ApiService) :
    CharactersRepositoryInterface {

    override suspend fun getAllCharacters(): Flow<List<Character>> = flow {
        val response = apiService.characters()

        if (!response.isSuccessful) {
            if (response.code() >= 500) {
                throw ServerErrorException()
            }

            throw Exception()
        } else {
            if (response.body() == null) {
                throw Exception()
            }

            emit(response.body()!!)
        }
    }

    override suspend fun getCharacter(id: String): Flow<Character> = flow {
        val response = apiService.character(id)

        if (!response.isSuccessful) {
            if (response.code() >= 500) {
                throw ServerErrorException()
            }

            throw Exception()
        } else {
            if (response.body() == null) {
                throw Exception()
            }

            if (response.body()!!.isEmpty()) {
                throw EntityNotFoundException()
            }

            emit(response.body()!![0])
        }

    }
}
