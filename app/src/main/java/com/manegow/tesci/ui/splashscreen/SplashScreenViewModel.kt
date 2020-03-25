package com.manegow.tesci.ui.splashscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manegow.tesci.db.TesciDao
import com.manegow.tesci.db.User
import kotlinx.coroutines.*

class SplashScreenViewModel(val database: TesciDao, application: Application) :
    AndroidViewModel(application) {

    private val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    private val _navigateToMainScreen = MutableLiveData<Boolean>()
    val navigateToMainScreen: LiveData<Boolean>
        get() = _navigateToMainScreen

    fun onMainScreenNavigated(){
        _navigateToMainScreen.value = false
    }

    fun onLoginNavigated(){
        _navigateToLogin.value = false
    }

    fun getLocalUser(){
        uiScope.launch {
            _navigateToMainScreen.value = false
            _navigateToLogin.value = false
            val user = suspendGetCurrentUser()
            if(user != null){
                _navigateToMainScreen.value = true
            }else _navigateToLogin.value = true
        }
    }

    private suspend fun suspendGetCurrentUser(): User?{
        return withContext(Dispatchers.IO){
            database.getUser(1)
        }
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}