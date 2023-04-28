package com.example.navigationdrawercommunityobjects.model

open class Item(
    var name: String = "",
    var category: String = "",
    var description: String = "",
    var imageURL: String = "",
    var user: String = ""
)

class EPP(
    private var degree: String,
    private var type: String
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
    private var author: String,
    private var subject: String,
) : Item() {

        constructor(
            name: String = "",
            category: String = "",
            description: String = "",
            imageURL: String = "",
            author: String = "",
            subject: String = "",
            user: String = ""
        ) : this(author, subject) {
            this.name = name
            this.category = category
            this.description = description
            this.imageURL = imageURL
            this.author = author
            this.subject = subject
            this.user = user
        }
    constructor() : this("", "")
}

class Clothes(
    private var colors: String,
    private var size: String,
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
    private var reference: String
) : Item() {
    constructor(
        name: String = "",
        category: String = "",
        description: String = "",
        imageURL: String = "",
        reference: String = "",
        user: String = ""
    ) : this(reference) {
        this.name = name
        this.category = category
        this.description = description
        this.imageURL = imageURL
        this.reference = reference
        this.user = user
    }

    constructor() : this("")
}
