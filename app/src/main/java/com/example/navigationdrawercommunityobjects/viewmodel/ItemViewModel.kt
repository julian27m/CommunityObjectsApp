package com.example.navigationdrawercommunityobjects.viewmodel

import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigationdrawercommunityobjects.model.FirebaseServiceAdapter
import com.example.navigationdrawercommunityobjects.model.Item
import com.example.navigationdrawercommunityobjects.model.ItemRepository
import kotlinx.coroutines.launch

class ItemViewModel() : ViewModel() {
    private val repository = ItemRepository()
    private val itemsLiveData = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = itemsLiveData

    init {
        viewModelScope.launch {
//            tener en cuenta el callback
            val items = repository.getItems() { items ->
                itemsLiveData.value = items
            }
        }
    }

    fun addItem(item: Item, image: Uri, callback: (Boolean, Item?) -> Unit) {
//        println("ItemViewModel.addItem")
        repository.addItem(item, image) { success, newItem ->
            callback(success, newItem)
        }
    }

    fun updateItem(itemId: String, item: Item, callback: (Boolean) -> Unit) {
        repository.updateItem(itemId, item) { success ->
            callback(success)
        }
    }

    fun getItem(itemId: String, callback: (Item?) -> Unit) {
        repository.getItem(itemId) { item ->
            callback(item)
        }
    }
}