package com.hemanth.vishwaktask.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hemanth.vishwaktask.data.local.dao.CommentDao
import com.hemanth.vishwaktask.data.local.entity.CommentEntity

@Database(entities = [CommentEntity::class], version = 1)
abstract class VishwakDatabase: RoomDatabase() {
    abstract fun getCommentDao(): CommentDao

    companion object {
        @Volatile
        private var INSTANCE: VishwakDatabase? = null
        @JvmStatic
        fun getDbHandler(context: Context): VishwakDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    VishwakDatabase::class.java,
                    "marketsimplified")
                    .build()
            }
            return INSTANCE as VishwakDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}