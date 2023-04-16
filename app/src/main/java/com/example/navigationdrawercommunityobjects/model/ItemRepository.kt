package com.example.navigationdrawercommunityobjects.model

import android.net.Uri

class ItemRepository {
    private val serviceAdapter = FirebaseServiceAdapter()

    fun addItem(item: Item, image: Uri, callback: (Boolean, Item?) -> Unit) {
//        println("ItemRepository.addItem")
        serviceAdapter.addItem(item, image) { success, newItem ->
            callback(success, newItem)
        }
    }

    fun updateItem(itemId: String, item: Item, callback: (Boolean) -> Unit) {
        serviceAdapter.updateItem(itemId, item) { success ->
            callback(success)
        }
    }

    fun getItem(itemId: String, callback: (Item?) -> Unit) {
        serviceAdapter.getItem(itemId) { item ->
            callback(item)
        }
    }

    fun getItems(callback: (List<Item>) -> Unit) {
        serviceAdapter.getItems { items ->
            callback(items)
        }
    }
}