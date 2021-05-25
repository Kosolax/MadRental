package com.formationandroid.madrental.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
class VehicleDTO (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nom: String,
    val image: String,
    val prixjournalierbase: String,
    val categorieco2: String
)