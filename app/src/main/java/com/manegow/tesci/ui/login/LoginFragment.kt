package com.manegow.tesci.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.manegow.tesci.R
import com.manegow.tesci.databinding.FragmentLoginBinding
import com.manegow.tesci.db.TesciDatabase

class LoginFragment : Fragment() {

    private lateinit var callbackManager: CallbackManager
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val bindingLogin : FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        bindingLogin.lifecycleOwner = this

        val databaseInstance = FirebaseDatabase.getInstance()

        val application = requireNotNull(this.activity).application

        auth = FirebaseAuth.getInstance()

        val dataSource = TesciDatabase.getInstance(application).TesciDao()

        val viewModelFactory = LoginViewModelFactory(auth, dataSource, databaseInstance, application)

        callbackManager = CallbackManager.Factory.create()

        loginViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(LoginViewModel::class.java)

        bindingLogin.loginViewModel = loginViewModel

        /*bindingLogin.btnRecoverPassword.setOnClickListener { v: View ->
            v.findNavController().navigate(LoginFragmentDirections.actionNavLoginToRecoverPasswordFragment())
        }*/

        bindingLogin.btnSignUp.setOnClickListener { v: View ->
            v.findNavController().navigate(LoginFragmentDirections.actionNavLoginToNavSingup())
        }

        loginViewModel.navigateToUserRegistration.observe(viewLifecycleOwner, Observer {
                navigate ->
            if(navigate){
                this.findNavController().navigate(
                    LoginFragmentDirections
                        .actionNavLoginToNavSingup())
                loginViewModel.onSignUpNavigated()
            }
        })

        loginViewModel.navigateToMainScreen.observe(viewLifecycleOwner, Observer {
                navigate ->
            if(navigate){
                this.findNavController().navigate(
                    LoginFragmentDirections
                        .actionNavLoginToHomeFragment())
                loginViewModel.onMainScreenNavigated()
            }
        })

        bindingLogin.btnLogin.setOnClickListener {
            loginViewModel.loginWithEmail(bindingLogin.controlNumberInputEditText.text.toString(), bindingLogin.passwordInputEditText.text.toString())
            loginViewModel.onMainScreenNavigated()
        }

        return bindingLogin.root
    }
}