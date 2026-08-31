package com.newpohone.modules.pedidos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @Test
    void mapsLabelsKeysAndSynonyms() {
        assertEquals(OrderStatus.PENDIENTE, OrderStatus.from(null));
        assertEquals(OrderStatus.PREPARADO, OrderStatus.from("Preparado"));
        assertEquals(OrderStatus.ENVIADO, OrderStatus.from("en camino"));
        assertEquals(OrderStatus.ENTREGADO, OrderStatus.from("entregado"));
        assertEquals(OrderStatus.CANCELADO, OrderStatus.from("Cancelado"));
        assertEquals(OrderStatus.ENVIADO, OrderStatus.fromKey("enviado"));
        assertNull(OrderStatus.fromKey("todos"));
    }

    @Test
    void terminalStatusesDoNotAllowFurtherChanges() {
        assertTrue(OrderStatus.ENTREGADO.isTerminal());
        assertTrue(OrderStatus.CANCELADO.isTerminal());
        assertFalse(OrderStatus.PENDIENTE.isTerminal());
        assertEquals(4, OrderStatus.FLOW.size());
    }
}
