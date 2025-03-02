package com.juligraph.listapp.network

import com.juligraph.listapp.data.User
import com.juligraph.listapp.data.UsersList
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json

class KtorClient {
    private val client = HttpClient(OkHttp) {
        defaultRequest {
            url("https://dummyjson.com/user/")
        }

        install(Logging) {
            logger = Logger.SIMPLE
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    // Lista cacheada de usuarios
    private var cachedUsers: List<User>? = null
    private val mutex = Mutex() // Para evitar condiciones de carrera en concurrencia

    // Cargar la lista de usuarios solo una vez
    private suspend fun loadUsers() {
        mutex.withLock {
            if (cachedUsers == null) { // Solo si aún no ha sido cargada
                cachedUsers = client.get("").body<UsersList>().users
            }
        }
    }

    // Obtener la lista desde cache
    suspend fun getUsers(): List<User> {
        if (cachedUsers == null) loadUsers()
        return cachedUsers ?: emptyList() // Si por alguna razón sigue siendo null, devolvemos lista vacía
    }

    // Buscar usuario en cache antes de llamar a la API
    suspend fun getUser(id: Int): User? {
        cachedUsers?.let { users ->
            return users.find { it.id == id }
        }

        // Si aún no está en cache, cargamos la lista y volvemos a buscar
        loadUsers()
        return cachedUsers?.find { it.id == id }
    }
}