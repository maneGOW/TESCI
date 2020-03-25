package com.manegow.tesci.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.manegow.tesci.BaseViewModel
import com.manegow.tesci.db.TesciDao
import com.manegow.tesci.db.User
import kotlinx.coroutines.*

class LoginViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val datasource: TesciDao,
    private val databaseInstance: FirebaseDatabase,
    application: Application
) : BaseViewModel(application) {

    private val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToUserRegistration = MutableLiveData<Boolean>()
    val navigateToUserRegistration: LiveData<Boolean>
        get() = _navigateToUserRegistration

    private val _navigateToMainScreen = MutableLiveData<Boolean>()
    val navigateToMainScreen: LiveData<Boolean>
        get() = _navigateToMainScreen

    private fun loginUser(email: String, password: String) {
        _navigateToUserRegistration.value = false
        _navigateToMainScreen.value = false
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getLocalUser()
                    onMainScreenNavigated()
                    println("Successful login with email")
                } else {
                  /*  generateWarningAlert(
                        "No estas registrado",
                        "¿Deseas crear una nueva cuenta?",
                        "Ok",
                        this
                    )*/
                    println("no hay usuario registrado")
                    _navigateToUserRegistration.value = true
                }
            }
    }

    private fun getLocalUser() {
        uiScope.launch {
            _navigateToMainScreen.value = false
            _navigateToUserRegistration.value = false
            val user = suspendGetCurrentUser()
            if (user != null) {
                _navigateToMainScreen.value = true
            } else {
                initUserFromFirebase()
                _navigateToMainScreen.value = true
            }
        }
    }

    private suspend fun suspendGetCurrentUser(): User?{
        return withContext(Dispatchers.IO){
            datasource.getUser(1)
        }
    }

    private fun initUserFromFirebase() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val rootRef = FirebaseDatabase.getInstance().reference
        val uidRef = rootRef.child("Users").child(uid)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(com.manegow.tesci.data.firebase.FirebaseUser::class.java)
                println("usuario: ${user!!.email}")
                println("usuario: ${user.password}")
                println("usuario: ${user.controlNumber}")
                createUserInRoom(user.username, user.controlNumber, user.email, user.password)
            }

            override fun onCancelled(p0: DatabaseError) {
                println("Error al obtener datos de Firebase $p0")
            }
        }
        uidRef.addListenerForSingleValueEvent(valueEventListener)
    }

    private suspend fun suspendCreateUser(user: User) {
        withContext(Dispatchers.IO) {
            datasource.insertUser(user)
        }
    }

    private fun createUserInRoom(username: String, controlNumber: String, email: String, password: String) {
        uiScope.launch {
            val user = User(0, username, controlNumber, email, password, System.currentTimeMillis())
            suspendCreateUser(user)
            println("usuario $username creado en room")
        }
    }

    fun onSignUpNavigated() {
        _navigateToUserRegistration.value = false
    }

    fun onMainScreenNavigated() {
        _navigateToMainScreen.value = false
    }

    fun loginWithEmail(username: String, password: String) {
        if (!username.isNullOrBlank()) {
            if (!password.isNullOrBlank()) {
                loginUser(username, password)
            } else {
                println("Tienes que escribir tu contraseña")
               // generateToast("Tienes que escribir tu contraseña")
            }
        } else {
            println("Tienes que escribir tu email")
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            println(user.displayName)
            println(user.email)
            println(user.phoneNumber)
            println(user.photoUrl)
            println(user.uid)
        } else {
            println("NONE")
        }
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}