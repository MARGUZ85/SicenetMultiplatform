package com.example.sicenetmultiplatform.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Entity(tableName = "academic_load")
data class AndroidAcademicLoadEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val materia: String, val grupo: String, val profesor: String,
    val lunes: String, val martes: String, val miercoles: String,
    val jueves: String, val viernes: String, val sabado: String,
    val domingo: String, val creditos: Int, val aula: String,
    val estadoMateria: String
) {
    fun toCommon() = AcademicLoadEntity(id, materia, grupo, profesor, lunes, martes, miercoles, jueves, viernes, sabado, domingo, creditos, aula, estadoMateria)
}
fun AcademicLoadEntity.toAndroid() = AndroidAcademicLoadEntity(id, materia, grupo, profesor, lunes, martes, miercoles, jueves, viernes, sabado, domingo, creditos, aula, estadoMateria)

@Entity(tableName = "cardex")
data class AndroidCardexEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val materia: String, val clave: String, val creditos: Int,
    val calificacion: String, val evaluacion: String, val semestre: Int,
    val anio: Int, val observacion: String
) {
    fun toCommon() = CardexEntity(id, materia, clave, creditos, calificacion, evaluacion, semestre, anio, observacion)
}
fun CardexEntity.toAndroid() = AndroidCardexEntity(id, materia, clave, creditos, calificacion, evaluacion, semestre, anio, observacion)

@Entity(tableName = "unit_grades")
data class AndroidUnitGradesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val materia: String, val profesor: String, val u1: String, val u2: String,
    val u3: String, val u4: String, val u5: String, val u6: String,
    val u7: String, val u8: String, val u9: String, val u10: String,
    val u11: String, val u12: String, val u13: String, val act: String, val pf: String
) {
    fun toCommon() = UnitGradesEntity(id, materia, profesor, u1, u2, u3, u4, u5, u6, u7, u8, u9, u10, u11, u12, u13, act, pf)
}
fun UnitGradesEntity.toAndroid() = AndroidUnitGradesEntity(id, materia, profesor, u1, u2, u3, u4, u5, u6, u7, u8, u9, u10, u11, u12, u13, act, pf)

@Entity(tableName = "final_grades")
data class AndroidFinalGradesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val materia: String, val profesor: String, val calif: String, val observacion: String
) {
    fun toCommon() = FinalGradesEntity(id, materia, profesor, calif, observacion)
}
fun FinalGradesEntity.toAndroid() = AndroidFinalGradesEntity(id, materia, profesor, calif, observacion)

@Entity(tableName = "last_update_log")
data class AndroidLastUpdateEntity(
    @PrimaryKey val feature: String, val timestamp: Long
) {
    fun toCommon() = LastUpdateEntity(feature, timestamp)
}
fun LastUpdateEntity.toAndroid() = AndroidLastUpdateEntity(feature, timestamp)

@Dao
interface AndroidSicenetDao {
    @Query("SELECT * FROM academic_load")
    fun getAcademicLoad(): Flow<List<AndroidAcademicLoadEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAcademicLoad(load: List<AndroidAcademicLoadEntity>)
    @Query("DELETE FROM academic_load")
    suspend fun clearAcademicLoad()

    @Query("SELECT * FROM cardex ORDER BY semestre DESC")
    fun getCardex(): Flow<List<AndroidCardexEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardex(cardex: List<AndroidCardexEntity>)
    @Query("DELETE FROM cardex")
    suspend fun clearCardex()

    @Query("SELECT * FROM unit_grades")
    fun getUnitGrades(): Flow<List<AndroidUnitGradesEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnitGrades(grades: List<AndroidUnitGradesEntity>)
    @Query("DELETE FROM unit_grades")
    suspend fun clearUnitGrades()

    @Query("SELECT * FROM final_grades")
    fun getFinalGrades(): Flow<List<AndroidFinalGradesEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinalGrades(grades: List<AndroidFinalGradesEntity>)
    @Query("DELETE FROM final_grades")
    suspend fun clearFinalGrades()

    @Query("SELECT timestamp FROM last_update_log WHERE feature = :feature LIMIT 1")
    fun getLastUpdate(feature: String): Flow<Long?>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setLastUpdate(entity: AndroidLastUpdateEntity)
}

class RoomSicenetDaoWrapper(private val dao: AndroidSicenetDao) : SicenetDao {
    override fun getAcademicLoad() = dao.getAcademicLoad().map { list -> list.map { it.toCommon() } }
    override suspend fun insertAcademicLoad(load: List<AcademicLoadEntity>) = dao.insertAcademicLoad(load.map { it.toAndroid() })
    override suspend fun clearAcademicLoad() = dao.clearAcademicLoad()

    override fun getCardex() = dao.getCardex().map { list -> list.map { it.toCommon() } }
    override suspend fun insertCardex(cardex: List<CardexEntity>) = dao.insertCardex(cardex.map { it.toAndroid() })
    override suspend fun clearCardex() = dao.clearCardex()

    override fun getUnitGrades() = dao.getUnitGrades().map { list -> list.map { it.toCommon() } }
    override suspend fun insertUnitGrades(grades: List<UnitGradesEntity>) = dao.insertUnitGrades(grades.map { it.toAndroid() })
    override suspend fun clearUnitGrades() = dao.clearUnitGrades()

    override fun getFinalGrades() = dao.getFinalGrades().map { list -> list.map { it.toCommon() } }
    override suspend fun insertFinalGrades(grades: List<FinalGradesEntity>) = dao.insertFinalGrades(grades.map { it.toAndroid() })
    override suspend fun clearFinalGrades() = dao.clearFinalGrades()

    override fun getLastUpdate(feature: String) = dao.getLastUpdate(feature)
    override suspend fun setLastUpdate(entity: LastUpdateEntity) = dao.setLastUpdate(entity.toAndroid())
}
