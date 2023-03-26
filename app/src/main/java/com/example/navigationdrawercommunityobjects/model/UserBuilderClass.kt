package com.example.navigationdrawercommunityobjects.model

class UserBuilderClass {
    var name: String? = null
    var gender: String? = null
    var age: String? = null
    var email: String? = null
    var username: String? = null
    var password: String? = null

    constructor(name: String?, gender: String?, age: String?, email: String?, username: String?, password: String?) {
        this.name = name
        this.gender = gender
        this.age = age
        this.email = email
        this.username = username
        this.password = password
    }

    constructor() {}
}