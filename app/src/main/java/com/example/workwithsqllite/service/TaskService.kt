package com.example.workwithsqllite.service

import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.workwithsqllite.MainActivity
import com.example.workwithsqllite.Purchase
import com.example.workwithsqllite.db.SQLiteHelper
import com.example.workwithsqllite.view.AddPurchaseView

class PurchaseService(
    val db: SQLiteHelper,
    val recyclerView: RecyclerView? = null
) {

    fun addPurchase(purchaseName: String, amount: Int) {
        if (purchaseName.isBlank() || amount.toString().isBlank()) {
            Toast.makeText(AddPurchaseView(), "enter all fields!", Toast.LENGTH_SHORT).show()
        } else {
            db.addPurchase(purchaseName, amount)
        }
    }

    fun deletePurchase(id: Int, purchaseIds: ArrayList<Int>?): Boolean {
        if (id.toString().isBlank()) {
            Toast.makeText(AddPurchaseView(), "id is empty!", Toast.LENGTH_SHORT).show()
            Log.d("purchase delete", "deleting purchase was wrong!")
            return false
        } else {
            db.deletePurchase(id)

            val position = purchaseIds?.find{it == id} ?: -1;
            recyclerView?.adapter?.notifyItemRemoved(position);
            Log.d("purchase delete", "success deleted purchase by id: $id")
            return true;
        }
    }

    fun changePurchase(id: Int, name: String, amount: String): Boolean {
        if (id.toString().isNotBlank() && name.isNotBlank() && amount.isNotBlank()) {
            db.updatePurchase(id, name, amount.toInt());
            Log.d("changing purchase", "purchase by id $id changed!")
            return true
        } else {
            Log.d("changing purchase", "changing purchase was wrong!")
            return false
        }
    }

    fun clear() {
        db.clear();
    }
}