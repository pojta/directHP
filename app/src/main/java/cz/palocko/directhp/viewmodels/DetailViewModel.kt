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
class DetailViewModel @Inject constructor(
    private val charactersRepository: CharactersRepositoryInterface,
) : ViewModel() {

    private val itemLive = MutableLiveData<Character>()
    val getItemLive: LiveData<Character>
        get() = itemLive

    private val errorLive = MutableLiveData<Throwable?>(null)
    val getErrorLive: LiveData<Throwable?>
        get() = errorLive

    fun loadCharacter(id: String) {
        viewModelScope.launch {
            charactersRepository.getCharacter(id)
                .catch { e -> errorLive.value = e }
                .collect {
                    character -> run {
                        errorLive.value = null
                        itemLive.value = character
                    }
                }
        }
    }
}
