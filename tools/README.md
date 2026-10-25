# Traducción del catálogo al español

Este directorio genera `app/src/main/assets/exercises_es.json` a partir del catálogo original
(`datos/exercises.json`, de [free-exercise-db](https://github.com/yuhonas/free-exercise-db)).
La app lee ese archivo como fuente principal. El `id` de cada ejercicio no cambia y el nombre en
inglés se conserva en el campo `nameOriginal`, que la búsqueda también usa.

## Cómo está armada la traducción

| Campo | Cómo se traduce |
|---|---|
| Músculos, equipo, nivel, categoría, fuerza, mecánica | Diccionario fijo en `glosario.py` (ej. `chest` = pecho, `dumbbell` = mancuerna). |
| Nombres | Tabla revisada a mano: `datos/nombres_es.txt`, una línea `Original\|Traducción` por ejercicio. |
| Instrucciones | Traducción automática local (argostranslate) con el glosario aplicado antes y después. |

Antes de traducir, los términos de gimnasio (`barbell`, `kettlebell`, `shoulder blades`, `power rack`…)
se cambian por marcadores que el traductor no toca; después se restauran en español con el género
correcto (`la barra`, no `el barra`). Las órdenes se unifican a la forma "tú" y se corrigen errores
frecuentes del traductor (lista `POST_REEMPLAZOS`).

## Cómo ejecutarlo

Requiere Python 3.10 o superior. Todo el trabajo ocurre en esta carpeta.

```bash
cd tools
python3 -m venv .venv
.venv/bin/python -m pip install argostranslate
.venv/bin/python traducir_ejercicios.py --instalar-modelo   # una sola vez, descarga ~100 MB
.venv/bin/python traducir_ejercicios.py                     # traduce y genera exercises_es.json
```

El script procesa por lotes (`--lote 25` por defecto) y guarda el avance después de cada uno en
`progreso/traducciones.json`. **Si se interrumpe, vuelve a ejecutarlo: continúa donde se quedó.**
Una traducción completa tarda alrededor de media hora; la traducción cruda queda guardada y se
versiona, así que no hace falta repetirla para ajustar el resultado.

## Cómo revisar y corregir a mano

1. Ejecuta el script. Además de `exercises_es.json`, crea `progreso/revision.tsv` con una fila por
   nombre y por paso: `id`, campo, texto original y traducción. Ábrelo en una hoja de cálculo
   y ordénalo o fíltralo (por ejemplo, busca palabras en inglés que hayan quedado sin traducir).
2. Hay tres formas de corregir, de la más general a la más puntual:
   - **Un término que se repite** (siempre sale mal): agrégalo o cámbialo en `glosario.py`
     (`TERMINOS` para protegerlo antes de traducir, o `POST_REEMPLAZOS` para corregir la salida).
   - **Un ejercicio concreto**: agrega su `id` a `datos/correcciones_manuales.json`:

     ```json
     {
       "Barbell_Squat": {
         "name": "Sentadilla con barra",
         "instructions": ["Paso 1 corregido.", "Paso 2 corregido."]
       }
     }
     ```

     Puede incluir cualquier campo del ejercicio. Estas correcciones siempre ganan y sobreviven
     a nuevas ejecuciones.
   - **Un nombre**: edita la línea correspondiente en `datos/nombres_es.txt`.
3. Regenera el archivo **sin volver a traducir** (tarda un segundo):

   ```bash
   .venv/bin/python traducir_ejercicios.py --solo-ensamblar
   ```

4. Si cambias el contenido de `exercises_es.json` y ya hay instalaciones de la app, sube
   `VERSION_CATALOGO` en `CargadorCatalogo.kt`: así cada teléfono recarga el catálogo al abrir la app
   sin perder los favoritos.

## Archivos

- `traducir_ejercicios.py`: el script.
- `glosario.py`: diccionarios fijos, términos protegidos y correcciones.
- `datos/exercises.json`: catálogo original (no se modifica).
- `datos/nombres_es.txt`: nombres traducidos a mano.
- `datos/correcciones_manuales.json`: correcciones por ejercicio (opcional).
- `progreso/traducciones.json`: salida cruda del traductor (permite reanudar y regenerar).
- `progreso/revision.tsv`: tabla de revisión (se genera sola).

---

# GIFs de ExerciseDB y mapa muscular de wger

La app muestra, cuando existe, un GIF animado de ExerciseDB por ejercicio, y dibuja el mapa muscular
con las figuras de wger. Los dos se obtienen y preparan con los scripts de esta carpeta.

> **Aviso de licencias.** Esto se integró para **uso personal**.
> - **ExerciseDB (exercisedb.dev)**: su API gratuita no publica términos de uso para los datos ni para los
>   GIFs, y su README dice que esos endpoints no son para producción. No hay una licencia confirmada para
>   empaquetar los GIFs. **Antes de compartir o publicar la app** hay que conseguir permiso por escrito
>   (support@ascendapi.com) o comprar una licencia. Para quitarlos basta borrar `app/src/main/assets/gifs/`
>   y regenerar el catálogo (ver abajo).
> - **wger (wger.de)**: licencia Creative Commons BY-SA; exige crédito (ya está en "Acerca de") y compartir
>   igual lo que derive de las imágenes. La versión exacta de estos diagramas no la pude confirmar.

## GIFs de ExerciseDB

```bash
python3 descargar_exercisedb.py          # 1) catálogo de ExerciseDB -> datos/exercisedb.json (1500 ejercicios)
python3 emparejar_gifs.py                # 2) empareja con free-exercise-db -> datos/gifs_emparejados.json
python3 descargar_exercisedb.py --gifs   # 3) descarga los GIFs emparejados -> app/src/main/assets/gifs/
.venv/bin/python traducir_ejercicios.py --solo-ensamblar   # 4) agrega el campo "gif" a exercises_es.json
```

- Los pasos 1 y 3 respetan el límite de peticiones del servidor (esperan cuando responde 429) y se pueden
  interrumpir y repetir: continúan donde se quedaron.
- **Cómo se empareja** (`emparejar_gifs.py`): compara los nombres en inglés (sin palabras de equipo, con
  sinónimos y penalizando palabras que cambian el ejercicio como *incline*, *rear* o *decline*) y exige que
  el equipo y el músculo principal sean compatibles. Es mejor no tener GIF que mostrar el de otro ejercicio.
  Hoy empareja 327 de 876 ejercicios (37 %).
- **Corregir un emparejamiento**: edita `datos/gifs_manuales.json`. Un id de ExerciseDB fuerza el GIF de
  un ejercicio; `null` se lo quita. Siempre gana sobre el automático. Después repite los pasos 2 a 4.
  `progreso/revision_gifs.tsv` lista todos los pares con su puntaje para revisarlos.
- Si cambias los GIFs, sube `VERSION_CATALOGO` en `CargadorCatalogo.kt`.

## Mapa muscular de wger

Los diagramas originales (SVG) están en `datos/wger/`. Las imágenes que usa la app son PNG generados así:

```bash
for f in datos/wger/muscle-*.svg; do rsvg-convert -w 560 $f -o ../app/src/main/assets/musculos/$(basename $f .svg).png; done
rsvg-convert -w 560 datos/wger/muscular_system_front.svg -o ../app/src/main/assets/musculos/frente.png
rsvg-convert -w 560 datos/wger/muscular_system_back.svg  -o ../app/src/main/assets/musculos/espalda.png
```

Cada músculo es una capa transparente que se coloca sobre la figura; la app la tiñe con sus colores.
La correspondencia entre los músculos de la app y las capas de wger está en `MusculosWger.kt`. A wger le
faltan abductores, aductores, antebrazos, espalda baja, espalda media y cuello: si un ejercicio usa alguno,
se dibuja con el mapa propio de la app.
