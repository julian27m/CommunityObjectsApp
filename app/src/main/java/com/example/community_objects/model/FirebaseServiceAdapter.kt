package com.example.community_objects.model

import android.net.Uri
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.google.firebase.Timestamp
import kotlin.collections.HashMap

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
        println(categorystr)
        var category = ""
        when (categorystr) {
            "Protective equipment" -> category = "Equipment"
            "Books" -> category = "Printed"
            "Clothes" -> category = "Clothes"
            "School and University Supplies" -> category = "Supplies"
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
                    var newItem: Any? = null
                    println(categorystr)
                    when (categorystr) {
                        "Other" -> newItem = Item(
                            item["name"].toString(),
                            item["category"].toString(),
                            item["description"].toString(),
                            uri.toString(),
                            item["user"].toString()
                        )
                        "Protective equipment" -> newItem = EPP(
                            item["name"].toString(),
                            item["category"].toString(),
                            item["description"].toString(),
                            uri.toString(),
                            item["degree"].toString(),
                            item["type"].toString(),
                            item["user"].toString()
                        )
                        "Books" -> newItem = Book(
                            item["title"].toString(),
                            item["category"].toString(),
                            item["description"].toString(),
                            uri.toString(),
                            item["user"].toString(),
                            item["author"].toString(),
                            item["subject"].toString()
                        )
                        "Clothes" -> newItem = Clothes(
                            item["name"].toString(),
                            item["category"].toString(),
                            item["description"].toString(),
                            uri.toString(),
                            item["colors"].toString(),
                            item["size"].toString(),
                            item["user"].toString()
                        )
                        "School and University Supplies" -> newItem = Supplies(
                            item["name"].toString(),
                            item["category"].toString(),
                            item["description"].toString(),
                            uri.toString(),
                            item["reference"].toString(),
                            item["title"].toString(),
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

//    fun getItems(callback: (List<Item>) -> Unit) {
////        test the conection getting a single item
////        firestore.collection("EPP").document("Wn2jIYyIbcp8ICHRKTxU").get()
////            .addOnSuccessListener { doc ->
////                println("doc: $doc")
////            }
//
//
//        // Obtener todos los items de Firestore y llamar al callback con ellos
//        val items = mutableListOf<Item>()
//        firestore.collection("Equipment").whereNotEqualTo("imageURL", "")
//            .get()
//            .addOnSuccessListener { docs ->
//                for (doc in docs) {
////                    println("doc: $doc")
//                    if (doc.get("imageURL") != null) {
////                        println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
//                        val item = doc.toObject(EPP::class.java)
//                        items.add(item)
//                    }
////                    val item = doc.toObject(EPP::class.java)
////                    items.add(item)
//                }
//                println("items length after EPP: ${items.size}")
//                firestore.collection("Printed").whereNotEqualTo("imageURL", "")
//                    .get()
//                    .addOnSuccessListener { docs ->
//                        for (doc in docs) {
////                            println("doc: $doc")
//                            val item = doc.toObject(Book::class.java)
//                            item.name = item.title
//                            items.add(item)
//                        }
//
//                        println("items length after books: ${items.size}")
//                        firestore.collection("Clothes").whereNotEqualTo("imageURL", "")
//                            .get()
//                            .addOnSuccessListener { docs ->
//                                for (doc in docs) {
//                                    val item = doc.toObject(Clothes::class.java)
//                                    items.add(item)
//                                }
//                                println("items length after clothes: ${items.size}")
//                                firestore.collection("Supplies")
//                                    .whereNotEqualTo("imageURL", "")
//                                    .get()
//                                    .addOnSuccessListener { docs ->
//                                        for (doc in docs) {
//                                            val item = doc.toObject(Supplies::class.java)
//                                            item.name = item.title
//                                            items.add(item)
//                                        }
//                                        println("items length after school_university: ${items.size}")
//                                        firestore.collection("items").whereNotEqualTo("imageURL", "")
//                                            .get()
//                                            .addOnSuccessListener { docs ->
//                                                for (doc in docs) {
//                                                    val item = doc.toObject(Item::class.java)
//                                                    items.add(item)
//                                                }
//                                                println("items length after items: ${items.size}")
//                                                callback(items)
//                                            }
//                                    }
//                            }
//                    }
//            }
//    }

    fun getItem(itemId: String, callback: (Any?) -> Unit) {
        // Obtener un item por su ID de Firestore y llamar al callback con él
        firestore.collection("items")
            .document(itemId)
            .get()
            .addOnSuccessListener { doc ->
                val item = doc.toObject(Item::class.java)
                callback(item)
            }
    }

    suspend fun getItems(): List<Any> {
        return withContext(Dispatchers.IO) {
            val items = mutableListOf<Any>()
            try {
                val eppTask = firestore.collection("Equipment")
                    .get()
                val eppDocs = eppTask.await()
                for (doc in eppDocs) {
                    if (doc.get("imageURL") != null) {
                        val item = doc.toObject(EPP::class.java)
                        items.add(item)
                    }
                }

                val booksTask = firestore.collection("Printed")
                    .get()
                val bookDocs = booksTask.await()
                for (doc in bookDocs) {
                    val item = doc.toObject(Book::class.java)
                    items.add(item)
                }

                val clothesTask = firestore.collection("Clothes")
                    .get()
                val clothesDocs = clothesTask.await()
                for (doc in clothesDocs) {
                    val item = doc.toObject(Clothes::class.java)
                    items.add(item)
                }

                val suppliesTask = firestore.collection("Supplies")
                    .get()
                val suppliesDocs = suppliesTask.await()
                for (doc in suppliesDocs) {
                    val item = doc.toObject(Supplies::class.java)
                    items.add(item)
                }

                val itemsTask = firestore.collection("items")
                    .get()
                val itemsDocs = itemsTask.await()
                for (doc in itemsDocs) {
                    val item = doc.toObject(Item::class.java)
                    items.add(item)
                }

                return@withContext items
            } catch (e: Exception) {
                println("Error al obtener los items: $e")
                return@withContext emptyList()
            }
        }
    }

    suspend fun getImageUrl(imageRef: StorageReference): String {
        return withContext(Dispatchers.IO) {
            val url = imageRef.downloadUrl.await()
            url.toString()
        }
    }

    fun getVisits(callback: (List<Visit>) -> Unit) {
        // Obtener todas las visitas del dia desde Firestore y llamar al callback con ellas
//        println("getVisits")
        val visits = mutableListOf<Visit>()
//        .whereGreaterThan("time", Timestamp.now().seconds - 86400)
        firestore.collection("category_popularity").whereNotEqualTo("time", null)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
//                    println("doc: $doc")
//                    filter the visits from the last 24 hours
                    val actual = Timestamp.now()
//                    println("actual: $actual")
                    val saved = doc.get("time") as Timestamp
                    val diff = actual.seconds - saved.seconds
//                    println("diff: $diff")
                    if (diff < 86400) {
                        val visit = doc.toObject(Visit::class.java)
                        visits.add(visit)
                    }
//                    val visit = doc.toObject(Visit::class.java)
//                    visits.add(visit)
                }
                callback(visits)
            }
    }

    fun saveVisit(visit: Visit) {
        // Guardar una visita en Firestore
        firestore.collection("category_popularity")
            .add(visit)
            .addOnSuccessListener { doc ->
                println("Visit saved")
            }
    }

}
