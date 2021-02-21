package com.hemanth.vishwaktask.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CommentEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val comment: String
    )