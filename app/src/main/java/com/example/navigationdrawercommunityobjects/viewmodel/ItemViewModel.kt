package com.example.navigationdrawercommunityobjects.viewmodel

import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigationdrawercommunityobjects.model.Item
import com.example.navigationdrawercommunityobjects.model.ItemRepository
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class ItemViewModel() : ViewModel() {
    private val repository = ItemRepository()
    private val itemsLiveData = MutableLiveData<List<Any>>()
    val items: LiveData<List<Any>>
        get() = itemsLiveData

    init {
        viewModelScope.launch {
//            tener en cuenta el callback
            val items = repository.getItems() { items ->
                itemsLiveData.value = items
            }
        }
    }

    fun addItem(item: HashMap<String,String>, image: Uri, callback: (Boolean) -> Unit) {
//        println("ItemViewModel.addItem")
        repository.addItem(item, image) { success ->
            callback(success)
        }
    }


    fun getItem(itemId: String, callback: (Any?) -> Unit) {
        repository.getItem(itemId) { item ->
            callback(item)
        }
    }

    fun getAllItems(callback: (List<Any>) -> Unit) {
//        println("ItemViewModel.getAllItems")
        repository.getItems() { items ->
            callback(items)
        }
    }

}