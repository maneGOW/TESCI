package com.manegow.tesci.ui.signup

import android.app.Application
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.manegow.tesci.BaseViewModel
import com.manegow.tesci.db.TesciDao
import com.manegow.tesci.db.User
import kotlinx.coroutines.*

class SignupViewModel(
    databaseInstance: FirebaseDatabase,
    val databaseRoom: TesciDao,
    val autentication: FirebaseAuth,
    application: Application
) : BaseViewModel(application) {

    private val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var databaseReference = databaseInstance.reference.child("Users")

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin : LiveData<Boolean>
    get() = _navigateToLogin


    private val _showToastError = MutableLiveData<Boolean>()
    val showToastError : LiveData<Boolean>
        get() = _showToastError

    private val _showToastCorrect = MutableLiveData<Boolean>()
    val showToastCorrect : LiveData<Boolean>
        get() = _showToastCorrect

    init {
        _navigateToLogin.value = false
        _showToastError.value = false
        _showToastCorrect.value = false
    }

    fun onLoginNavigated(){
        _navigateToLogin.value = false
    }

    fun onToastShowed(){
        _showToastCorrect.value = false
        _showToastError.value = false
    }

    private fun createNewAccount(controlNumber: String, email: String, password: String) {
        autentication.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                try{
                    val user: FirebaseUser = autentication.currentUser!!
                    verifyEmail(user)
                    createUserInstance(user, controlNumber, email, password)
                    _showToastCorrect.value = true
                    _navigateToLogin.value = true
                }catch (e:Exception){
                    println("Error al obtener usuario")
                }
            }.addOnFailureListener {
                println("Error en la autenticación.")
                _showToastError.value = true
                // Toast.makeText(
                //  getApplication(), "Error en la autenticación.",
                //  Toast.LENGTH_SHORT
                // ).show()
            }
    }

    private fun createUserInstance(
        user: FirebaseUser,
        controlNumber: String,
        email: String,
        password: String
    ) {
        createUserInFirebaseDB(user, controlNumber, email, password)
        createUserInRoom(controlNumber, email, password)
    }

    private fun createUserInRoom(
        controlNumber: String,
        email: String,
        password: String
    ) {
        uiScope.launch {
            val user = User(0,"", controlNumber, email, password, System.currentTimeMillis())
            suspendCreateUser(user)
            println("usuario $controlNumber creado en room")
        }
    }

    private suspend fun suspendCreateUser(user: User) {
        withContext(Dispatchers.IO) {
            databaseRoom.insertUser(user)
        }
    }

    private suspend fun suspendGetCurrentUser(): User? {
        return withContext(Dispatchers.IO) {
            databaseRoom.getUser(1)
        }
    }

    fun registerUser(controlNumber: String, email: String, password: String) {
        if (!controlNumber.isNullOrBlank()) {
            if (!email.isNullOrBlank()) {
                if (!password.isNullOrBlank()) {
                    createNewAccount(controlNumber, email, password)
                } else println("Tienes que escribir un password.")
            } else println("Tienes que escribir tu email.")
        } else println("Tienes que escribir un username.")
    }

    private fun createUserInFirebaseDB(
        user: FirebaseUser,
        controlNumber: String,
        email: String,
        password: String
    ) {
        val currentUserDb = databaseReference.child(user.uid)
        currentUserDb.child("email").setValue(email)
        currentUserDb.child("controlNumber").setValue(controlNumber)
        currentUserDb.child("password").setValue(password)
        currentUserDb.child("created").setValue(System.currentTimeMillis())
        currentUserDb.child("deviceId").setValue(Build.ID)
        currentUserDb.child("device").setValue(Build.DEVICE)
        currentUserDb.child("deviceModel").setValue(Build.MODEL)
        currentUserDb.child("isActive").setValue(true)
        println("usuario $controlNumber creado en firebase")
    }
/*
    private fun createUserInSafeAmiApi(username: String, email: String, password: String) {
        uiScope.launch {
            val request = UserRegistrationRequest(
                username,
                email,
                password,
                Build.ID,
                Build.DEVICE,
                Build.MODEL,
                "",
                true
            )
            val sendRequestDeferred =
                SafeAmiApi.amiApi.retrofitService.registerUserOnApi(request)
            try {
                val result = sendRequestDeferred.await()

                if (result.status == "0") {
                    generateWarningAlert("Registro", result.description, "Ok")
                } else {
                    println("error al crear usuario en mongo")
                }
            } catch (e: Exception) {
                println("error: $e")
                generateToast("Error de conexión, verifica que estés conectado a internet")
            }
        }
    }*/

    private fun verifyEmail(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println(user.email)
                } else {
                    println("Error al verificar el correo ")
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}
