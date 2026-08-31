package com.newpohone.modules.autenticacion.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordHasherTest {

    @Test
    void hashesAndVerifiesPassword() {
        String hash = PasswordHasher.hash("admin123");
        assertTrue(hash.startsWith("pbkdf2$"));
        assertTrue(PasswordHasher.verify("admin123", hash));
        assertFalse(PasswordHasher.verify("otra", hash));
        assertFalse(PasswordHasher.needsUpgrade(hash));
    }

    @Test
    void acceptsLegacyPlainTextAndFlagsUpgrade() {
        assertTrue(PasswordHasher.verify("admin123", "admin123"));
        assertTrue(PasswordHasher.needsUpgrade("admin123"));
        assertFalse(PasswordHasher.verify("admin123", "otra"));
        assertFalse(PasswordHasher.verify("clave", null));
    }
}
