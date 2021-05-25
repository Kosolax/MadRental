package com.formationandroid.madrental.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [VehicleDTO::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun vehiclesDAO(): VehicleDAO
}