package com.example.navigationdrawercommunityobjects.model

data class Item (
    val name: String = "",
    val categories: List<String> = emptyList(),
    val description: String = "",
    val photos: List<String> = emptyList()
)