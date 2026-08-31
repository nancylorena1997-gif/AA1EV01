package com.newpohone.modules.crud.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ModuleRegistryTest {

    private final ModuleRegistry registry = new ModuleRegistry();

    @Test
    void registersBusinessModulesWithReadableKeys() {
        assertEquals(16, registry.all().size());
        assertNotNull(registry.get("productos"));
        assertEquals("Productos", registry.get("productos").getTitle());
        assertEquals("Catálogo", registry.get("productos").getGroup());
        assertEquals("producto", registry.get("productos").getTable());
        assertNull(registry.get("inexistente"));
    }

    @Test
    void exposesRequiredFieldsForReuseByTheCrudLayer() {
        ModuleDefinition clientes = registry.get("clientes");
        assertTrue(clientes.getFields().stream().anyMatch(field -> "cedula".equals(field.getName())));
        assertTrue(clientes.getFields().stream().anyMatch(field -> field.isRequired()));
    }
}
