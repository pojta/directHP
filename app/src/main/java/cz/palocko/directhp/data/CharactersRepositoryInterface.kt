package cz.palocko.directhp.data

import kotlinx.coroutines.flow.Flow


interface CharactersRepositoryInterface {
    suspend fun getAllCharacters(): Flow<List<Character>>
    suspend fun getCharacter(id: String): Flow<Character>
}
