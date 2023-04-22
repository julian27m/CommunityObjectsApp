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
        photo: String = "",
        id: String = "",
        colors: String = "",
        size: String = ""
    ) : this(colors, size) {
        this.name = name
        this.category = category
        this.description = description
        this.photo = photo
        this.id = id
        this.colors = colors
        this.size = size
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
        photo: String = "",
        id: String = "",
        reference: String = ""
    ) : this(reference) {
        this.name = name
        this.category = category
        this.description = description
        this.photo = photo
        this.id = id
        this.reference = reference
    }

    constructor() : this("")
}
