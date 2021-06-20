package com.example.myapplication.ViewmodelsAndRepository

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(application: Application): AndroidViewModel(application) {
    var firebaseuser: MutableLiveData<FirebaseUser>
    var repository: LoginRepository

    init {
        repository = LoginRepository()
        firebaseuser = repository.firebaseUser
    }

    fun loginFirebase(idToken: String) {
        repository.loginFirebase(idToken)
    }

    fun createReauest(context: Context, str: String): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(str)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        return googleSignInClient

    }
}

