package com.hemanth.vishwaktask.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hemanth.vishwaktask.data.local.entity.CommentEntity

@Dao
interface CommentDao {
    @Insert
    fun insertComment(commentEntity: CommentEntity)

    @Query("SELECT * FROM CommentEntity WHERE userId =:id")
    fun getAllComments(id: String): List<CommentEntity>
}