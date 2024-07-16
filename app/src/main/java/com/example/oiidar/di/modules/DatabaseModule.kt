package com.example.oiidar.di.modules

import android.content.Context
import androidx.room.Room
import com.example.oiidar.database.OiidarDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext



@Module
@InstallIn(SingletonComponent::class)
object  DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): OiidarDatabase {
        return Room.databaseBuilder(
            context,
            OiidarDatabase::class.java,
            "oiidar_db"
        ).build()
    }
    @Provides
    fun providesDao(db: OiidarDatabase) = db.dao()
}