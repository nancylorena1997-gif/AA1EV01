# Prompt — App React del módulo Productos (Newphone)

Copia y pega el bloque siguiente en un agente o usa este archivo como especificación única para crear el proyecto.

---

## Prompt

Eres un desarrollador front-end. Crea desde cero una aplicación **React** (Vite) para la evidencia **GA7-220501096-AA4-EV03** del proyecto formativo **Newphone** (tienda de celulares y accesorios).

El único módulo funcional a implementar es **Productos**. El resultado debe verse y navegarse como el centro de operaciones del proyecto Spring Boot actual, pero **sin ningún backend**: ni Spring Boot, ni APIs, ni `fetch` a un servidor propio. Toda la información vive en el navegador.

### Restricciones obligatorias

1. **Sin backend.** No hay JSON Server, Mock Service Worker contra un API real, Firebase, ni llamadas HTTP de negocio. La app es 100 % front-end.
2. **Datos en memoria del navegador.** Usa estado de React (`useState` / `useReducer` + Context) como capa de aplicación y **persiste** productos, categorías y administradores de demostración en `localStorage`. Si el almacenamiento está vacío, carga el seed. Si el usuario crea, edita o elimina, actualiza el estado y `localStorage` de inmediato.
3. **Un solo módulo operativo: Productos.** El resto de la navegación existe para respetar el menú del proyecto actual, pero esas rutas muestran una pantalla de “no implementado en esta evidencia”, excepto Productos.
4. **Interfaz responsive.** Debe verse bien en escritorio y en móvil (menú hamburguesa, tabla con desplazamiento horizontal o tarjetas en pantallas estrechas, formulario a una columna).
5. **Español** en toda la interfaz.
6. No implementes catálogo público, carrito, checkout, login real ni los otros 15 CRUD.

### Identidad visual (copiar del Spring Boot)

Paleta y tipografía del `app.css` actual:

```css
--bg: #070b1a;
--bg-elevated: #0d1430;
--panel: rgba(16, 22, 48, .82);
--ink: #f4f7ff;
--muted: #8b93b0;
--line: rgba(255, 255, 255, .08);
--cyan: #20d9ff;
--cyan-deep: #0bbde7;
--magenta: #ec39d5;
--violet: #884dff;
--nav: #0a1028;
--radius: 18px;
--shadow: 0 24px 60px rgba(0, 0, 0, .45);
--font: "Outfit", sans-serif;
--display: "Syne", sans-serif;
```

- Fondo del `body`: radial-gradients cyan / magenta / violeta sobre `--bg`.
- Tipografías: **Outfit** (UI) y **Syne** (títulos), Google Fonts.
- Iconos: **Bootstrap Icons**.
- Botón principal (`.btn-neon`): degradado `cyan → #7cf0ff → magenta`, texto `#041018`.
- Botón secundario (`.btn-ghost`): transparente, borde `--line`.
- Enlace de menú activo: degradado `rgba(32, 217, 255, .16)` a `rgba(236, 57, 213, .12)` y borde interior cyan.
- Badges: degradado cyan → magenta, texto `#041018`.
- Toasts de éxito: fondo cyan translúcido. Toasts de error: fondo `#ff5a78` translúcido.
- Píldoras de stock: normal (cyan), `warn` si `0 < stock ≤ 30`, `danger` si `stock ≤ 0`.
- Radio de paneles ~18px, sombras profundas, estética oscura “neon”.
- `theme-color`: `#070b1a`.
- Marca: **Newphone** / subtítulo **Celulares y accesorios**. Si no hay logo, usa un recuadro con la N y el mismo degradado del avatar.

### Navegación (igual que el layout actual)

Reproduce el **shell de administración** de `layout/base.html`:

- **Sidebar fijo** (270px) con marca, tarjeta “Espacio de trabajo · Tienda principal”, nav y pie de usuario.
- **Topbar** con migas (`Newphone › sección`), título `h1`, y chips (Alertas de stock, “Datos locales”, indicador en línea). En móvil: botón de menú que abre el sidebar y un backdrop.
- **Pie del sidebar:** avatar con inicial, nombre `Ana Administradora`, rol `ADMINISTRADOR`, botón de “cerrar sesión” que solo limpia un flag visual o muestra un aviso (no hay autenticación real).

Orden del menú (los no implementados no deben romperse):

1. Resumen (`/`)
2. Inventario (`/inventario`)
3. Pedidos (`/pedidos`)
4. Sección **Usuarios:** Cuentas, Clientes, Administradores
5. Sección **Catálogo:** Categorías, **Productos** (único activo de verdad)
6. Sección **Compras:** Carritos, Detalle de carritos, Productos favoritos
7. Sección **Pedidos:** Pedidos, Detalle de pedidos, Envíos, Facturas, Pagos
8. Sección **Atención:** Reseñas, Atención al cliente
9. Sección **Reportes:** Reportes de venta

Iconos Bootstrap Icons del registro actual: `bi-grid-1x2-fill`, `bi-clipboard-data`, `bi-bag-check`, `bi-person-badge`, `bi-people`, `bi-shield-lock`, `bi-tags`, `bi-phone`, `bi-cart3`, `bi-cart-plus`, `bi-heart`, `bi-bag-check`, `bi-list-ul`, `bi-truck`, `bi-receipt`, `bi-credit-card`, `bi-star`, `bi-headset`, `bi-graph-up-arrow`.

Rutas de Productos (equivalentes a `/modules/productos` del Spring Boot):

