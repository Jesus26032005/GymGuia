package com.gymguia.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gymguia.app.ui.GymGuiaApp
import com.gymguia.app.ui.screens.carga.CargaScreen
import com.gymguia.app.ui.screens.carga.CargaViewModel
import com.gymguia.app.ui.screens.carga.EstadoCarga
import com.gymguia.app.ui.theme.GymGuiaTheme

/** Única actividad de la app. Todo lo demás son pantallas de Compose. */
class MainActivity : ComponentActivity() {

    override fun onStart() {
        super.onStart()
        // Si el usuario vuelve a la app, ya no hace falta la notificación de "descanso terminado".
        (application as GymGuiaApplication).avisos.cancelarAvisoFin()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Dibuja detrás de las barras del sistema; cada pantalla agrega sus márgenes.
        enableEdgeToEdge()
        setContent {
            // Los ajustes viven en el repositorio de la app: un cambio se ve al instante en todas partes
            // (incluido el tema) y el aviso del temporizador lee de la misma fuente.
            val repositorioAjustes = (application as GymGuiaApplication).ajustes
            val ajustes by repositorioAjustes.ajustes.collectAsStateWithLifecycle()
            val carga: CargaViewModel = viewModel(factory = CargaViewModel.Fabrica)
            val estadoCarga by carga.estado.collectAsStateWithLifecycle()

            GymGuiaTheme(tema = ajustes.tema) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Hasta que el catálogo esté en la base no se crean las pantallas.
                    if (estadoCarga is EstadoCarga.Listo) {
                        GymGuiaApp(ajustes = ajustes, onAjustesChange = { nuevos -> repositorioAjustes.actualizar { nuevos } })
                    } else {
                        CargaScreen(estado = estadoCarga, onReintentar = carga::iniciar)
                    }
                }
            }
        }
    }
}
