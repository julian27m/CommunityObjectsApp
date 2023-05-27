package com.example.community_objects.model

import android.net.Uri
import android.util.Log
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
            if (items.isEmpty()){
                //Log.d("ItemRepository", "items: $items")
            }


            //Log.d("ItemRepository", "items: $items")
            withContext(Dispatchers.Main) {
                callback(items)
            }
        }
    }

    fun getItemsByCategory(category: String, callback: (List<Any>) -> Unit) {
        GlobalScope.launch {
            val items = serviceAdapter.getItemsByCategory(category)
            withContext(Dispatchers.Main) {
                callback(items)
            }
        }
    }

    fun getItemsRequests(username: String? = null, callback: (List<Any>) -> Unit) {
        GlobalScope.launch {
            val items = serviceAdapter.getItemsRequests(username)
            withContext(Dispatchers.Main) {
                callback(items)
            }
        }
    }
}