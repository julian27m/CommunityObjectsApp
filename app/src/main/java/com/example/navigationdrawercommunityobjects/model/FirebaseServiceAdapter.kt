package com.example.navigationdrawercommunityobjects.model

import android.net.Uri
import android.widget.ImageView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FirebaseServiceAdapter {
    private val firestore = Firebase.firestore
    private val storageRef = FirebaseStorage.getInstance().reference

    fun addItem(item: Item, image: Uri, callback: (Boolean, Item?) -> Unit) {
//        println("ItemRepository.addItem")
        // Generar un ID único para el item
        val itemId = firestore.collection("items").document().id

        // Subir la imagen a Firebase Storage
        val imageRef = storageRef.child("items").child("$itemId.jpg")
        imageRef.putFile(image)
            .addOnSuccessListener { _ ->
                // Obtener la URL de la imagen subida
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Crear un nuevo item con los datos proporcionados
                    val newItem = hashMapOf(
                        "id" to itemId,
                        "name" to item.name,
                        "description" to item.description,
                        "category" to item.category,
                        "image" to uri.toString()
                    )

                    // Agregar el nuevo item a Firestore
                    firestore.collection("items")
                        .document(itemId)
                        .set(newItem)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Si se agregó correctamente el item, llamar al callback con el item
                                callback(true, item.copy(id = itemId))
                            } else {
                                // Si hubo un error al agregar el item, llamar al callback con un valor nulo
                                callback(false, null)
                            }
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Si hubo un error al subir la imagen, llamar al callback con un valor nulo
                callback(false, null)
            }
    }

    fun updateItem(itemId: String, item: Item, onComplete: (Boolean) -> Unit) {
        firestore.collection("items")
            .document(itemId)
            .set(item)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun getItems(callback: (List<Item>) -> Unit) {
        // Obtener todos los items de Firestore y llamar al callback con ellos
        firestore.collection("items")
            .get()
            .addOnSuccessListener { result ->
                val items = result.documents.mapNotNull { doc ->
                    doc.toObject(Item::class.java)?.copy(id = doc.id)
                }
                callback(items)
            }
    }

    fun getItem(itemId: String, callback: (Item?) -> Unit) {
        // Obtener un item por su ID de Firestore y llamar al callback con él
        firestore.collection("items")
            .document(itemId)
            .get()
            .addOnSuccessListener { doc ->
                val item = doc.toObject(Item::class.java)
                callback(item?.copy(id = doc.id))
            }
    }

}
