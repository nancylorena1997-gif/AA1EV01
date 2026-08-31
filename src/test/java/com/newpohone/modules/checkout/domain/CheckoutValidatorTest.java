package com.newpohone.modules.checkout.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CheckoutValidatorTest {

    @Test
    void acceptsPseWithoutCardData() {
        assertTrue(CheckoutValidator.isAllowed("PSE"));
        assertFalse(CheckoutValidator.isCard("PSE"));
        assertDoesNotThrow(() -> CheckoutValidator.validatePayment("PSE", null, null, null, null));
    }

    @Test
    void rejectsInvalidPaymentAndShipping() {
        CheckoutException payment = assertThrows(CheckoutException.class,
                () -> CheckoutValidator.validatePayment("Efectivo", null, null, null, null));
        assertEquals("Selecciona un método de pago.", payment.getMessage());

        CheckoutException shipping = assertThrows(CheckoutException.class,
                () -> CheckoutValidator.validateShipping("Al", "123", "Cra 1"));
        assertEquals("Ingresa el nombre de quien recibe el pedido.", shipping.getMessage());
    }

    @Test
    void validatesCardAndTracking() {
        assertDoesNotThrow(() -> CheckoutValidator.validateCard(
                "Ana Pérez", "4111111111111111", "12/29", "123"));

        CheckoutException card = assertThrows(CheckoutException.class,
                () -> CheckoutValidator.validateCard("Ana Pérez", "123", "12/29", "123"));
        assertEquals("Ingresa un número de tarjeta válido.", card.getMessage());

        CheckoutException tracking = assertThrows(CheckoutException.class,
                () -> CheckoutValidator.validateTracking(" ", "12"));
        assertEquals("Ingresa la guía y el teléfono registrados en la compra.", tracking.getMessage());
    }
}
