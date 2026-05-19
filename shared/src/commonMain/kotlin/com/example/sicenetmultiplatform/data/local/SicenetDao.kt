package com.example.sicenetmultiplatform.data.local

import kotlinx.coroutines.flow.Flow

interface SicenetDao {

    // --- CARGA ACADÉMICA ---

    fun getAcademicLoad(): Flow<List<AcademicLoadEntity>>
    suspend fun insertAcademicLoad(load: List<AcademicLoadEntity>)
    suspend fun clearAcademicLoad()


    // --- KARDEX (CARDEX) ---

    fun getCardex(): Flow<List<CardexEntity>>
    suspend fun insertCardex(cardex: List<CardexEntity>)
    suspend fun clearCardex()


    // --- CALIFICACIONES POR UNIDAD ---

    fun getUnitGrades(): Flow<List<UnitGradesEntity>>
    suspend fun insertUnitGrades(grades: List<UnitGradesEntity>)
    suspend fun clearUnitGrades()


    // --- CALIFICACIONES FINALES ---

    fun getFinalGrades(): Flow<List<FinalGradesEntity>>
    suspend fun insertFinalGrades(grades: List<FinalGradesEntity>)
    suspend fun clearFinalGrades()


    // --- METADATOS (ÚLTIMA ACTUALIZACIÓN) ---

    fun getLastUpdate(feature: String): Flow<Long?>
    suspend fun setLastUpdate(entity: LastUpdateEntity)
}
