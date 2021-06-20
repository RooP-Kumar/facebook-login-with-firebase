package com.example.myapplication.ViewmodelsAndRepository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginRepository {
    private lateinit var auth: FirebaseAuth
    var firebaseUser = MutableLiveData<FirebaseUser>()

    fun loginFirebase(idToken: String) {
        auth = Firebase.auth
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                firebaseUser.postValue(auth.currentUser!!)
            }.addOnFailureListener {
            }

    }

}