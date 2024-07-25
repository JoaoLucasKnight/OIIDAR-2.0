package com.example.oiidar.ui.viewModel

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oiidar.authenticator.AuthInterceptor
import com.example.oiidar.authenticator.TokenProvider
import com.example.oiidar.contantes.CLIENT_ID
import com.example.oiidar.contantes.REDIRECT_URI
import com.example.oiidar.convertType.toUser
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.repositories.Repository
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
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

    private val _destination = MutableStateFlow(false)
    val destination = _destination.asStateFlow()
    init {
        checkLogIn()
    }

    fun checkSaveOrSave(){
        viewModelScope.launch {
            val res =repository.getSpotifyUser()
            val entity = res.toUser()
            if (repository.checkUserSave(entity.nameId)!=null) {
                updateStatusUser(entity)
            }else{
                saveUserAndProgram(entity)
                updateStatusUser(entity)
            }
            checkLogIn()
        }
    }
    private fun checkLogIn(){
        viewModelScope.launch {
            val result = repository.userLogIn()
            if (result != null) {
                _user.value = result
            }
        }
    }
    private suspend fun saveUserAndProgram(user: UserEntity){
        repository.saveUser(user)
        repository.saveProgram(user)
    }

    fun updateStatusUser(user: UserEntity){
        viewModelScope.launch {
            repository.updateStatusUser(user)
        }
    }
}