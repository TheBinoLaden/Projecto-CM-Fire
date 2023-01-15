package com.example.finalproject.utils

class UserUtils {


    companion object{
        /**
         * Handler for the username of the type email
         */
        fun handlingEmailUsername(username: String): String {

            //in the case of email
            return if ('@' in username) {
                username.substringBefore('@')
            } else username
        }
    }
}