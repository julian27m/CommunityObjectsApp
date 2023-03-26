package com.example.navigationdrawercommunityobjects.model

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreRepository {
    private val firestore = Firebase.firestore

    fun addItem(item: Item, onComplete: (Boolean) -> Unit) {
        firestore.collection("items")
            .add(item)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun updateItem(itemId: String, item: Item, onComplete: (Boolean) -> Unit) {
        firestore.collection("items")
            .document(itemId)
            .set(item)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun getItem(itemId: String, onComplete: (Item?) -> Unit) {
        firestore.collection("items")
            .document(itemId)
            .get()
            .addOnSuccessListener { document ->
                val item = document.toObject(Item::class.java)
                onComplete(item)
            }
            .addOnFailureListener { onComplete(null) }
    }
}
