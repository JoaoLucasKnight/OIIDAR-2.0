package com.example.oiidar.ui.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oiidar.convertType.toUser
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthVM @Inject constructor(
    private val repository: Repository
): ViewModel(){
    init {
        checkLogIn()
    }
    private val _user = MutableStateFlow<UserEntity?>(null)
    val user = _user.asStateFlow()
    fun checkSaveOrSave(){
        viewModelScope.launch() {
            try {
                val res =repository.getSpotifyUser()
                val check = repository.checkUserSave(res.nameId)
                if (check!= null) {
                    updateStatusUser(true, res.nameId)
                }else{
                    saveUserAndProgram(res)
                }
                checkLogIn()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

    }
    private fun checkLogIn(){
        viewModelScope.launch() {
            val result = repository.userLogIn()
            _user.value = result
        }
    }
    private suspend fun saveUserAndProgram(user: UserEntity){
        var useFlag = false
        var programFlag = false
        try {
            repository.saveUser(user)
            useFlag = true
            repository.saveProgram(user)
            programFlag = true
            repository.updateStatusUser(true, user.nameId)
        }catch (e: Exception){
            rollback(user,useFlag, programFlag)
            e.printStackTrace()

        }
    }
    private suspend fun rollback(userEntity: UserEntity,user: Boolean,program: Boolean){
        try{
            if (user) repository.deleteUser(userEntity)
            if (program) repository.deleteProgram(userEntity.nameId)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    suspend fun updateStatusUser(status: Boolean,user: String){
        repository.updateStatusUser(status ,user)
    }
}