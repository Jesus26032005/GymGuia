package com.gymguia.app.ui.components

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gymguia.app.ui.theme.GymGuiaTheme

/**
 * Anotación múltiple: un solo @PreviewClaroOscuro genera dos vistas previas,
 * una en modo claro y otra en modo oscuro.
 */
@Preview(name = "Claro", showBackground = true)
@Preview(name = "Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class PreviewClaroOscuro

/** Igual que la anterior, con tamaño de teléfono para pantallas completas. */
@Preview(name = "Claro", showBackground = true, device = "id:pixel_7")
@Preview(name = "Oscuro", showBackground = true, device = "id:pixel_7", uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class PreviewPantalla

/** Envuelve el contenido de una vista previa con el tema y el fondo de la app. */
@Composable
fun VistaPrevia(content: @Composable () -> Unit) {
    GymGuiaTheme {
        Surface(color = MaterialTheme.colorScheme.background, content = content)
    }
}
