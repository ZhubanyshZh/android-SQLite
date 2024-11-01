package com.example.workwithsqllite.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.workwithsqllite.MainActivity
import com.example.workwithsqllite.R
import com.example.workwithsqllite.db.SQLiteHelper
import com.example.workwithsqllite.service.PurchaseService

class AddPurchaseView : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_task_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val db: SQLiteHelper = SQLiteHelper(this)
        val purchaseService: PurchaseService = PurchaseService(db)

        val addBtn: Button = findViewById(R.id.addButton)
        val name: EditText = findViewById(R.id.name)
        val amount: EditText = findViewById(R.id.amount)

        addBtn.setOnClickListener() {
            if(name.text.toString().isNotBlank() && amount.text.toString().isNotBlank()) {
                purchaseService.addPurchase(name.text.toString(), amount.text.toString().toInt())
                Toast.makeText(this, "success created purchase", Toast.LENGTH_SHORT).show();
                startActivity(Intent(this, MainActivity::class.java));
            } else {
                Toast.makeText(this, "all fields must entered!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}