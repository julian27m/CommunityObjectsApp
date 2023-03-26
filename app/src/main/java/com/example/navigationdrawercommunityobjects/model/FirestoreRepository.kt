package com.example.navigationdrawercommunityobjects.model

import android.net.Uri
import android.widget.ImageView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FirestoreRepository {
    private val firestore = Firebase.firestore
    private val storageReference = FirebaseStorage.getInstance().reference

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

    fun addPhoto(ivItemImage: ImageView): String {
        val storageRef = storageReference.child("images/${ivItemImage.tag}")
        val imageRef = storageRef.child("image.jpg")
        val uploadTask = imageRef.putFile(Uri.parse(ivItemImage.tag.toString()))
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
        return ivItemImage.tag.toString()
    }

}
