package com.example.sicenetmultiplatform.data

import android.content.Context
import com.example.sicenetmultiplatform.data.local.RoomSicenetDaoWrapper
import com.example.sicenetmultiplatform.data.local.SicenetDatabase
import com.example.sicenetmultiplatform.network.SicenetService

actual class DefaultAppContainer actual constructor() : AppContainer {

    // Contexto de Android
    private var appContext: Context? = null

    // Inicializar contexto
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // Servicio de red
    actual override val sicenetService: SicenetService by lazy {
        SicenetService()
    }

    // Repositorio remoto
    actual override val sicenetRepository: SicenetRepository by lazy {
        SicenetRepository(sicenetService)
    }

    // Base de datos Room
    private val sicenetDatabase: SicenetDatabase by lazy {
        val context = appContext
            ?: throw IllegalStateException(
                "DefaultAppContainer no fue inicializado. Llama init(context) primero."
            )

        SicenetDatabase.getDatabase(context)
    }

    // Repositorio local
    actual override val sicenetLocalRepository: SicenetLocalRepository by lazy {
        SicenetLocalRepository(
            RoomSicenetDaoWrapper(
                sicenetDatabase.androidSicenetDao()
            )
        )
    }
}

actual fun formatTimestamp(timestamp: Long): String {
    return java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date(timestamp))
}

actual fun getCurrentTimeMillis(): Long {
    return System.currentTimeMillis()
}