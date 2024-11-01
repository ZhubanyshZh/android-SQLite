package com.example.workwithsqllite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workwithsqllite.db.SQLiteHelper
import com.example.workwithsqllite.service.PurchaseService
import com.example.workwithsqllite.view.AddPurchaseView

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sqliteHelper: SQLiteHelper = SQLiteHelper(this)
        val recyclerview: RecyclerView = findViewById(R.id.recyclerview)
        var searchText: SearchView = findViewById(R.id.searchId);
        val purchaseService: PurchaseService = PurchaseService(sqliteHelper, recyclerview)
        val sortBtn: Button = findViewById(R.id.sortBtn);
        var isAscending: Boolean = true
        var clearBtn: Button = findViewById(R.id.clearBtn)

        recyclerview.layoutManager = LinearLayoutManager(this)
        var data = sqliteHelper.getAllPurchase()

        val adapter = CustomAdapter(data, sqliteHelper, data)
        recyclerview.adapter = adapter

        val addBtn: Button = findViewById(R.id.addButton)

        addBtn.setOnClickListener() {
            val addPurchaseIntent: Intent = Intent(this, AddPurchaseView::class.java)
            startActivity(addPurchaseIntent)
        }

        searchText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var filteredList = data.filter {
                    it.name.contains(newText ?: "", ignoreCase = true)
                }
                adapter.filterList(filteredList)
                return true;
            }
        })

        sortBtn.setOnClickListener() {
            if (isAscending) {
                adapter.filterList(data.sortedBy { it.name })
                isAscending = false;
                sortBtn.text = "sort desc"
            } else {
                adapter.filterList(data.sortedByDescending { it.name })
                isAscending = true;
                sortBtn.text = "sort asc"
            }
        }

        clearBtn.setOnClickListener() {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm")
            builder.setMessage("Are you sure, to delete all purchase?")

            builder.setPositiveButton("Yes") { dialog, _ ->
                data = ArrayList()
                adapter.clearList()
                dialog.dismiss()
            }
            builder.setNegativeButton("No") {dialog, _ ->
                dialog.dismiss()
            }

            if(data.size != 0) {
                val dialog = builder.create()
                dialog.show()
            }
        }
    }
}