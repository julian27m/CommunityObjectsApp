package com.example.community_objects.model

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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


    suspend fun getItems(username: String? = null): List<Any> {
        //Log.d("ItemRepository.getItems", "username: $username")
        return withContext(Dispatchers.IO) {
            val items = mutableListOf<Any>()
            try {
                val categories = listOf("Equipment", "Printed", "Clothes", "Supplies", "items")
                var query: Query;
                var categoryTask: Task<QuerySnapshot>;
                var categoryDocs: QuerySnapshot;
                var item: Any?;
                for (category in categories) {
                    query = firestore.collection(category)
                    if (username != null) {
                        query = query.whereEqualTo("user", username)
                        //Log.d("ItemRepository", "query: $query")
                    }
                    categoryTask = query.get()
                    categoryDocs = categoryTask.await()
                    for (doc in categoryDocs) {
                        if (doc.get("imageURL") != null) {
                            item = when(category) {
                                "Equipment" -> doc.toObject(EPP::class.java)
                                "Printed" -> doc.toObject(Book::class.java)
                                "Clothes" -> doc.toObject(Clothes::class.java)
                                "Supplies" -> doc.toObject(Supplies::class.java)
                                "items" -> doc.toObject(Item::class.java)
                                else -> null
                            }
                            item?.let { items.add(it) }
                            //if (item != null) {
                              //  Log.d("ItemRepository", "item: $item")
                            //}
                        }
                    }
                }
                //  Log.d("ItemRepository", "items: $items")
                return@withContext items
            } catch (e: Exception) {
                println("Error al obtener los items: $e")
                return@withContext emptyList()
            }
        }
    }

    suspend fun getItemsByCategory(category: String, username: String? = null): List<Any> {
        //Log.d("ItemRepository.getItems", "username: $username")
        return withContext(Dispatchers.IO) {
            val items = mutableListOf<Any>()
            try {
                val categories = listOf("Equipment", "Printed", "Clothes", "Supplies", "items")
                for (category in categories) {
                    var query: Query = firestore.collection(category)
                    if (username != null) {
                        query = query.whereEqualTo("user", username)
                        //Log.d("ItemRepository", "query: $query")
                    }
                    val categoryTask = query.get()
                    val categoryDocs = categoryTask.await()
                    for (doc in categoryDocs) {
                        if (doc.get("imageURL") != null) {
                            val item = when(category) {
                                "Equipment" -> doc.toObject(EPP::class.java)
                                "Printed" -> doc.toObject(Book::class.java)
                                "Clothes" -> doc.toObject(Clothes::class.java)
                                "Supplies" -> doc.toObject(Supplies::class.java)
                                "items" -> doc.toObject(Item::class.java)
                                else -> null
                            }
                            item?.let { items.add(it) }
                            //if (item != null) {
                              //  Log.d("ItemRepository", "item: $item")
                            //}
                        }
                    }
                }
                //  Log.d("ItemRepository", "items: $items")
                return@withContext items
            } catch (e: Exception) {
                println("Error al obtener los items: $e")
                return@withContext emptyList()
            }
        }
    }

    suspend fun getItemsRequests(username: String? = null): List<Any> {
        //Log.d("ItemRepository.getItems", "username: $username")
        return withContext(Dispatchers.IO) {
            val items = mutableListOf<Any>()
            try {
                val categories = listOf("Equipment_Request", "Printed_Request", "Clothes_Request", "Supplies_Request")
                for (category in categories) {
                    var query: Query = firestore.collection(category)
                    if (username != null) {
                        query = query.whereEqualTo("user", username)
                        //Log.d("ItemRepository", "query: $query")
                    }
                    val categoryTask = query.get()
                    val categoryDocs = categoryTask.await()
                    for (doc in categoryDocs) {
                            val item = when(category) {
                                "Equipment_Request" -> doc.toObject(EPPRequest::class.java)
                                "Printed_Request" -> doc.toObject(BookRequest::class.java)
                                "Clothes_Request" -> doc.toObject(ClothesRequest::class.java)
                                "Supplies_Request" -> doc.toObject(SuppliesRequest::class.java)
                                else -> null
                            }
                            item?.let { items.add(it) }
                            //if (item != null) {
                              //  Log.d("ItemRepository", "item: $item")
                            //}
                        }
                }
                //  Log.d("ItemRepository", "items: $items")
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
                var actual: Timestamp;
                var saved: Timestamp;
                var diff: Long;
                var visit: Visit;
                for (doc in docs) {
//                    println("doc: $doc")
//                    filter the visits from the last 24 hours
                    actual = Timestamp.now()
//                    println("actual: $actual")
                    saved = doc.get("time") as Timestamp
                    diff = actual.seconds - saved.seconds
//                    println("diff: $diff")
                    if (diff < 86400) {
                        visit = doc.toObject(Visit::class.java)
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
