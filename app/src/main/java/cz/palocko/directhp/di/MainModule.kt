package cz.palocko.directhp.di

import android.content.ContentResolver
import android.content.Context
import cz.palocko.directhp.data.CharacterRepository
import cz.palocko.directhp.data.CharactersRepositoryInterface
import cz.palocko.directhp.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MainModule {

    @Singleton
    @Provides
    fun provideContentProvider(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Singleton
    @Provides
    fun provideCharacterRepository(apiService: ApiService): CharactersRepositoryInterface {
        return CharacterRepository(apiService)
    }

}
