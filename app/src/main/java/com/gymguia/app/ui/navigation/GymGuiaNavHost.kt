package com.gymguia.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gymguia.app.R
import com.gymguia.app.domain.model.Ajustes
import com.gymguia.app.ui.components.EstadoVacio
import com.gymguia.app.ui.components.PantallaBase
import com.gymguia.app.ui.screens.acerca.AcercaDeScreen
import com.gymguia.app.ui.screens.ajustes.AjustesScreen
import com.gymguia.app.ui.screens.ajustes.AjustesViewModel
import com.gymguia.app.ui.screens.detalle.DetalleEjercicioScreen
import com.gymguia.app.ui.screens.detalle.DetalleViewModel
import com.gymguia.app.ui.screens.detalle.EstadoDetalle
import com.gymguia.app.ui.screens.ejercicios.ListaEjerciciosScreen
import com.gymguia.app.ui.screens.ejercicios.ListaEjerciciosViewModel
import com.gymguia.app.ui.screens.favoritos.FavoritosScreen
import com.gymguia.app.ui.screens.favoritos.FavoritosViewModel
import com.gymguia.app.ui.screens.inicio.InicioScreen
import com.gymguia.app.ui.screens.inicio.InicioViewModel
import com.gymguia.app.ui.screens.progreso.ProgresoScreen
import com.gymguia.app.ui.screens.progreso.ProgresoViewModel

/**
 * Grafo de navegación. Cada "composable(ruta)" es una pantalla y crea su ViewModel,
 * que entrega los datos reales de Room como StateFlow.
 * El calendario y el progreso siguen con datos de ejemplo hasta la Fase 6.
 */
