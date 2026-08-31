package com.newpohone.modules.checkout.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.newpohone.modules.carrito.application.CartService;
import com.newpohone.modules.catalogo.infrastructure.CatalogRepository;
import com.newpohone.modules.checkout.domain.CheckoutException;
import com.newpohone.modules.checkout.domain.CheckoutIntent;
import com.newpohone.modules.checkout.infrastructure.CheckoutRepository;
import com.newpohone.modules.pedidos.application.OrderManagementService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private CartService cartService;
    @Mock
    private CatalogRepository catalogRepository;
    @Mock
    private CheckoutRepository checkoutRepository;
    @Mock
    private OrderManagementService orderManagementService;

    private CheckoutService checkoutService;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        checkoutService = new CheckoutService(cartService, catalogRepository, checkoutRepository,
                orderManagementService);
        session = new MockHttpSession();
    }

    @Test
    void rejectsEmptyCart() {
        when(cartService.view(session)).thenReturn(Map.of("empty", true));
        CheckoutException exception = assertThrows(CheckoutException.class,
                () -> checkoutService.prepare(session, "PSE", null, null, null, null,
                        "Ana Pérez", "3001001001", "Calle 10 # 20-30 Bogotá"));
        assertEquals("Agrega productos al carrito para continuar.", exception.getMessage());
    }

    @Test
    void preparesIntentForValidCartAndPayment() {
        when(cartService.view(session)).thenReturn(cartView());
        when(catalogRepository.findById(1)).thenReturn(java.util.Optional.of(Map.of(
                "id", 1, "nombre", "Auriculares", "stock", 5)));

        CheckoutIntent intent = checkoutService.prepare(session, "PSE", null, null, null, null,
                "Ana Pérez", "3001001001", "Calle 10 # 20-30 Bogotá");

        assertEquals("PSE", intent.getMetodo());
        assertEquals("3001001001", intent.getTelefono());
        assertEquals(intent, checkoutService.requireIntent(session));
    }

    @Test
    void requireIntentFailsWhenMissing() {
        CheckoutException exception = assertThrows(CheckoutException.class,
                () -> checkoutService.requireIntent(session));
        assertEquals("La sesión de pago expiró. Vuelve a confirmar tu compra.", exception.getMessage());
    }

    private Map<String, Object> cartView() {
        return Map.of(
                "empty", false,
                "total", 1000,
                "items", List.of(Map.of("id", 1, "cantidad", 1, "subtotal", 1000)));
    }
}
