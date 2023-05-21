package com.example.community_objects.model

class UserBuilderClass public constructor(
    val name: String?,
    val gender: String?,
    val age: String?,
    val email: String?,
    val username: String?,
    val password: String?,
    val donations: String?,
    val career: String?
) {
    class Builder {
        private var name: String? = null
        private var gender: String? = null
        private var age: String? = null
        private var email: String? = null
        private var username: String? = null
        private var password: String? = null
        private var donations: String? = null
        private var career: String? = null

        fun setName(name: String?): Builder {
            this.name = name
            return this
        }

        fun setGender(gender: String?): Builder {
            this.gender = gender
            return this
        }

        fun setAge(age: String?): Builder {
            this.age = age
            return this
        }

        fun setEmail(email: String?): Builder {
            this.email = email
            return this
        }

        fun setUsername(username: String?): Builder {
            this.username = username
            return this
        }

        fun setPassword(password: String?): Builder {
            this.password = password
            return this
        }

        fun setDonations(donations: String?): Builder {
            this.donations = donations
            return this
        }

        fun setCareer(career: String?): Builder {
            this.career = career
            return this
        }

        fun build(): UserBuilderClass {
            return UserBuilderClass(name, gender, age, email, username, password, donations, career)
        }
    }
}