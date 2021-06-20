package com.example.myapplication

import android.app.ActivityOptions
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var callbackManager: CallbackManager

    private val EMAIL = "email"

    private var currentUser: FirebaseUser? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflating the activity using viewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = Firebase.auth

        binding.loginButton.setOnClickListener {
            callbackManager = CallbackManager.Factory.create();

            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(EMAIL))
            LoginManager.getInstance().registerCallback(callbackManager,
                    object : FacebookCallback<LoginResult?> {
                        override fun onSuccess(loginResult: LoginResult?) {
                            binding.progressBar.visibility = View.VISIBLE
                            if (loginResult?.accessToken != null) {
                                handleFacebookAccessToken(loginResult.accessToken)
                            }
                        }

                        override fun onCancel() {
                            // App code
                        }

                        override fun onError(exception: FacebookException) {
                            // App code
                        }
                    })
        }

    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, MainActivity2::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success")
                        this.currentUser = auth.currentUser
                        updateUI(this.currentUser)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val pair = Pair<View, String>(binding.text, "Text_animation")
            val option = ActivityOptions.makeSceneTransitionAnimation(this, pair)
            binding.progressBar.visibility = View.GONE
            startActivity(Intent(this, MainActivity2::class.java), option.toBundle())
        }
    }

}