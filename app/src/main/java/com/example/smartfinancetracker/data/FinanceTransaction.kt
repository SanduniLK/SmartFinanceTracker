package com.example.smartfinancetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "finance_table")
data class FinanceTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val type: String,     // "INCOME" හෝ "SPENT"
    val category: String,
    val date: Long
)