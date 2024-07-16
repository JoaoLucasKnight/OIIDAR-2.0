package com.example.oiidar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.oiidar.database.dao.Dao
import com.example.oiidar.database.entities.PlaylistEntity
import com.example.oiidar.database.entities.ProgramaEntity
import com.example.oiidar.database.entities.TrackEntity
import com.example.oiidar.database.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ProgramaEntity::class,
        PlaylistEntity::class,
        TrackEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class OiidarDatabase: RoomDatabase() {
    abstract fun dao(): Dao

}