package com.newpohone.modules.carrito.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CartTest {

    @Test
    void accumulatesItemsTotalsAndQuantities() {
        Cart cart = new Cart();
        cart.put(item(1, 1000, 2));
        cart.put(item(2, 500, 1));

        assertEquals(2, cart.getLineCount());
        assertEquals(3, cart.getItemCount());
        assertEquals(2500, cart.getTotal());

        cart.remove(1);
        assertEquals(1, cart.getLineCount());
        assertEquals(500, cart.getTotal());

        cart.clear();
        assertTrue(cart.isEmpty());
    }

    private CartItem item(int id, double precio, int cantidad) {
        CartItem item = new CartItem();
        item.setId(id);
        item.setNombre("Producto " + id);
        item.setPrecio(precio);
        item.setCantidad(cantidad);
        item.setStock(10);
        return item;
    }
}
