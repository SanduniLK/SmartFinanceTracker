package com.example.smartfinancetracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    // සියලුම ගනුදෙනු අලුත්ම එකේ සිට පිළිවෙලට ලබා ගැනීමට
    @Query("SELECT * FROM finance_table ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<FinanceTransaction>>

    // අලුත් දත්තයක් ඇතුළත් කිරීමට (Income හෝ Spent)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: FinanceTransaction)

    // දත්තයක් මැකීමට
    @Delete
    suspend fun deleteTransaction(transaction: FinanceTransaction)

    // සියලුම දත්ත මැකීමට (Reset app)
    @Query("DELETE FROM finance_table")
    suspend fun deleteAll()
}