@Composable
fun GymGuiaNavHost(
    navController: NavHostController,
    ajustes: Ajustes,
    onAjustesChange: (Ajustes) -> Unit,
    onIniciarDescanso: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val abrirDetalle: (String) -> Unit = { navController.navigate(Rutas.detalle(it)) }

    NavHost(navController = navController, startDestination = Rutas.INICIO, modifier = modifier) {
        composable(Rutas.INICIO) {
            val vm: InicioViewModel = viewModel(factory = InicioViewModel.Fabrica)
            val musculos by vm.musculos.collectAsStateWithLifecycle()
            val sugeridos by vm.sugeridos.collectAsStateWithLifecycle()
            val favoritos by vm.favoritos.collectAsStateWithLifecycle()
            InicioScreen(
                musculos = musculos,
                sugeridos = sugeridos,
                favoritos = favoritos,
                onBuscar = { navController.navigate(Rutas.ejercicios(consulta = it)) },
                onMusculo = { navController.navigate(Rutas.ejercicios(musculo = it)) },
                onFavoritos = { navController.navigate(Rutas.FAVORITOS) },
                onEjercicio = abrirDetalle,
                onAlternarFavorito = vm::alternarFavorito,
            )
        }

        composable(
            route = Rutas.EJERCICIOS_PATRON,
            arguments = listOf(
                navArgument(Rutas.ARG_MUSCULO) { type = NavType.StringType; nullable = true; defaultValue = null },
                navArgument(Rutas.ARG_CONSULTA) { type = NavType.StringType; nullable = true; defaultValue = null },
            ),
        ) {
            // El ViewModel lee los argumentos (músculo y texto iniciales) de su SavedStateHandle.
            val vm: ListaEjerciciosViewModel = viewModel(factory = ListaEjerciciosViewModel.Fabrica)
            val filtros by vm.filtros.collectAsStateWithLifecycle()
            val resultados by vm.resultados.collectAsStateWithLifecycle()
            val favoritos by vm.favoritos.collectAsStateWithLifecycle()
            val musculos by vm.musculos.collectAsStateWithLifecycle()
            val equipos by vm.equipos.collectAsStateWithLifecycle()
            ListaEjerciciosScreen(
                filtros = filtros,
                resultados = resultados,
                favoritos = favoritos,
                musculos = musculos,
                equipos = equipos,
                onConsultaChange = vm::cambiarConsulta,
                onMusculo = vm::alternarMusculo,
                onEquipo = vm::alternarEquipo,
                onNivel = vm::alternarNivel,
                onLimpiarFiltros = vm::limpiarFiltros,
                onEjercicio = abrirDetalle,
                onAlternarFavorito = vm::alternarFavorito,
            )
        }

        composable(Rutas.FAVORITOS) {
            val vm: FavoritosViewModel = viewModel(factory = FavoritosViewModel.Fabrica)
            val favoritos by vm.favoritos.collectAsStateWithLifecycle()
            FavoritosScreen(
                favoritos = favoritos,
                onAtras = { navController.popBackStack() },
                onEjercicio = abrirDetalle,
                onAlternarFavorito = vm::quitarFavorito,
            )
        }

        composable(
            route = Rutas.DETALLE_PATRON,
            arguments = listOf(navArgument(Rutas.ARG_ID) { type = NavType.StringType }),
        ) {
            val vm: DetalleViewModel = viewModel(factory = DetalleViewModel.Fabrica)
            val detalle by vm.detalle.collectAsStateWithLifecycle()
            val esFavorito by vm.esFavorito.collectAsStateWithLifecycle()
            val series by vm.seriesDeHoy.collectAsStateWithLifecycle()
            val ultimaVez by vm.ultimaVez.collectAsStateWithLifecycle()
            val atras: () -> Unit = { navController.popBackStack() }

            when (val estado = detalle) {
                EstadoDetalle.Cargando -> PantallaBase(titulo = "", onAtras = atras) { relleno ->
                    Box(Modifier.padding(relleno), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                }
                EstadoDetalle.NoEncontrado -> PantallaBase(titulo = "", onAtras = atras) { relleno ->
                    EstadoVacio(
                        icono = Icons.Outlined.SearchOff,
                        titulo = stringResource(R.string.detalle_no_encontrado_titulo),
                        mensaje = stringResource(R.string.detalle_no_encontrado_mensaje),
                        modifier = Modifier.padding(relleno),
                    )
                }
                is EstadoDetalle.Listo -> DetalleEjercicioScreen(
                    ejercicio = estado.ejercicio,
                    esFavorito = esFavorito,
                    unidad = ajustes.unidad,
                    descansoSegundos = ajustes.descansoSegundos,
                    series = series,
                    ultimaVez = ultimaVez,
                    onAgregarSerie = vm::agregarSerie,
                    onActualizarSerie = vm::actualizarSerie,
                    onEliminarSerie = vm::eliminarSerie,
                    onAtras = atras,
                    onAlternarFavorito = vm::alternarFavorito,
                    onIniciarDescanso = onIniciarDescanso,
                )
            }
        }

        composable(Rutas.PROGRESO) {
            val vm: ProgresoViewModel = viewModel(factory = ProgresoViewModel.Fabrica)
            val calendario by vm.calendario.collectAsStateWithLifecycle()
            val graficas by vm.graficas.collectAsStateWithLifecycle()
            ProgresoScreen(
                calendario = calendario,
                graficas = graficas,
                unidad = ajustes.unidad,
                onCambiarMes = vm::cambiarMes,
                onDia = vm::seleccionarDia,
                onActualizarSerie = vm::actualizarSerie,
                onEliminarSerie = vm::eliminarSerie,
                onEjercicio = vm::elegirEjercicio,
                onRango = vm::cambiarRango,
            )
        }

        composable(Rutas.AJUSTES) {
            val vm: AjustesViewModel = viewModel(factory = AjustesViewModel.Fabrica)
            AjustesScreen(
                ajustes = ajustes,
                onAjustesChange = onAjustesChange,
                onBorrarHistorial = vm::borrarHistorial,
                onAcercaDe = { navController.navigate(Rutas.ACERCA_DE) },
            )
        }

        composable(Rutas.ACERCA_DE) {
            AcercaDeScreen(onAtras = { navController.popBackStack() })
        }
    }
}
