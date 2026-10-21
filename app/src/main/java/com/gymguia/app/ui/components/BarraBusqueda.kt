package com.gymguia.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.gymguia.app.R
import com.gymguia.app.ui.theme.espaciado

/** Barra de búsqueda redondeada con ícono de lupa y botón para limpiar. */
@Composable
fun BarraBusqueda(
    consulta: String,
    onConsultaChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBuscar: (String) -> Unit = {},
    placeholder: String = stringResource(R.string.busqueda_placeholder),
) {
    TextField(
        value = consulta,
        onValueChange = onConsultaChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = MaterialTheme.espaciado.alturaBarraBusqueda),
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (consulta.isNotEmpty()) {
                IconButton(onClick = { onConsultaChange("") }) {
                    Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.accion_limpiar_busqueda))
                }
            }
        },
        singleLine = true,
        shape = MaterialTheme.shapes.extraLarge,
        // Botón "buscar" del teclado.
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onBuscar(consulta) }),
        // Sin línea inferior: se ve como una "píldora".
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )
}

@PreviewClaroOscuro
@Composable
private fun BarraBusquedaPreview() {
    VistaPrevia {
        BarraBusqueda(consulta = "", onConsultaChange = {}, modifier = Modifier.padding(MaterialTheme.espaciado.l))
    }
}
