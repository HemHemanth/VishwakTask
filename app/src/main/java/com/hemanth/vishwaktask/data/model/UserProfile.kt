package com.hemanth.vishwaktask.data.model

data class UserProfile(
    val userName: String,
    val profileImage: String,
    val youTubeURL: String,
    val facebookURL: String,
    val instagramURL: String,
    val companySiteURL: String
) {
    constructor(): this("", "", "", "", "", "")
}
