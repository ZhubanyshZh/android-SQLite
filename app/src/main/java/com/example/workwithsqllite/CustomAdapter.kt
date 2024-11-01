package com.example.workwithsqllite

import android.annotation.SuppressLint
import android.content.ClipData.Item
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workwithsqllite.db.SQLiteHelper

class CustomAdapter(private var purchases: List<Purchase>, val db: SQLiteHelper, var data: List<Purchase>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = purchases[position]

        holder.purchaseName.text = itemsViewModel.name
        holder.amount.text = itemsViewModel.amount.toString()

        holder.itemView.setOnClickListener() {
            val detailPage: Intent = Intent(holder.itemView.context, Detail::class.java)
            Log.d("data123", data.toString()+"adapter")
            detailPage.putExtra("purchaseIds", ArrayList(data.map{it.id}))
            detailPage.putExtra("purchase", purchases.get(position))
            Log.d("intentDetailPage", "opening detail page");
            holder.itemView.context.startActivity(detailPage)
        }
    }

    override fun getItemCount(): Int {
        return purchases.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: List<Purchase>) {
        purchases = filteredList;
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearList() {
        purchases = ArrayList()
        notifyDataSetChanged()
        db.clear();
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val purchaseName: TextView = this.itemView.findViewById(R.id.name)
        val amount: TextView = this.itemView.findViewById(R.id.amount)
    }
}