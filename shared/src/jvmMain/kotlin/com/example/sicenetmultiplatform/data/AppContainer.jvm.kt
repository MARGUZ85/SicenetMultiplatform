package com.example.sicenetmultiplatform.data

import com.example.sicenetmultiplatform.data.local.AcademicLoadEntity
import com.example.sicenetmultiplatform.data.local.CardexEntity
import com.example.sicenetmultiplatform.data.local.FinalGradesEntity
import com.example.sicenetmultiplatform.data.local.LastUpdateEntity
import com.example.sicenetmultiplatform.data.local.SicenetDao
import com.example.sicenetmultiplatform.data.local.UnitGradesEntity
import com.example.sicenetmultiplatform.network.SicenetService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemorySicenetDao : SicenetDao {
    private val academicLoadFlow = MutableStateFlow<List<AcademicLoadEntity>>(emptyList())
    private val cardexFlow = MutableStateFlow<List<CardexEntity>>(emptyList())
    private val unitGradesFlow = MutableStateFlow<List<UnitGradesEntity>>(emptyList())
    private val finalGradesFlow = MutableStateFlow<List<FinalGradesEntity>>(emptyList())
    private val lastUpdateFlow = MutableStateFlow<Map<String, Long>>(emptyMap())

    override fun getAcademicLoad() = academicLoadFlow
    override suspend fun insertAcademicLoad(load: List<AcademicLoadEntity>) { academicLoadFlow.value = load }
    override suspend fun clearAcademicLoad() { academicLoadFlow.value = emptyList() }

    override fun getCardex() = cardexFlow
    override suspend fun insertCardex(cardex: List<CardexEntity>) { cardexFlow.value = cardex }
    override suspend fun clearCardex() { cardexFlow.value = emptyList() }

    override fun getUnitGrades() = unitGradesFlow
    override suspend fun insertUnitGrades(grades: List<UnitGradesEntity>) { unitGradesFlow.value = grades }
    override suspend fun clearUnitGrades() { unitGradesFlow.value = emptyList() }

    override fun getFinalGrades() = finalGradesFlow
    override suspend fun insertFinalGrades(grades: List<FinalGradesEntity>) { finalGradesFlow.value = grades }
    override suspend fun clearFinalGrades() { finalGradesFlow.value = emptyList() }

    override fun getLastUpdate(feature: String) = lastUpdateFlow.map { it[feature] }
    override suspend fun setLastUpdate(entity: LastUpdateEntity) {
        lastUpdateFlow.value = lastUpdateFlow.value + (entity.feature to entity.timestamp)
    }
}

actual class DefaultAppContainer actual constructor() : AppContainer {

    actual override val sicenetService: SicenetService by lazy {
        SicenetService()
    }

    actual override val sicenetRepository: SicenetRepository by lazy {
        SicenetRepository(sicenetService)
    }

    actual override val sicenetLocalRepository: SicenetLocalRepository by lazy {
        SicenetLocalRepository(InMemorySicenetDao())
    }
}

actual fun formatTimestamp(timestamp: Long): String {
    return java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date(timestamp))
}

actual fun getCurrentTimeMillis(): Long {
    return System.currentTimeMillis()
}
