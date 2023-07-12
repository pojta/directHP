package cz.palocko.directhp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.palocko.directhp.data.Character
import cz.palocko.directhp.data.CharactersRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val charactersRepository: CharactersRepositoryInterface,
) : ViewModel() {

    private val itemsLive = MutableLiveData<List<Character>>()

    val getItemsLive: LiveData<List<Character>>
        get() = itemsLive

    private val errorLive = MutableLiveData<Throwable?>(null)
    val getErrorLive: LiveData<Throwable?>
        get() = errorLive

    init {
        loadCharacters();
    }

    fun loadCharacters() {
        viewModelScope.launch {
            charactersRepository.getAllCharacters()
                .catch { e -> errorLive.value = e }
                .collect {
                    characters -> run {
                        errorLive.value = null
                        itemsLive.value = characters.sortedBy { it.name }
                    }
                }
        }
    }
}
