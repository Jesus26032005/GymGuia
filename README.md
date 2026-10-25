# Gym Guía

App de Android para consultar ejercicios de gimnasio, registrar las series de cada día y ver el progreso. Funciona **100 % sin conexión**: no pide internet, no tiene cuentas y todo se guarda en el teléfono.

## Qué hace

- **Catálogo de 876 ejercicios en español**, con búsqueda y filtros por músculo, equipo y nivel.
- **Detalle de cada ejercicio**: fotos, instrucciones paso a paso, músculos trabajados en un mapa del cuerpo y, en unos 300 ejercicios, una demostración animada (GIF).
- **Favoritos** guardados en el teléfono.
- **Registro de series** del día: peso y repeticiones con botones grandes, marca de serie completada, y la última vez que hiciste ese ejercicio.
- **Temporizador de descanso** que sigue contando con la app en segundo plano y avisa con sonido, vibración y notificación.
- **Calendario y progreso**: días entrenados, racha, y gráficas de peso máximo y volumen por ejercicio, con récords personales.
- **Ajustes**: tema claro, oscuro o del sistema, unidades (kg o lb), descanso por defecto y borrado del historial.

## Identidad visual

El diseño se llama **Placa**: grafito, amarillo de seguridad y tiza, con números grandes en Barlow Condensed. Los kilos, las repeticiones y el tiempo son lo más grande de cada pantalla, porque son lo que se mira entre series. Está en modo oscuro y claro.

## Tecnología

| Parte | Con qué |
|---|---|
| Lenguaje e interfaz | Kotlin, Jetpack Compose, Material 3 |
| Arquitectura | MVVM con `StateFlow`; capas `data`, `domain` y `ui` |
| Base de datos | Room (con KSP), versión 4 con migraciones escritas a mano |
| Preferencias | DataStore |
| Imágenes y GIFs | Coil |
| Gráficas | Canvas de Compose, sin librerías |
| Temporizador | Servicio en primer plano; el tiempo se calcula con un reloj monotónico |
| Versiones | `minSdk` 26, `targetSdk` y `compileSdk` 35 |

Permisos: solo **vibración** y **notificaciones**.

## Cómo compilarla

Necesitas Android Studio reciente (o el SDK de Android y JDK 17) y un teléfono o emulador con Android 8.0 o superior.

```bash
./gradlew assembleDebug          # genera app/build/outputs/apk/debug/app-debug.apk
./gradlew testDebugUnitTest      # tests unitarios
./gradlew connectedDebugAndroidTest   # tests con emulador o teléfono conectado
```

Para instalarla en un dispositivo conectado:

```bash
./gradlew installDebug
```

## Estructura

```
app/src/main/java/com/gymguia/app/
  data/        Room, DataStore, repositorios y carga del catálogo
  domain/      modelos y reglas (racha, semana, límites de series)
  service/     servicio del temporizador de descanso
  ui/
    theme/       colores, tipografía, formas y espaciado
    components/  piezas reutilizables y mapa muscular
    screens/     Inicio, Ejercicios, Detalle, Favoritos, Progreso, Ajustes
    navigation/  rutas
app/src/main/assets/
  exercises_es.json   catálogo en español (fuente principal de la app)
  gifs/               demostraciones animadas
  musculos/           capas del mapa muscular
tools/               scripts para generar el catálogo y los GIFs (ver tools/README.md)
```

Reglas del proyecto: los textos van en `strings.xml`, los tamaños y colores salen del tema (no hay números mágicos en las pantallas), cada componente tiene `@Preview` en claro y oscuro, y los comentarios importantes están en español.

## Datos del catálogo

El catálogo se genera con los scripts de [`tools/`](tools/README.md): traducción al español de los ejercicios, emparejamiento con los GIFs y preparación del mapa muscular. El detalle de cómo regenerarlo está ahí.

Si cambias el contenido de `exercises_es.json` o de los GIFs, sube `VERSION_CATALOGO` en `CargadorCatalogo.kt`: cada teléfono recarga el catálogo al abrir la app **sin perder los favoritos ni el historial**.

## Créditos y licencias

- **Ejercicios, instrucciones y fotos:** [free-exercise-db](https://github.com/yuhonas/free-exercise-db) (dominio público), traducidas al español para esta app.
- **Mapa muscular:** diagramas de [wger](https://wger.de), Creative Commons BY-SA. El crédito aparece en la pantalla "Acerca de" de la app.
- **Fuentes:** Barlow y Barlow Condensed, licencia SIL OFL.
- **Demostraciones animadas (GIF):** de ExerciseDB. **Sus términos de uso no están confirmados**, así que se incluyen solo para uso personal. Antes de compartir o publicar la app hay que conseguir permiso por escrito o una licencia, o quitar la carpeta `app/src/main/assets/gifs/` y regenerar el catálogo (ver `tools/README.md`).

El código de la app no tiene una licencia definida todavía.
