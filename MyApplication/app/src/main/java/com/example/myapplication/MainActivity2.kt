package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.transform.CircleCropTransformation
import com.example.myapplication.databinding.ActivityMain2Binding
import com.google.firebase.auth.FirebaseAuth

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    private lateinit var imageuri: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebaseuser = FirebaseAuth.getInstance().currentUser!!
        if (firebaseuser.photoUrl != null) {
            imageuri = firebaseuser.photoUrl.toString() // Getting uri from facebook
            imageuri += "?type=large"
        }
        binding.imageView.load(imageuri) {
            transformations(CircleCropTransformation())
            placeholder(R.drawable.ic_launcher_foreground)
        }

        binding.goBackToFirstActivity.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finishAfterTransition()
        }

    }
}