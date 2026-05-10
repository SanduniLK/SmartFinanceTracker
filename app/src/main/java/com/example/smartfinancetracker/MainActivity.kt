
package com.example.smartfinancetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import com.example.smartfinancetracker.data.AppDatabase
import com.example.smartfinancetracker.ui.HomeScreen // HomeScreen එක import කරගන්න
import com.example.smartfinancetracker.ui.theme.SmartFinanceTrackerTheme
import com.example.smartfinancetracker.viewmodel.FinanceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Database එක නිවැරදිව සාදා ගැනීම
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "finance_database"
        ).allowMainThreadQueries() // මුලින්ම ටෙස්ට් කරන්න ලේසි වෙන්න මේක දාන්න (පසුව අයින් කරන්න පුළුවන්)
            .build()

        val dao = db.transactionDao()
        val viewModel = FinanceViewModel(dao)

        enableEdgeToEdge()
        setContent {
            SmartFinanceTrackerTheme {
                // viewModel එක මෙහෙම ඇතුළත් කරන්න
                HomeScreen(viewModel = viewModel)
            }
        }
    }
}