package com.manegow.tesci.ui.signup

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.manegow.tesci.R
import com.manegow.tesci.databinding.FragmentSignupBinding
import com.manegow.tesci.db.TesciDatabase

class SignupFragment : Fragment() {

    private lateinit var signUpViewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bindingSignUp: FragmentSignupBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        val application = requireNotNull(this.activity).application
        val databaseInstance = FirebaseDatabase.getInstance()
        val authentication = FirebaseAuth.getInstance()
        val dataSource = TesciDatabase.getInstance(application).TesciDao()
        val viewModelFactory =
            SignupViewModelFactory(databaseInstance, dataSource, authentication, application)
        signUpViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SignupViewModel::class.java)

        bindingSignUp.btnRegister.setOnClickListener {
            signUpViewModel.registerUser(
                bindingSignUp.controlNumberInputEditText.text.toString(),
                bindingSignUp.emailInputEditText.text.toString(),
                bindingSignUp.passwordInputEditText.text.toString()
            )
        }

        signUpViewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(SignupFragmentDirections.actionNavSingupToNavLogin())
                signUpViewModel.onLoginNavigated()
            }
        })

        signUpViewModel.showToastError.observe(viewLifecycleOwner, Observer {
            if(it){
                generateToast("Error al registrar el usuario")
                signUpViewModel.onToastShowed()
            }
        })

        signUpViewModel.showToastCorrect.observe(viewLifecycleOwner, Observer {
            if(it){
                generateToast("Usuario registrado correctamente")
                signUpViewModel.onToastShowed()
            }
        })

        return bindingSignUp.root
    }

    private fun generateToast(text:String){
        Toast.makeText(this.requireContext(), text, Toast.LENGTH_LONG).show()
    }
}

