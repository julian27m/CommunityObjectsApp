package com.example.community_objects.model

import android.net.Uri
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
class ItemRepository {
    private val serviceAdapter = FirebaseServiceAdapter()

    fun addItem(item: HashMap<String,String>, image: Uri, callback: (Boolean) -> Unit) {
//        println("ItemRepository.addItem")
        serviceAdapter.addItem(item, image) { success ->
            callback(success)
        }
    }


    fun getItem(itemId: String, callback: (Any?) -> Unit) {
        serviceAdapter.getItem(itemId) { item ->
            callback(item)
        }
    }

    fun getItems(username: String? = null, callback: (List<Any>) -> Unit) {
        GlobalScope.launch {
            val items = serviceAdapter.getItems(username)
            withContext(Dispatchers.Main) {
                callback(items)
            }
        }
    }
}