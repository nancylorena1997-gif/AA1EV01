package com.newpohone.modules.inventario.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class InventoryStatusTest {

    @Test
    void classifiesStockLevels() {
        Map<String, Object> out = new LinkedHashMap<>();
        InventoryStatus.apply(out, 0, 30);
        assertEquals(InventoryStatus.LEVEL_OUT, out.get("level"));
        assertEquals("Agotado", out.get("stockLabel"));

        Map<String, Object> low = new LinkedHashMap<>();
        InventoryStatus.apply(low, 12, 30);
        assertEquals(InventoryStatus.LEVEL_LOW, low.get("level"));

        Map<String, Object> ok = new LinkedHashMap<>();
        InventoryStatus.apply(ok, 80, 30);
        assertEquals(InventoryStatus.LEVEL_OK, ok.get("level"));
        assertEquals("Disponible", ok.get("stockLabel"));
    }

    @Test
    void sanitizesInventoryFilters() {
        assertEquals("agotado", InventoryStatus.sanitizeFilter("agotado"));
        assertEquals("bajo", InventoryStatus.sanitizeFilter("bajo"));
        assertEquals("alerta", InventoryStatus.sanitizeFilter("alerta"));
        assertEquals("todos", InventoryStatus.sanitizeFilter("otro"));
        assertEquals("todos", InventoryStatus.sanitizeFilter(null));
    }
}
