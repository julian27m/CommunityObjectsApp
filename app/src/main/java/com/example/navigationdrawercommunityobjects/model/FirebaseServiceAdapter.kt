package com.example.navigationdrawercommunityobjects.model

import android.net.Uri
import android.widget.ImageView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FirebaseServiceAdapter {
    private val firestore = Firebase.firestore
    private val storageRef = FirebaseStorage.getInstance().reference

    fun addItem(
        item: HashMap<String, String>,
        image: Uri,
        callback: (Boolean) -> Unit
    ) {
//        println("ItemRepository.addItem")
        // Generar un ID único para el item
        val categorystr = item["category"].toString()
        var category = ""
        when (categorystr) {
            "Protective equipment" -> category = "EPP"
            "Books" -> category = "books_printed"
            "Clothes" -> category = "clothes"
            "School and University Supplies" -> category = "school_university"
            "Other" -> category = "items"
        }


        val itemId = firestore.collection(category).document().id

        // Subir la imagen a Firebase Storage
        val imageRef = storageRef.child("items").child("$itemId.jpg")
        imageRef.putFile(image)
            .addOnSuccessListener { _ ->
                // Obtener la URL de la imagen subida
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Crear un nuevo item con los datos proporcionados
                    var newItem: Item? = null
                    when (categorystr) {
                        "Other" -> newItem = Item(
                            item["name"].toString(),
                            item["category"].toString(),
                            item["description"].toString(),
                            uri.toString(),
                            itemId
                        )
                        "Protective equipment" -> newItem = EPP(
                            item["name"].toString(),
                            item["category"].toString(),
                            item["description"].toString(),
                            uri.toString(),
                            itemId,
                            item["degree"].toString(),
                            item["type"].toString()
                        )
                        "Books" -> newItem = EPP(
                            item["name"].toString(),
                            item["category"].toString(),
                            item["description"].toString(),
                            uri.toString(),
                            itemId,
                            item["degree"].toString(),
                            item["type"].toString()
                        )
                        "Clothes" -> newItem = EPP(
                            item["name"].toString(),
                            item["category"].toString(),
                            item["description"].toString(),
                            uri.toString(),
                            itemId,
                            item["colors"].toString(),
                            item["size"].toString()
                        )
                        "School and University Supplies" -> newItem = EPP(
                            item["name"].toString(),
                            item["category"].toString(),
                            item["description"].toString(),
                            uri.toString(),
                            itemId,
                            item["reference"].toString()
                        )
                    }

                    // Agregar el nuevo item a Firestore
                    if (newItem != null) {
                        firestore.collection(category)
                            .document(itemId)
                            .set(newItem)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Si se agregó correctamente el item, llamar al callback con el item
                                    callback(true)
                                } else {
                                    // Si hubo un error al agregar el item, llamar al callback con un valor nulo
                                    callback(false)
                                }
                            }
                    } else {
                        // Si hubo un error al agregar el item, llamar al callback con un valor nulo
                        println("Error al crear el item")
                        callback(false)
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Si hubo un error al subir la imagen, llamar al callback con un valor nulo
                callback(false)
            }
    }

    fun getItems(callback: (List<Item>) -> Unit) {
        // Obtener todos los items de Firestore y llamar al callback con ellos
        val items = mutableListOf<Item>()
        firestore.collection("EPP").whereEqualTo("photo", true)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    println("doc: $doc")
                    val item = doc.toObject(EPP::class.java)
                    items.add(item)
                }
            }
        firestore.collection("books_printed").whereEqualTo("photo", true)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    println("doc: $doc")
                    val item = doc.toObject(Book::class.java)
                    items.add(item)
                }
            }
        firestore.collection("clothes").whereEqualTo("photo", true)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val item = doc.toObject(Clothes::class.java)
                    items.add(item)
                }
            }
        firestore.collection("school_university").whereEqualTo("photo", true)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val item = doc.toObject(Supplies::class.java)
                    items.add(item)
                }
            }
        firestore.collection("items").whereEqualTo("photo", true)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val item = doc.toObject(Item::class.java)
                    items.add(item)
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
                callback(item)
            }
    }

}
