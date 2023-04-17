package com.example.navigationdrawercommunityobjects.model

open class Item(
    var name: String = "",
    var category: String = "",
    var description: String = "",
    var photo: String = "",
    var id: String = ""
)

class EPP(
    private var degree: String,
    private var type: String
) : Item() {

    constructor(
        name: String = "",
        category: String = "",
        description: String = "",
        photo: String = "",
        id: String = "",
        degree: String = "",
        type: String = ""
    ) : this(degree, type) {
        this.name = name
        this.category = category
        this.description = description
        this.photo = photo
        this.id = id
        this.degree = degree
        this.type = type
    }
}

class Book(
    private var author: String,
    private var subject: String,
) : Item() {

        constructor(
            name: String = "",
            category: String = "",
            description: String = "",
            photo: String = "",
            id: String = "",
            author: String = "",
            subject: String = ""
        ) : this(author, subject) {
            this.name = name
            this.category = category
            this.description = description
            this.photo = photo
            this.id = id
            this.author = author
            this.subject = subject
        }
}

class Clothes(
    private var colors
) : Item()

class Supplies(
    val reference: String = "",
) : Item()
