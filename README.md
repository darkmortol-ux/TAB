# TabPersonalizado

Plugin para Paper 1.21.11 que personaliza el TAB del servidor (la lista de jugadores que aparece al presionar la tecla `Tab`), mostrando información fija y en vivo sin necesidad de plugins de terceros.

## ¿Qué muestra?

**Encabezado (fijo)** — se calcula una vez cuando el jugador entra, o al ejecutar `/tab reload`:

- Nombre del servidor
- Listado de rangos (por valor, de mayor a menor)
- Link de la tienda virtual
- Nombre del servidor de Discord

**Pie de página (en vivo)** — se actualiza automáticamente cada cierto tiempo:

- Coordenadas del jugador (X, Y, Z)
- Bioma en el que se encuentra
- Dimensión actual (Overworld, Nether o End), cada una con su propio nombre configurable

Todo el texto acepta códigos de color con `&` y, si tienes **PlaceholderAPI** instalado, también placeholders normales (`%player_name%`, `%vault_eco_balance%`, etc.).

## Dependencia: RangosMC

Este plugin necesita **[RangosMC](https://github.com/darkmortol-ux/RangosMC)** para poder mostrar el listado de rangos con sus colores y prefijos reales (Host, Admin, Mod, Constructor, Ayudante, Usuario).

- La conexión con RangosMC se hace **por reflexión**, no como dependencia de compilación — o sea que no hace falta el `.jar` de RangosMC al compilar TabPersonalizado, solo tenerlo instalado y activo en el servidor.
- Está declarado como `softdepend` en el `plugin.yml`, así que Paper se asegura de cargar RangosMC primero si está presente.
- Si RangosMC no está instalado o no está activo, el plugin sigue funcionando normalmente: simplemente omite del listado los rangos de tipo `rangosmc` (revisa la consola, va a avisarte con un warning).
- Para que un rango de RangosMC aparezca en el TAB, el nombre en `config.yml` debe coincidir **exactamente** con el nombre configurado en RangosMC.

También podés agregar rangos manuales que no vienen de RangosMC (por ejemplo un VIP comprado en la tienda) usando `tipo: personalizado` en el `config.yml`, y ubicarlos en cualquier posición del orden.

## Instalación

1. Instalá primero **[RangosMC](https://github.com/darkmortol-ux/RangosMC)** en tu servidor (y PlaceholderAPI, si querés usar placeholders extra).
2. Descargá el `.jar` de TabPersonalizado desde el release [`latest`](../../releases/tag/latest) de este repositorio.
3. Colocalo en la carpeta `plugins/` de tu servidor Paper.
4. Iniciá o reiniciá el servidor. Esto genera el `config.yml` en `plugins/TabPersonalizado/`.
5. Editá el `config.yml` a tu gusto (nombre del server, tienda, discord, rangos, formato del footer, etc.).
6. Ejecutá `/tab reload` para aplicar los cambios sin reiniciar el servidor.

## Comandos y permisos

| Comando       | Descripción                                  | Permiso                  |
|---------------|-----------------------------------------------|---------------------------|
| `/tab reload` | Recarga la configuración y actualiza el TAB de todos los jugadores conectados | `tabpersonalizado.admin` (default: `op`) |

## Configuración rápida

```yaml
servidor:
  nombre: "&6&l✦ MiServidorMC ✦"
  tienda: "&etienda.miservidor.com"
  discord: "&9discord.gg/miservidor"

tab-rangos:
  - tipo: rangosmc
    nombre: Host
  - tipo: personalizado
    nombre: VIP
    prefix: "&6&l[VIP]"
  - tipo: rangosmc
    nombre: Admin
  # ...
```

Ver el `config.yml` incluido en el plugin para la lista completa de opciones comentadas (formato del header, nombres de dimensiones, formato y frecuencia de actualización del footer).

## Requisitos

- Paper (o fork compatible) **1.21.11**
- Java **21**
- [RangosMC](https://github.com/darkmortol-ux/RangosMC) (softdepend)
- PlaceholderAPI (opcional, softdepend)

## Compilación

```bash
mvn clean package
```

El `.jar` queda en `target/`. Este repositorio también incluye un workflow de GitHub Actions que compila y publica automáticamente el `.jar` en el release `latest` con cada push a `main`.
