package com.oncontigoteam.oncontigo.menu.doctor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.oncontigoteam.oncontigo.MainActivity
import com.oncontigoteam.oncontigo.R
import com.oncontigoteam.oncontigo.profile.Profile
import com.oncontigoteam.oncontigo.register.RegisterFragment

public final class DoctorMenu : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctor_menu, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.closeMenu(view)
        this.home(view)
        this.patients(view)
        this.profile(view)
        this.logout(view)
    }

    private fun closeMenu(view: View): Unit {
        val backButton: ImageButton = requireView().findViewById<ImageButton>(R.id.doctor_menu_back_button)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
    private fun home(view: View): Unit {
        val myImageView: ImageView = view.findViewById(R.id.doctor_menu_home_button)
        myImageView.isClickable = true
        myImageView.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
    private fun patients(view: View): Unit { }
    private fun profile(view: View): Unit {
        val myImageView: ImageView = view.findViewById(R.id.doctor_my_profile_button)
        myImageView.isClickable = true
        myImageView.setOnClickListener {
            this.replaceFragment(Profile())
        }
    }
    private fun logout(view: View): Unit {
        val myImageView: ImageView = view.findViewById(R.id.doctor_logout_menu_button)
        myImageView.isClickable = true
        myImageView.setOnClickListener {
            requireActivity().finishAffinity()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }

    private fun replaceFragment(fragment: Fragment): Unit {
        val fragmentManager: FragmentManager = this.requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}