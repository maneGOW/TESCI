package com.manegow.tesci.ui.signup

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.manegow.tesci.db.TesciDao

class SignupViewModelFactory (private val databaseInstance: FirebaseDatabase,
                              private val databaseRoom: TesciDao,
                              private val authentication: FirebaseAuth,
                              private val application: Application
): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SignupViewModel::class.java)){
            return SignupViewModel(databaseInstance, databaseRoom, authentication, application) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}