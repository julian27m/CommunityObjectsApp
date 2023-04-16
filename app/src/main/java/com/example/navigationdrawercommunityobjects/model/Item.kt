package com.example.navigationdrawercommunityobjects.model

import javax.security.auth.Subject
import kotlin.text.Typography.degree

open class Item(
    val name: String = "",
    val category: String = "",
    val description: String = "",
    val photo: String = "",
    val id: String = ""
)

class EPP(
    val degree : String = "",
    val type : String = "") : Item()

class Book(
    val author : String = "",
    val subject: String = "",
) : Item()

class Clothes(
    val colors : String = "",
    val size : String = ""
) : Item()

class Supplies(
    val reference : String = "",
) : Item()
