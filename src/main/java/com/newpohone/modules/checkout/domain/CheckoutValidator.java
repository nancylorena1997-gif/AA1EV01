package com.newpohone.modules.checkout.domain;

import com.newpohone.shared.domain.TextNormalizer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CheckoutValidator {

    public static final List<Map<String, String>> PAYMENT_METHODS = List.of(
            method("PSE", "PSE", "Transferencia inmediata desde tu banco.", "bi-bank"),
            method("Bancolombia", "Bancolombia", "Paga con tu app o sucursal virtual.", "bi-phone"),
            method("Tarjeta de crédito", "Tarjeta de crédito", "Visa, Mastercard y American Express.",
                    "bi-credit-card-2-front"),
            method("Tarjeta débito", "Tarjeta débito", "Débito Visa o Mastercard.", "bi-credit-card"));

    private static final Set<String> ALLOWED_METHODS = Set.of(
            "PSE", "Bancolombia", "Tarjeta de crédito", "Tarjeta débito");

    private CheckoutValidator() {
    }

    public static boolean isAllowed(String metodo) {
        return metodo != null && ALLOWED_METHODS.contains(metodo);
    }

    public static boolean isCard(String metodo) {
        return metodo != null && metodo.startsWith("Tarjeta");
    }

    public static void validatePayment(String metodo, String titular, String numero,
            String vencimiento, String cvv) {
        if (!isAllowed(metodo)) {
            throw new CheckoutException("Selecciona un método de pago.");
        }
        if (isCard(metodo)) {
            validateCard(titular, numero, vencimiento, cvv);
        }
    }

    public static void validateShipping(String nombre, String telefono, String direccion) {
        if (nombre == null || nombre.trim().length() < 3) {
            throw new CheckoutException("Ingresa el nombre de quien recibe el pedido.");
        }
        String phone = TextNormalizer.digits(telefono);
        if (phone.length() < 7 || phone.length() > 15) {
            throw new CheckoutException("Ingresa un teléfono válido para el seguimiento.");
        }
        if (direccion == null || direccion.trim().length() < 10) {
            throw new CheckoutException("Ingresa una dirección de entrega completa.");
        }
    }

    public static void validateCard(String titular, String numero, String vencimiento, String cvv) {
        if (titular == null || titular.trim().length() < 3) {
            throw new CheckoutException("Ingresa el nombre que aparece en la tarjeta.");
        }
        String cardDigits = TextNormalizer.digits(numero);
        if (cardDigits.length() < 13 || cardDigits.length() > 19) {
            throw new CheckoutException("Ingresa un número de tarjeta válido.");
        }
        if (vencimiento == null || !vencimiento.trim().matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
            throw new CheckoutException("Ingresa la fecha de vencimiento en formato MM/AA.");
        }
        if (cvv == null || !cvv.trim().matches("^\\d{3,4}$")) {
            throw new CheckoutException("Ingresa un CVV válido.");
        }
    }

    public static void validateTracking(String guia, String telefono) {
        if (guia == null || guia.isBlank() || telefono == null || TextNormalizer.digits(telefono).length() < 7) {
            throw new CheckoutException("Ingresa la guía y el teléfono registrados en la compra.");
        }
    }

    private static Map<String, String> method(String id, String name, String description, String icon) {
        Map<String, String> method = new LinkedHashMap<>();
        method.put("id", id);
        method.put("nombre", name);
        method.put("descripcion", description);
        method.put("icono", icon);
        return method;
    }
}
