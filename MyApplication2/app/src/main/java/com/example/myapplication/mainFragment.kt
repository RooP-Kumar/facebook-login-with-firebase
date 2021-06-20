package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.ViewmodelsAndRepository.LoginViewModel
import com.example.myapplication.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth

class mainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        mainFunction()

        return binding.root
    }

    fun mainFunction() {
        binding.signOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            loginViewModel.firebaseuser.observe(viewLifecycleOwner, {
                it?.let {
                    it.delete()
                }
            })
            findNavController().navigate(R.id.action_mainFragment_to_homeFragment)
        }
    }


}