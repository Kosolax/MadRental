package com.formationandroid.madrental.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class VehicleDAO {
    @Query("SELECT * FROM vehicles")
    abstract fun getListVehicles(): List<VehicleDTO>

    @Insert
    abstract fun insert(vararg vehicle: VehicleDTO)

    @Query("SELECT COUNT(*) FROM vehicles WHERE id = :id")
    abstract fun countVehicleById(id: Long): Long
}