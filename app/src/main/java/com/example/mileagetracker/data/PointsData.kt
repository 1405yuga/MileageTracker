package com.example.mileagetracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = PointsData.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = JourneyData::class,
            parentColumns = [JourneyData.COLUMN_ID],
            childColumns = [PointsData.COLUMN_JOURNEY_ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(PointsData.COLUMN_JOURNEY_ID)]
)
data class PointsData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_POINT_ID)
    val pointId: Long = 0,

    @ColumnInfo(name = COLUMN_JOURNEY_ID)
    val journeyId: Long,

    @ColumnInfo(name = COLUMN_LATITUDE)
    val latitude: Double,

    @ColumnInfo(name = COLUMN_LONGITUDE)
    val longitude: Double
) {
    companion object {
        const val TABLE_NAME = "points"
        const val COLUMN_POINT_ID = "point_id"
        const val COLUMN_JOURNEY_ID = "journey_id"
        const val COLUMN_LATITUDE = "latitude"
        const val COLUMN_LONGITUDE = "longitude"
    }
}
