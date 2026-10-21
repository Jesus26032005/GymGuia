package com.gymguia.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gymguia.app.R
import com.gymguia.app.domain.model.Ajustes
import com.gymguia.app.ui.components.TarjetaTemporizador
import com.gymguia.app.ui.navigation.DestinoInferior
import com.gymguia.app.ui.navigation.GymGuiaNavHost
import com.gymguia.app.ui.navigation.Rutas
import com.gymguia.app.ui.temporizador.TemporizadorViewModel
import com.gymguia.app.ui.theme.espaciado

/**
 * Raíz de la interfaz: barra inferior, tarjeta flotante del temporizador y navegación.
 * Recibe los ajustes desde MainActivity (en la Fase 7 vendrán de DataStore).
 */
@Composable
fun GymGuiaApp(
    ajustes: Ajustes,
    onAjustesChange: (Ajustes) -> Unit,
) {
    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()
    val temporizador: TemporizadorViewModel = viewModel(factory = TemporizadorViewModel.Fabrica)
    val estadoTemporizador by temporizador.estado.collectAsStateWithLifecycle()
    val explicarPermiso by temporizador.explicarPermiso.collectAsStateWithLifecycle()

    // Pide el permiso de notificaciones al sistema (solo Android 13+, y solo después de explicarlo).
    val pedirPermiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }
    if (explicarPermiso) {
        AlertDialog(
            onDismissRequest = temporizador::explicacionAtendida,
            title = { Text(stringResource(R.string.permiso_notificaciones_titulo)) },
            text = { Text(stringResource(R.string.permiso_notificaciones_mensaje)) },
            confirmButton = {
                TextButton(onClick = {
                    temporizador.explicacionAtendida()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        pedirPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }) { Text(stringResource(R.string.accion_permitir)) }
            },
            dismissButton = {
                TextButton(onClick = temporizador::explicacionAtendida) { Text(stringResource(R.string.accion_ahora_no)) }
            },
        )
    }

    Scaffold(
        // Las pantallas manejan su propio margen superior (TopAppBar).
        contentWindowInsets = WindowInsets(0),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Column {
                // La tarjeta flota sobre la barra inferior mientras haya un descanso activo.
                AnimatedVisibility(
                    visible = estadoTemporizador != null,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut(),
                ) {
                    estadoTemporizador?.let { estado ->
                        TarjetaTemporizador(
                            estado = estado,
                            onPausarReanudar = temporizador::pausarReanudar,
                            onSumar = temporizador::sumar,
                            onRestar = temporizador::restar,
                            onOmitir = temporizador::omitir,
                            modifier = Modifier.padding(
                                horizontal = MaterialTheme.espaciado.margenPantalla,
                                vertical = MaterialTheme.espaciado.s,
                            ),
                        )
                    }
                }
                NavigationBar {
                    DestinoInferior.entries.forEach { destino ->
                        // Una pestaña está activa si la ruta actual (sin parámetros) coincide.
                        // "Acerca de" se abre desde Ajustes, así que la pestaña de Ajustes sigue marcada.
                        val activa = entradaActual?.destination?.hierarchy?.any {
                            val ruta = it.route?.substringBefore('?')
                            ruta == destino.ruta || (destino == DestinoInferior.AJUSTES && ruta == Rutas.ACERCA_DE)
                        } == true
                        NavigationBarItem(
                            selected = activa,
                            onClick = {
                                navController.navigate(destino.ruta) {
                                    // Evita apilar pantallas repetidas y conserva el estado de cada pestaña.
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (activa) destino.iconoActivo else destino.iconoInactivo,
                                    contentDescription = null,
                                )
                            },
                            label = { Text(stringResource(destino.etiqueta)) },
                        )
                    }
                }
            }
        },
    ) { relleno ->
        GymGuiaNavHost(
            navController = navController,
            ajustes = ajustes,
            onAjustesChange = onAjustesChange,
            onIniciarDescanso = temporizador::iniciarDescanso,
            modifier = Modifier.padding(relleno),
        )
    }
}

// Sin @Preview: esta raíz crea ViewModels que necesitan la aplicación y la base de datos.
// Cada pantalla y componente que contiene sí tiene su vista previa en claro y oscuro.