| Ruta | Pantalla |
| --- | --- |
| `/modules/productos` | Listado con búsqueda |
| `/modules/productos/nuevo` | Alta |
| `/modules/productos/:id` | Consulta (solo lectura) |
| `/modules/productos/:id/editar` | Edición |

Las demás entradas del menú van a una vista genérica: título del ítem, texto “Este módulo no forma parte de la evidencia. Solo Productos está implementado.” y enlace de vuelta a Productos. **Productos** debe quedar marcado como activo cuando la URL esté bajo `/modules/productos`.

### Modelo de Producto

Campos alineados con `ModuleRegistry` / tabla `producto`:

| Campo | Etiqueta | Tipo UI | Obligatorio | Notas |
| --- | --- | --- | --- | --- |
| `idproducto` | ID | número | no (autogenerado) | No se pide en el alta |
| `nombre` | Nombre | texto | sí | Mínimo 3 caracteres |
| `precio` | Precio | decimal | sí | `≥ 0`; mostrar en COP (`$ 5.299.900`) |
| `stock` | Unidades disponibles | entero | sí | `≥ 0`; píldora de estado |
| `descripcion` | Descripción | textarea | sí | Mínimo 10 caracteres |
| `administrador_cedula` | Administrador | select | no | Catálogo en memoria |
| `categoria_id_categoria` | Categoría | select | no | Catálogo en memoria |

Relaciones (solo para selects y para mostrar nombres en la tabla, no CRUD propio):

- Categorías seed: `1 Smartphones`, `2 Accesorios`, `3 Audio`.
- Administrador seed: cédula `1001`, `Ana Administradora`.

Umbral de stock bajo: **30** unidades (igual que `newphone.inventory.low-stock-threshold`).

### Casos de uso a cubrir

1. **Listado:** tabla (escritorio) / tarjetas (móvil) con ID, nombre, precio, stock, descripción recortada, categoría, administrador y acciones Ver / Editar / Eliminar.
2. **Búsqueda / consulta en listado:** filtrar por texto en nombre, descripción, precio, stock, id, nombre de categoría o administrador (equivalente al `q` de `/modules/{key}`).
3. **Consulta:** ficha de un producto, solo lectura, botón Editar y Volver.
4. **Creación:** formulario, validación visible por campo, toast de éxito, redirigir al listado.
5. **Edición:** mismo formulario con datos cargados; el ID no se modifica.
6. **Eliminación:** confirmación (modal al estilo oscuro/neon, no `window.confirm` pobre); toast de éxito. Como no hay backend, no hay error de FK: al borrar, el registro desaparece del almacenamiento.
7. **Estado vacío:** mensaje “Sin registros” o “Sin coincidencias” y botón Crear.
8. **Alertas en topbar:** contar productos con `stock ≤ 30`; el menú de campana lista esos ítems y enlaza a editar.

### Seed inicial (si `localStorage` está vacío)

```text
1 | NewPhone X Pro | 5299900 | 25 | Pantalla AMOLED, cámara profesional y rendimiento de nueva generación. | admin 1001 | cat 1
2 | NewPhone Air  | 2899900 | 42 | Ligero, potente y diseñado para acompañarte todo el día. | admin 1001 | cat 1
3 | Funda MagSafe | 89900   | 100 | Protección premium con acople magnético. | admin 1001 | cat 2
4 | NewBuds Pro   | 649900  | 36 | Audio inmersivo con cancelación activa de ruido. | admin 1001 | cat 3
5 | Cargador 45W  | 159900  | 8  | Carga rápida USB-C con protección inteligente. | admin 1001 | cat 2
```

IDs autogenerados a partir del máximo existente + 1.

### Validación de formularios

Validar en el cliente **antes** de guardar. Mostrar errores bajo cada campo (`*` magenta en requeridos):

- Nombre: obligatorio, mínimo 3 caracteres.
- Precio: número ≥ 0 (aceptar coma o punto decimal).
- Stock: entero ≥ 0.
- Descripción: obligatoria, 10–400 caracteres.
- Categoría y administrador: si se eligen, deben existir en el catálogo en memoria.

Toasts al guardar, al eliminar y si se pide un `:id` inexistente (redirigir al listado con error).

### Stack y estructura sugerida

- Vite + React (JavaScript o TypeScript).
- React Router para las rutas del shell y de Productos.
- Context (por ejemplo `ProductosProvider`) + hook `useProductos` para listar, buscar, obtener por id, crear, actualizar y eliminar.
- Claves de `localStorage`: `newphone.productos`, `newphone.categorias`, `newphone.administradores`.
- CSS propio (variables de arriba). No hace falta copiar Bootstrap completo; sí los iconos.
- Componentes reutilizables mínimos: `Sidebar`, `Topbar`, `TablaProductos` / tarjetas, `FormularioProducto`, `ModalConfirmacion`, `Toast`, `EstadoVacio`, `PildoraStock`.

### Criterio de terminado

- `npm install` y `npm run dev` levantan la app sin errores.
- En escritorio se reconoce el look & feel de Newphone (fondo oscuro, neon cyan/magenta, sidebar).
- En un viewport de ~375px el menú es off-canvas, el listado no desborda de forma ilegible y el formulario es usable.
- Recargar el navegador conserva altas, ediciones y bajas (localStorage).
- Solo Productos tiene CRUD completo; el menú lateral sigue el orden del proyecto Spring Boot.

No agregues backend, autenticación real ni los demás módulos.
