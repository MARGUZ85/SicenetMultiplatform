package com.example.sicenetmultiplatform.data

import com.example.sicenetmultiplatform.network.SicenetService

interface AppContainer {
    val sicenetRepository: SicenetRepository
    val sicenetLocalRepository: SicenetLocalRepository
    val sicenetService: SicenetService
}

expect class DefaultAppContainer() {
    val sicenetRepository: SicenetRepository
    val sicenetLocalRepository: SicenetLocalRepository
    val sicenetService: SicenetService
}

expect fun formatTimestamp(timestamp: Long): String
expect fun getCurrentTimeMillis(): Long