package com.newpohone.modules.checkout.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CheckoutIntentTest {

    @Test
    void expiresAfterFifteenMinutes() {
        CheckoutIntent intent = new CheckoutIntent();
        intent.setCreatedAt(System.currentTimeMillis());
        assertFalse(intent.expired());

        intent.setCreatedAt(System.currentTimeMillis() - (16 * 60 * 1000));
        assertTrue(intent.expired());
    }
}
