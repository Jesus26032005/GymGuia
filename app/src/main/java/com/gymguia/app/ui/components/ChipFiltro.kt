package com.gymguia.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gymguia.app.R
import com.gymguia.app.ui.theme.espaciado

/** Chip seleccionable para filtros. Muestra una palomita cuando está activo. */
@Composable
fun ChipFiltro(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = { Text(texto) },
        modifier = modifier,
        leadingIcon = if (seleccionado) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                )
            }
        } else {
            null
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
    )
}

@PreviewClaroOscuro
@Composable
private fun ChipFiltroPreview() {
    VistaPrevia {
        Row(
            modifier = Modifier.padding(MaterialTheme.espaciado.l),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.espaciado.s),
        ) {
            ChipFiltro(texto = stringResource(R.string.filtro_musculo), seleccionado = true, onClick = {})
            ChipFiltro(texto = stringResource(R.string.filtro_equipo), seleccionado = false, onClick = {})
        }
    }
}
