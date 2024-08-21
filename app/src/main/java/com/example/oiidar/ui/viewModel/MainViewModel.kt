package com.example.oiidar.ui.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
): ViewModel(){

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user = _user.asStateFlow()

    private val _auth = MutableStateFlow(false)
    val auth = _auth.asStateFlow()

    private val _state = MutableStateFlow("LOADING")
    val state = _state.asStateFlow()

    fun checkUser() {
        viewModelScope.launch {
            val user = repository.userLogIn()
            _user.value = user
            if (user == null) { _state.value = "LOAD"}
        }
    }
    fun logOut(){
        viewModelScope.launch {
            user.value?.let{
                repository.updateStatusUser(false,it)
                _user.value = null
            }
        }
    }
    fun checkSaveOrSave(){
        viewModelScope.launch {
            try {
                val res =repository.getSpotifyUser()
                val check = repository.checkUserSave(res)
                if (check != null) {
                    repository.updateStatusUser(true, res)
                    _user.value = res
                }else{
                    repository.saveUserAndProgram(res)
                    _user.value = res
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    fun getAuth(res: Boolean){ _auth.value = res }

}
