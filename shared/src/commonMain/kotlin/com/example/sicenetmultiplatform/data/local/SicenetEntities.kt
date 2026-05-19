package com.example.sicenetmultiplatform.data.local

import kotlinx.serialization.Serializable

/**
 * [CAPA DE DATOS - ENTIDADES DE PERSISTENCIA]
 * Estas clases definen la estructura de las tablas en la base de datos local (Room).
 * Se marcan como @Serializable para permitir su transporte fácil si fuera necesario.
 */

// --- Tabla: Carga Académica ---
@Serializable
data class AcademicLoadEntity(
    val id: Int = 0,               // ID único generado automáticamente

    val materia: String = "",      // Nombre de la asignatura
    val grupo: String = "",        // Grupo asignado
    val profesor: String = "",     // Nombre del docente
    val lunes: String = "",        // Horarios por día
    val martes: String = "",
    val miercoles: String = "",
    val jueves: String = "",
    val viernes: String = "",
    val sabado: String = "",
    val domingo: String = "",
    val creditos: Int = 0,         // Valor en créditos
    val aula: String = "",         // Aula física o virtual
    val estadoMateria: String = "" // Estado (ej: "Cursando")
)


// --- Tabla: Kardex (Historial Académico) ---
@Serializable
data class CardexEntity(
    val id: Int = 0,

    val materia: String = "",
    val clave: String = "",        // Clave oficial de la materia
    val creditos: Int = 0,
    val calificacion: String = "", // Nota obtenida
    val evaluacion: String = "",   // Tipo de evaluación (ej: "Ordinaria")
    val semestre: Int = 0,         // Número de semestre en que se cursó
    val anio: Int = 0,             // Periodo/Año
    val observacion: String = ""   // Observaciones adicionales
)


// --- Tabla: Calificaciones por Unidad (Parciales) ---
@Serializable
data class UnitGradesEntity(
    val id: Int = 0,

    val materia: String = "",
    val profesor: String = "",     // Nombre del docente (Agregado)
    val u1: String = "",           // Calificaciones individuales del 1 al 13
    val u2: String = "",
    val u3: String = "",
    val u4: String = "",
    val u5: String = "",
    val u6: String = "",
    val u7: String = "",
    val u8: String = "",
    val u9: String = "",
    val u10: String = "",
    val u11: String = "",
    val u12: String = "",
    val u13: String = "",
    val act: String = "",          // Actitud o participación
    val pf: String = ""            // Promedio Final parcial
)


// --- Tabla: Calificaciones Finales ---
@Serializable
data class FinalGradesEntity(
    val id: Int = 0,

    val materia: String = "",
    val profesor: String = "",     // Nombre del docente (Agregado)
    val calif: String = "",
    val observacion: String = ""
)


// --- Tabla: Registro de Metadatos (Última Actualización) ---
@Serializable
data class LastUpdateEntity(
    val feature: String,           // Nombre de la sección (ej: "cardex")

    val timestamp: Long = 0L       // Fecha y hora en milisegundos de la última sync
)
