package com.example.navigationdrawercommunityobjects.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigationdrawercommunityobjects.model.Visit
import com.example.navigationdrawercommunityobjects.model.VisitRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch

class VisitViewModel(): ViewModel() {
    val repository = VisitRepository()
    val visitsLiveData = MutableLiveData<List<Visit>>()
    val visitsByCategoryLiveData = mutableMapOf<String, Int>()

    val visits: LiveData<List<Visit>>
        get() = visitsLiveData


    init {
        viewModelScope.launch {
            val visits = repository.getVisits() { visits ->
                visitsLiveData.value = visits
//              generate a dict with the category name and the amount of visits
                val visitsByCategory = visits.groupingBy { it.category }.eachCount()
//                println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa")
                println("Visits by category: $visitsByCategory")
            }
        }
    }

    fun saveVisit(category: String, user: String) {
        val time: Timestamp = Timestamp.now()
        val visit = Visit(category, time, user)
        viewModelScope.launch {
            println("Visit: $visit")
            println("Visit category: ${visit.category}")
            println("Visit time: ${visit.time}")
            println("Visit user: ${visit.user}")
            repository.saveVisit(visit)
        }
    }

//    funtion that returns a dict with the category name and the amount of visits with a callback
    fun getVisitsByCategory(callback: (Map<String, Int>) -> Unit) {
        repository.getVisits() { visits ->
            val visitsByCategory = visits.groupingBy { it.category }.eachCount()
            callback(visitsByCategory)
        }
    }

//    companion object {
//        private val instance = VisitViewModel()
//
//        fun getInstance(): VisitViewModel {
//            return instance
//        }
//    }
}