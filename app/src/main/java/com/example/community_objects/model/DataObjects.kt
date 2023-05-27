package com.example.community_objects.model

open class Item(
    var id: String = "",
    var name: String = "",
    var category: String = "",
    var description: String = "",
    var imageURL: String = "",
    var user: String = ""
)

open class ItemRequest(
    var id: String = "",
    var name: String = "",
    var category: String = "",
    var description: String = "",
    var user: String = ""
)

class EPP(
    var degree: String = "",
    var type: String = ""
) : Item() {

    constructor(
        id: String = "",
        name: String = "",
        category: String = "",
        description: String = "",
        imageURL: String = "",
        degree: String = "",
        type: String = "",
        user: String = ""
    ) : this(degree, type) {
        this.id = id
        this.name = name
        this.category = category
        this.description = description
        this.imageURL = imageURL
        this.degree = degree
        this.type = type
        this.user = user
    }
}

class EPPRequest(
    var degree: String = "",
    var type: String = ""
) : ItemRequest() {

    constructor(
        id: String = "",
        name: String = "",
        category: String = "",
        description: String = "",
        degree: String = "",
        type: String = "",
        user: String = ""
    ) : this(degree, type) {
        this.id = id
        this.name = name
        this.category = category
        this.description = description
        this.degree = degree
        this.type = type
        this.user = user
    }
}

class Book(
    var id: String = "",
    var title: String = "",
    var category: String = "",
    var description: String = "",
    var imageURL: String = "",
    var user: String = "",
    var author: String = "",
    var subject: String = ""
)

class BookRequest(
    var id: String = "",
    var title: String = "",
    var category: String = "",
    var description: String = "",
    var user: String = "",
    var author: String = "",
    var subject: String = ""
)

class Clothes(
    var colors: String = "",
    var size: String = ""
) : Item() {
    constructor(
        id: String = "",
        name: String = "",
        category: String = "",
        description: String = "",
        imageURL: String = "",
        colors: String = "",
        size: String = "",
        user: String = ""
    ) : this(colors, size) {
        this.id = id
        this.name = name
        this.category = category
        this.description = description
        this.imageURL = imageURL
        this.colors = colors
        this.size = size
        this.user = user
    }
}

class ClothesRequest(
    var colors: String = "",
    var size: String = ""
) : ItemRequest() {
    constructor(
        id: String = "",
        name: String = "",
        category: String = "",
        description: String = "",
        colors: String = "",
        size: String = "",
        user: String = ""
    ) : this(colors, size) {
        this.id = id
        this.name = name
        this.category = category
        this.description = description
        this.colors = colors
        this.size = size
        this.user = user
    }
}

class Supplies(
    var id: String = "",
    var title: String = "",
    var category: String = "",
    var description: String = "",
    var imageURL: String = "",
    var reference: String = "",
    var user: String = ""
)

class SuppliesRequest(
    var id: String = "",
    var title: String = "",
    var category: String = "",
    var description: String = "",
    var user: String = "",
    var reference: String = ""
)
