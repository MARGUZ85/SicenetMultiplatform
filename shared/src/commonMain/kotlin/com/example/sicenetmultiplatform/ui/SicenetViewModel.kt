package com.example.sicenetmultiplatform.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.example.sicenetmultiplatform.data.SicenetRepository
import com.example.sicenetmultiplatform.data.SicenetLocalRepository
import com.example.sicenetmultiplatform.data.model.LoginResult
import com.example.sicenetmultiplatform.data.model.SicenetProfile
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope


sealed interface SicenetUiState {
    object Login : SicenetUiState
    object Loading : SicenetUiState
    data class Success(val profile: SicenetProfile) : SicenetUiState
    data class Error(val message: String) : SicenetUiState
}

enum class SicenetScreen {
    Login,
    Menu,
    Profile,
    Load,
    Cardex,
    GradesUnits,
    GradesFinal
}

enum class SyncState {
    IDLE, RUNNING, SUCCESS, ERROR
}

/**
 * El ViewModel es el "Cerebro" de la UI.
 * Aquí se gestiona el estado de las pantallas y se orquestan las corrutinas para la sincronización.
 */
class SicenetViewModel(
    private val repository: SicenetRepository,
    private val localRepository: SicenetLocalRepository
) : androidx.lifecycle.ViewModel() {

    private val _syncState = MutableStateFlow(SyncState.IDLE)
    val syncState = _syncState.asStateFlow()

    private val activeSyncs = MutableStateFlow(0)

    var sicenetUiState by mutableStateOf<SicenetUiState>(SicenetUiState.Login)
        private set

    var currentScreen by mutableStateOf(SicenetScreen.Login)
        private set

    fun navigateTo(screen: SicenetScreen) {
        currentScreen = screen
    }

    // Flujos de datos (Flows) que vienen de la base de datos local (Room).
    // Imagina un "Flow" como una tubería de agua viva conectada a la base de datos.
    // .stateIn convierte esta tubería en un "StateFlow" (un tanque de agua caliente) 
    // que la pantalla (UI) puede ver y reaccionar instantáneamente cuando el agua (los datos) cambian.
    val academicLoad = localRepository.academicLoad
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cardex = localRepository.cardex
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unitGrades = localRepository.unitGrades
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val finalGrades = localRepository.finalGrades
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lastUpdateLoad =
        localRepository.getLastUpdate("carga")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val lastUpdateCardex =
        localRepository.getLastUpdate("cardex")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val lastUpdateGradesUnits =
        localRepository.getLastUpdate("calif_unidades")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val lastUpdateGradesFinal =
        localRepository.getLastUpdate("calif_finales")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Inicia el proceso de sesión.
    fun iniciarSesion(user: String, pass: String) {
        // viewModelScope.launch lanza una corrutina (un hilo de trabajo ligero) amarrada a esta pantalla.
        // Si el usuario presiona "Atrás" y cierra la app, esta corrutina se cancela automáticamente 
        // para no gastar batería ni memoria a lo tonto.
        viewModelScope.launch {
            // Avisamos a la pantalla que ponga el circulito de "Cargando..."
            sicenetUiState = SicenetUiState.Loading

            // Vamos al Repositorio a intentar el login (esperamos la respuesta sin que la app se trabe)
            when (val result = repository.login(user, pass)) {

                is LoginResult.Success -> {
                    val profile = repository.getPerfil()

                    if (profile != null) {
                        sicenetUiState = SicenetUiState.Success(profile)
                        currentScreen = SicenetScreen.Menu
                        // Sincronización automática de todas las funciones al entrar.
                        syncAll()
                    } else {
                        sicenetUiState =
                            SicenetUiState.Error("Error al obtener el perfil")
                    }
                }

                is LoginResult.Error -> {
                    sicenetUiState =
                        SicenetUiState.Error(result.message)
                }
            }
        }
    }

    // Lanza un trabajo de sincronización para una característica específica.
    fun syncFeature(feature: String, tag: String) {
        viewModelScope.launch {
            activeSyncs.value = activeSyncs.value + 1
            _syncState.value = SyncState.RUNNING
            try {
                when (feature) {
                    "LOAD" -> {
                        val data = repository.getCargaAcademica()
                        if (data != null) localRepository.saveAcademicLoad(data)
                    }
                    "CARDEX" -> {
                        val data = repository.getCardex()
                        if (data != null) localRepository.saveCardex(data)
                    }
                    "GRADES_UNITS" -> {
                        val data = repository.getCalifUnidades()
                        if (data != null) localRepository.saveUnitGrades(data)
                    }
                    "GRADES_FINAL" -> {
                        val data = repository.getCalifFinales()
                        if (data != null) localRepository.saveFinalGrades(data)
                    }
                }
                activeSyncs.value = maxOf(0, activeSyncs.value - 1)
                if (activeSyncs.value == 0) {
                    _syncState.value = SyncState.SUCCESS
                }
            } catch (e: Exception) {
                e.printStackTrace()
                activeSyncs.value = maxOf(0, activeSyncs.value - 1)
                _syncState.value = SyncState.ERROR
            }
        }
    }

    private fun syncAll() {
        syncFeature("LOAD", "LOAD")
        syncFeature("CARDEX", "CARDEX")
        syncFeature("GRADES_UNITS", "GRADES_UNITS")
        syncFeature("GRADES_FINAL", "GRADES_FINAL")
    }


}


