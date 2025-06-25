package com.example.mileagetracker.network.db

import android.content.Context
import androidx.room.Room
import com.example.mileagetracker.network.dao.JourneyDao
import com.example.mileagetracker.network.dao.PointsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "journey_db"
        ).build()
    }

    @Provides
    fun provideJourneyDao(db: AppDatabase): JourneyDao = db.journeyDao()

    @Provides
    fun providePointsDao(db: AppDatabase): PointsDao = db.pointsDao()
}