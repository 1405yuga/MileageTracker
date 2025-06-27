package com.example.mileagetracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = JourneyData.TABLE_NAME)
data class JourneyData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 0,

    @ColumnInfo(name = COLUMN_TITLE)
    val title: String,

    @ColumnInfo(name = COLUMN_START_TIME)
    val startTime: Long,

    @ColumnInfo(name = COLUMN_END_TIME)
    val endTime: Long? = null
) {
    companion object {
        const val TABLE_NAME = "journey"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_START_TIME = "start_time"
        const val COLUMN_END_TIME = "end_time"
        val mock = JourneyData(
            id = 0L,
            title = "Mocked journey",
            startTime = 100L,
            endTime = 500L
        )
    }
}
