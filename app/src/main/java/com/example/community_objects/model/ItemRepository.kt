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

    fun getItems(callback: (List<Any>) -> Unit) {
//        println("ItemRepository.getItems")
//        create a coroutine to call get items from the service adapter
        GlobalScope.launch {
            val items = serviceAdapter.getItems()
            withContext(Dispatchers.Main) {
                callback(items)
            }
        }
    }
}