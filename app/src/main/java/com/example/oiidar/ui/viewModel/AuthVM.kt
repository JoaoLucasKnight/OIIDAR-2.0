package com.example.oiidar.ui.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oiidar.convertType.toUser
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthVM @Inject constructor(
    private val repository: Repository
): ViewModel(){

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user = _user.asStateFlow()

    init {
        viewModelScope.launch {
            checkLogIn()
        }
    }
    fun checkSaveOrSave(){
        viewModelScope.launch {
            val res =repository.getSpotifyUser()
            val check = repository.checkUserSave(res.nameId)
            if (check!= null) {
                updateStatusUser(true, res.nameId)
            }else{
                saveUserAndProgram(res)
            }
            checkLogIn()
        }
    }
    private suspend fun checkLogIn(){
        val result = repository.userLogIn()
        _user.value = result
    }
    private suspend fun saveUserAndProgram(user: UserEntity){
        repository.saveUser(user)
        repository.saveProgram(user)
        repository.updateStatusUser(true, user.nameId)
    }
    fun updateStatusUser(status: Boolean,user: String){
        viewModelScope.launch {
            repository.updateStatusUser(status ,user)
        }
    }
}