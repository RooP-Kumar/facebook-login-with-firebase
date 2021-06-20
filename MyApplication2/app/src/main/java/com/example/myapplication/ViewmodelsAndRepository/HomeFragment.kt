package com.example.myapplication.ViewmodelsAndRepository

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class homeFragment : Fragment() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: FragmentHomeBinding
    private val RC_SIGN_IN = 123
    private lateinit var anotherContext: Context
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        anotherContext = inflater.context

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        val str = getString(R.string.default_web_client_id)

        navController = findNavController()

        googleSignInClient = loginViewModel.createReauest(anotherContext, str)

        auth = Firebase.auth

        mainFunction()

        return binding.root
    }

    private fun mainFunction() {
        binding.loginButton.setOnClickListener {
            signIn()
            loginViewModel.firebaseuser.observe(viewLifecycleOwner, {
                it?.let {
                    if (it != null) {
                        navController.navigate(R.id.action_homeFragment_to_mainFragment)
                    }
                }
            })
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                loginViewModel.loginFirebase(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user != null) {
            navController.navigate(R.id.action_homeFragment_to_mainFragment)
        }

    }

}