package com.example.oiidar.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oiidar.database.entities.UserEntity
import com.example.oiidar.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthVM @Inject constructor(
    private val repository: UserRepository
): ViewModel(){

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user = _user.asStateFlow()
    init {
        userLogado()
    }
    private fun userLogado() {
        viewModelScope.launch {
           _user.value = repository.buscaUserLogado()
        }
    }
    suspend fun verificarSeEstaSalvo(){
        viewModelScope.launch {
            repository.verificarSeJaEstaSalvo()
            userLogado()
        }
    }

    suspend fun deslogandoUser(){
        val userLogado = _user.value
        userLogado?.let {
            repository.deslogaUser(userLogado)
        }
    }

}