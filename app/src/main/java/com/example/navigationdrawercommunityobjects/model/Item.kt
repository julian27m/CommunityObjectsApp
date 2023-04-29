package com.example.navigationdrawercommunityobjects.model

open class Item(
    var name: String = "",
    var category: String = "",
    var description: String = "",
    var imageURL: String = "",
    var user: String = ""
)

class EPP(
    var degree: String,
    var type: String
) : Item() {

    constructor(
        name: String = "",
        category: String = "",
        description: String = "",
        imageURL: String = "",
        degree: String = "",
        type: String = "",
        user: String = ""
    ) : this(degree, type) {
        this.name = name
        this.category = category
        this.description = description
        this.imageURL = imageURL
        this.degree = degree
        this.type = type
        this.user = user
    }

    constructor() : this("", "")
}

class Book(
    var title: String = "",
    var category: String = "",
    var description: String = "",
    var imageURL: String = "",
    var user: String = "",
    var author: String = "",
    var subject: String = ""
)

class Clothes(
    var colors: String,
    var size: String,
) : Item() {
    constructor(
        name: String = "",
        category: String = "",
        description: String = "",
        imageURL: String = "",
        colors: String = "",
        size: String = "",
        user: String = ""
    ) : this(colors, size) {
        this.name = name
        this.category = category
        this.description = description
        this.imageURL = imageURL
        this.colors = colors
        this.size = size
        this.user = user
    }

    constructor() : this("", "")
}

class Supplies(
    var title: String = "",
    var category: String = "",
    var description: String = "",
    var imageURL: String = "",
    var reference: String = "",
    var user: String = ""
)


