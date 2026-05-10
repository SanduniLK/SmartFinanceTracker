package com.example.smartfinancetracker.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FinanceTransaction::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}