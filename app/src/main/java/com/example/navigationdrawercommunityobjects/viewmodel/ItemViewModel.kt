package com.example.navigationdrawercommunityobjects.viewmodel

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.navigationdrawercommunityobjects.model.FirestoreRepository
import com.example.navigationdrawercommunityobjects.model.Item

class ItemViewModel : ViewModel() {
    private val repository = FirestoreRepository()

    private val _addItemResult = MutableLiveData<Boolean>()
    val addItemResult: LiveData<Boolean> = _addItemResult

    fun addItem(item: Item) {
        repository.addItem(item) { success ->
            _addItemResult.value = success
        }
    }

    private val _updateItemResult = MutableLiveData<Boolean>()
    val updateItemResult: LiveData<Boolean> = _updateItemResult

    fun updateItem(itemId: String, item: Item) {
        repository.updateItem(itemId, item) { success ->
            _updateItemResult.value = success
        }
    }

    private val _item = MutableLiveData<Item>()
    val item: LiveData<Item> = _item

    fun getItem(itemId: String) {
        repository.getItem(itemId) { item ->
            _item.value = item
        }
    }

    fun addPhoto(ivItemImage: ImageView): String {
//        put the foto on the storage
        return repository.addPhoto(ivItemImage)
    }
}
