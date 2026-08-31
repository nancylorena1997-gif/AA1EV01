package com.newpohone.modules.carrito.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CartItemTest {

    @Test
    void calculatesSubtotalFromPriceAndQuantity() {
        CartItem item = new CartItem();
        item.setPrecio(1500);
        item.setCantidad(3);
        assertEquals(4500, item.getSubtotal());
    }
}
