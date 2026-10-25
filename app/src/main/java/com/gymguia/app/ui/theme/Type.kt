package com.gymguia.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.gymguia.app.R

/*
 * Escala tipográfica de la identidad "Placa".
 *
 * Dos familias incluidas en la app (licencia OFL, sin descargas):
 *  - Barlow Condensed: títulos, etiquetas y sobre todo los números. Es angosta, así los
 *    kilos y las repeticiones pueden ser muy grandes sin ocupar todo el ancho.
 *  - Barlow: texto corrido (instrucciones, descripciones).
 */
private val Condensada = FontFamily(
    Font(R.font.barlow_condensed_semibold, FontWeight.SemiBold),
    Font(R.font.barlow_condensed_bold, FontWeight.Bold),
    Font(R.font.barlow_condensed_extrabold, FontWeight.ExtraBold),
)

private val Texto = FontFamily(
    Font(R.font.barlow_regular, FontWeight.Normal),
    Font(R.font.barlow_medium, FontWeight.Medium),
    Font(R.font.barlow_semibold, FontWeight.SemiBold),
)

internal val Tipografia = Typography(
    // Números grandes: kilos y repeticiones al registrar, tiempo del temporizador y récords.
    displayLarge = TextStyle(fontFamily = Condensada, fontWeight = FontWeight.ExtraBold, fontSize = 96.sp, lineHeight = 88.sp),
    displayMedium = TextStyle(fontFamily = Condensada, fontWeight = FontWeight.ExtraBold, fontSize = 64.sp, lineHeight = 60.sp),
    displaySmall = TextStyle(fontFamily = Condensada, fontWeight = FontWeight.ExtraBold, fontSize = 44.sp, lineHeight = 46.sp),
    headlineLarge = TextStyle(fontFamily = Condensada, fontWeight = FontWeight.ExtraBold, fontSize = 44.sp, lineHeight = 42.sp),
    headlineMedium = TextStyle(fontFamily = Condensada, fontWeight = FontWeight.ExtraBold, fontSize = 34.sp, lineHeight = 36.sp),
    headlineSmall = TextStyle(fontFamily = Condensada, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 32.sp),
    titleLarge = TextStyle(fontFamily = Condensada, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 28.sp, letterSpacing = 0.2.sp),
    titleMedium = TextStyle(fontFamily = Condensada, fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 24.sp, letterSpacing = 0.3.sp),
    titleSmall = TextStyle(fontFamily = Condensada, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 22.sp, letterSpacing = 0.3.sp),
    bodyLarge = TextStyle(fontFamily = Texto, fontWeight = FontWeight.Normal, fontSize = 17.sp, lineHeight = 25.sp),
    bodyMedium = TextStyle(fontFamily = Texto, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 21.sp),
    bodySmall = TextStyle(fontFamily = Texto, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp),
    labelLarge = TextStyle(fontFamily = Condensada, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 22.sp, letterSpacing = 1.sp),
    labelMedium = TextStyle(fontFamily = Condensada, fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 18.sp, letterSpacing = 1.4.sp),
    labelSmall = TextStyle(fontFamily = Condensada, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 1.4.sp),
)
