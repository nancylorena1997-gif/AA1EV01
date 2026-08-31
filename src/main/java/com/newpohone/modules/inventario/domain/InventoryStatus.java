package com.newpohone.modules.inventario.domain;

import java.util.Map;

public final class InventoryStatus {

    public static final String LEVEL_OUT = "out";
    public static final String LEVEL_LOW = "low";
    public static final String LEVEL_OK = "ok";

    private InventoryStatus() {
    }

    public static void apply(Map<String, Object> row, int stock, int threshold) {
        if (stock <= 0) {
            row.put("level", LEVEL_OUT);
            row.put("stockLabel", "Agotado");
        } else if (stock <= threshold) {
            row.put("level", LEVEL_LOW);
            row.put("stockLabel", "Stock bajo");
        } else {
            row.put("level", LEVEL_OK);
            row.put("stockLabel", "Disponible");
        }
    }

    public static String sanitizeFilter(String estado) {
        if (estado == null) {
            return "todos";
        }
        return switch (estado) {
            case "agotado", "bajo", "alerta" -> estado;
            default -> "todos";
        };
    }
}
