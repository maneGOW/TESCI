package com.manegow.tesci.ui.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.manegow.tesci.db.TesciDao

class LoginViewModelFactory(
    private val firebaseAuth: FirebaseAuth,
    private val datasource: TesciDao,
    private val firebaseDataSource: FirebaseDatabase,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(firebaseAuth, datasource, firebaseDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}