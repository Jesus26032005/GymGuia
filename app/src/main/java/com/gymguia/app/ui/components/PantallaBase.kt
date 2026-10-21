package com.gymguia.app.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.gymguia.app.R
import com.gymguia.app.ui.theme.espaciado

/**
 * Estructura común de cada pantalla: barra superior con título, botón atrás opcional
 * y acciones. La barra inferior y el temporizador los pone el Scaffold principal,
 * por eso aquí no se agregan márgenes inferiores (contentWindowInsets = 0).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaBase(
    titulo: String,
    modifier: Modifier = Modifier,
    onAtras: (() -> Unit)? = null,
    acciones: @Composable RowScope.() -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    contenido: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(titulo, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    if (onAtras != null) {
                        IconButton(onClick = onAtras) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.accion_atras))
                        }
                    }
                },
                actions = acciones,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        },
        snackbarHost = snackbarHost,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
        content = contenido,
    )
}

@PreviewClaroOscuro
@Composable
private fun PantallaBasePreview() {
    VistaPrevia {
        PantallaBase(titulo = stringResource(R.string.app_nombre), onAtras = {}) { relleno ->
            Text(
                text = stringResource(R.string.ejemplo_contenido),
                modifier = Modifier
                    .padding(relleno)
                    .padding(MaterialTheme.espaciado.l),
            )
        }
    }
}
