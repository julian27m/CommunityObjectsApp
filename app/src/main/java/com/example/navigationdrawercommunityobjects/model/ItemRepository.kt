package com.example.navigationdrawercommunityobjects.model

import android.net.Uri

class ItemRepository {
    private val serviceAdapter = FirebaseServiceAdapter()

    fun addItem(item: HashMap<String,String>, image: Uri, callback: (Boolean) -> Unit) {
//        println("ItemRepository.addItem")
        serviceAdapter.addItem(item, image) { success ->
            callback(success)
        }
    }


    fun getItem(itemId: String, callback: (Item?) -> Unit) {
        serviceAdapter.getItem(itemId) { item ->
            callback(item)
        }
    }

    fun getItems(callback: (List<Item>) -> Unit) {
//        println("ItemRepository.getItems")
        serviceAdapter.getItems { items ->
            callback(items)
        }
    }
}