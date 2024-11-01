package com.example.workwithsqllite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.workwithsqllite.db.SQLiteHelper
import com.example.workwithsqllite.service.PurchaseService


class Detail : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val db: SQLiteHelper = SQLiteHelper(this);
        val purchaseService: PurchaseService = PurchaseService(db);

        val saveBtn: Button = findViewById(R.id.saveBtn);
        val deleteBtn: Button = findViewById(R.id.deleteBtn);
        var purchaseName: EditText = findViewById(R.id.name);
        var amount: EditText = findViewById(R.id.amount);

        val purchase: Purchase =
            intent.getSerializableExtra("purchase") as Purchase;
        val purchaseNameEditable: Editable = SpannableStringBuilder(purchase.name)
        purchaseName.text = purchaseNameEditable;
        val purchaseAmountEditable: Editable = SpannableStringBuilder(purchase.amount.toString())
        amount.text = purchaseAmountEditable;

        deleteBtn.setOnClickListener() {
            if(purchaseService.deletePurchase(purchase.id, intent.getIntegerArrayListExtra("purchaseIds"))) {
                Toast.makeText(this, "purchase success deleted!", Toast.LENGTH_SHORT).show()
            }

            val mainActivityIntent: Intent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent);
        }

        saveBtn.setOnClickListener() {
            if(purchaseService.changePurchase(
                purchase.id,
                purchaseName.text.toString(),
                amount.text.toString()
            )){
                Toast.makeText(this, "success changed!", Toast.LENGTH_SHORT).show();
                val mainPageIntent: Intent = Intent(this, MainActivity::class.java);
                this.startActivity(mainPageIntent);
            } else {
                Toast.makeText(this, "all fields must entered!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}