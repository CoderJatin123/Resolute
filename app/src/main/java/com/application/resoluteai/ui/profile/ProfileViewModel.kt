package com.application.resoluteai.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class ProfileViewModel : ViewModel() {

    private val user = MutableLiveData<FirebaseUser?>()

    init {
        val mUser = FirebaseAuth.getInstance().currentUser
        if (mUser != null) {
           user.value=mUser
        }
    }
    val currentUser: MutableLiveData<FirebaseUser?> = user
}