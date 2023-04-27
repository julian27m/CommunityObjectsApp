package com.example.navigationdrawercommunityobjects.model

class VisitRepository {
    private val serviceAdapter = FirebaseServiceAdapter()
    fun getVisits(callback: (List<Visit>) -> Unit) {
        serviceAdapter.getVisits { visits ->
//            println("AAAAAAAAAAA Visits: $visits")
            callback(visits)
        }
    }

    fun saveVisit(visit: Visit) {
        serviceAdapter.saveVisit(visit)
    }

}
