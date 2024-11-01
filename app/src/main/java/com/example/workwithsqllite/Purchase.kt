package com.example.workwithsqllite

import java.io.Serializable

data class Purchase(val id: Int, var name: String, var amount: Int) : Serializable {
}
