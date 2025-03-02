package com.juligraph.listapp.data

import kotlinx.serialization.Serializable

@Serializable
data class UsersList(
    val users: List<User>,
    val total: Int,
    val skip: Int,
    val limit: Int
)