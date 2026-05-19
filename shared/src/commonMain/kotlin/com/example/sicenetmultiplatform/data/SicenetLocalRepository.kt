package com.example.sicenetmultiplatform.data

import com.example.sicenetmultiplatform.data.local.AcademicLoadEntity
import com.example.sicenetmultiplatform.data.local.CardexEntity
import com.example.sicenetmultiplatform.data.local.FinalGradesEntity
import com.example.sicenetmultiplatform.data.local.LastUpdateEntity
import com.example.sicenetmultiplatform.data.local.SicenetDao
import com.example.sicenetmultiplatform.data.local.UnitGradesEntity
import kotlinx.coroutines.flow.Flow
class SicenetLocalRepository(private val sicenetDao: SicenetDao) {

    // Academic Load
    val academicLoad: Flow<List<AcademicLoadEntity>> = sicenetDao.getAcademicLoad()

    suspend fun saveAcademicLoad(load: List<AcademicLoadEntity>) {
        sicenetDao.clearAcademicLoad()
        sicenetDao.insertAcademicLoad(load)
        sicenetDao.setLastUpdate(LastUpdateEntity("carga", getCurrentTimeMillis()))
    }

    // Cardex
    val cardex: Flow<List<CardexEntity>> = sicenetDao.getCardex()

    suspend fun saveCardex(data: List<CardexEntity>) {
        sicenetDao.clearCardex()
        sicenetDao.insertCardex(data)
        sicenetDao.setLastUpdate(LastUpdateEntity("cardex", getCurrentTimeMillis()))
    }

    // Unit Grades
    val unitGrades: Flow<List<UnitGradesEntity>> = sicenetDao.getUnitGrades()

    suspend fun saveUnitGrades(grades: List<UnitGradesEntity>) {
        sicenetDao.clearUnitGrades()
        sicenetDao.insertUnitGrades(grades)
        sicenetDao.setLastUpdate(LastUpdateEntity("calif_unidades", getCurrentTimeMillis()))
    }

    // Final Grades
    val finalGrades: Flow<List<FinalGradesEntity>> = sicenetDao.getFinalGrades()

    suspend fun saveFinalGrades(grades: List<FinalGradesEntity>) {
        sicenetDao.clearFinalGrades()
        sicenetDao.insertFinalGrades(grades)
        sicenetDao.setLastUpdate(LastUpdateEntity("calif_finales", getCurrentTimeMillis()))
    }

    // Metadata
    fun getLastUpdate(feature: String): Flow<Long?> = sicenetDao.getLastUpdate(feature)
}
