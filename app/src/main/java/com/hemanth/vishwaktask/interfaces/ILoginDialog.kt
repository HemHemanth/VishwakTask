package com.hemanth.vishwaktask.interfaces

import com.google.firebase.auth.FirebaseUser

interface ILoginDialog {

    fun onLoginSuccessful(currentUser: FirebaseUser)
}