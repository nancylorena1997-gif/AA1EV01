package com.newpohone.modules.crud.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class NavItemTest {

    @Test
    void buildsSectionAndModuleLinks() {
        NavItem section = NavItem.section("Catálogo");
        assertTrue(section.section());
        assertEquals("Catálogo", section.label());

        ModuleRegistry registry = new ModuleRegistry();
        NavItem link = NavItem.link(registry.get("productos"));
        assertFalse(link.section());
        assertEquals("productos", link.key());
        assertEquals("Productos", link.label());
    }
}
