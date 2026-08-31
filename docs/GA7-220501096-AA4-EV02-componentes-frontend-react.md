## Componentes React propuestos

### 1 Tienda pública

#### 1. `EncabezadoTienda`

Encabezado de la tienda: logo Newphone, accesos a favoritos, carrito, seguimiento, login y, si el usuario es administrador, al panel.

**Justificación:** Hoy ese bloque se copia en varias plantillas. Como componente se escribe una sola vez y se reutiliza en catálogo, ficha, carrito y pago.

#### 2. `BarraBusquedaCatalogo`

Campo de búsqueda del catálogo, con el botón “Buscar” y los filtros activos ocultos (categoría, precio, orden).

**Justificación:** Es la primera acción del cliente. En React puede actualizar resultados sin recargar toda la página.

#### 3. `FiltrosCatalogo`

Barra lateral (o panel móvil) con categoría, rango de precio, “solo con stock” y criterio de ordenación.

**Justificación:** El catálogo actual ya concentra esos controles en un solo formulario. Un componente aislado facilita mostrarlos en escritorio y en el botón “Filtros” del celular.

#### 4. `TarjetaProducto`

Tarjeta con foto, categoría, estrellas, precio, stock, corazón de favorito y botón “Agregar al carrito”.

**Justificación:** La grilla del catálogo y la lista de favoritos muestran el mismo producto. Un solo componente evita duplicar esa tarjeta.

#### 5. `FichaProducto`

Ficha completa: imagen grande, nombre, descripción, precio, estado de stock y botones de carrito y favoritos.

**Justificación:** Es la pantalla de decisión de compra. Separarla permite cargar reseñas y acciones sin mezclarlas con el listado.

#### 6. `SeccionResenas`

Listado de opiniones, selector de estrellas y caja de comentario para clientes que ya compraron el producto.

**Justificación:** Las reseñas tienen reglas propias (solo quien compró puede calificar). Encapsularlas deja esa lógica en un solo lugar.

#### 7. `CajonCarrito`

Panel que se abre desde la derecha con los productos elegidos, el total y el enlace “Editar carrito”.

**Justificación:** El cajón aparece encima del catálogo y de la ficha. Como componente se abre y se cierra con estado, sin repetir el HTML.

#### 8. `LineaCarrito`

Fila de un producto en el carrito: foto, precio, botones +/−, subtotal y opción de quitar.

**Justificación:** La misma línea sirve en el cajón y en la página completa de edición. Así el ajuste de cantidades queda unificado.

#### 9. `FormularioPago`

Formulario de pago: nombre de quien recibe, teléfono, dirección, método (tarjeta, transferencia u otro) y, si aplica, datos de la tarjeta.

**Justificación:** El checkout junta validación, resumen y simulación de cobro. Un componente dedicado mantiene el flujo de compra ordenado.

#### 10. `FormularioSeguimiento`

Consulta de pedido con guía (ejemplo `NP-20260719-000001`) y teléfono, más la línea de estados (pendiente, preparado, enviado, entregado o cancelado).

**Justificación:** El cliente no entra al panel admin. Este componente cubre el seguimiento público sin mezclarlo con la gestión interna.

#### 11. `TarjetaAcceso`

Tarjeta de acceso con pestañas “Iniciar sesión” y “Crear cuenta”, campos de correo y contraseña, y avisos de error o éxito.

**Justificación:** Login y registro ya comparten la misma presentación. Un componente con pestañas evita dos pantallas casi iguales.

#### 12. `WidgetWhatsApp`

Botón flotante de asesor, con alternativas de llamada, correo y copiar número si la persona no tiene WhatsApp.

**Justificación:** El fragmento `whatsapp.html` ya se incrusta en varias páginas. En React sería un widget global de contacto.

---

### 4.2 Centro de operaciones

#### 13. `MenuLateralAdmin`

Menú izquierdo con marca Newphone, enlaces a Resumen, Inventario y Pedidos, lista de los 16 módulos y pie con nombre, rol y cierre de sesión.

**Justificación:** Es el esqueleto del `layout/base.html`. Extraerlo permite que dashboard, inventario y CRUD compartan la misma navegación.

#### 14. `BarraSuperiorAdmin`

Franja superior con menú móvil, migas de pan, título de la pantalla, campana de stock bajo y acceso al catálogo público.

**Justificación:** Cada sección del panel cambia el título, pero la barra es la misma. Un componente recibe el título y las alertas como datos.

#### 15. `TarjetaMetrica`

Tarjeta numérica (ventas, pedidos, clientes, productos, alertas, enviados, entregados, etc.) con etiqueta, cifra e icono.

**Justificación:** El dashboard, el inventario y la lista de pedidos usan el mismo tipo de tarjeta. Reutilizarla mantiene el resumen visual uniforme.

#### 16. `TablaDatos`

Tabla con columnas, filas, badges de estado, acciones de ver/editar/eliminar y mensaje cuando no hay registros.

**Justificación:** Inventario, pedidos y los 16 módulos muestran listados muy parecidos. Un componente de tabla se configura según el módulo.

#### 17. `FormularioModulo`

Formulario genérico de alta y edición: arma campos de texto, número, fecha, correo, contraseña o área de texto según el módulo.

**Justificación:** Hoy un solo `form.html` sirve para productos, clientes, facturas y el resto. En React se replica esa idea: un formulario, muchos módulos.

#### 18. `ModalEstadoPedido`

Ventana para cambiar el estado de un pedido, con confirmación de que el cliente verá el cambio en el seguimiento.

**Justificación:** Es un diálogo puntual, no una página. Un componente modal se abre desde la lista o desde el detalle sin recargar el panel.

#### 19. `BannerAlertaInventario`

Franja de aviso cuando hay productos con 5 unidades o menos, con enlace a “Ver alertas”.

**Justificación:** El inventario necesita un llamado de atención visible. Separarlo permite mostrarlo también en el dashboard si hace falta.

#### 20. `AvisoEmergente`

Aviso corto de operación correcta (“registro guardado”) o de error (“no se pudo eliminar”).

**Justificación:** Casi todas las pantallas muestran `success` o `error`. Un toast reutilizable da la misma respuesta visual en tienda y en administración.
