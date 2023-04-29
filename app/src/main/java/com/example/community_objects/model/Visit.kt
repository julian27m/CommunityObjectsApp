package com.example.community_objects.model

import com.google.firebase.Timestamp

class Visit(
    var category: String,
    var time: Timestamp,
    var user: String
) {
    constructor() : this("", Timestamp.now(), "")
}