package com.newpohone.modules.carrito.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.newpohone.modules.catalogo.infrastructure.CatalogRepository;
import com.newpohone.modules.catalogo.presentation.ProductPresenter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CatalogRepository catalogRepository;

    @Mock
    private ProductPresenter productPresenter;

    private CartService cartService;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        cartService = new CartService(catalogRepository, productPresenter);
        session = new MockHttpSession();
        lenient().doAnswer(invocation -> null).when(productPresenter).enrich(any(Map.class));
    }

    @Test
    void addsProductAndPreventsExceedingStock() {
        when(catalogRepository.findById(1)).thenReturn(Optional.of(product(1, "Auriculares", 2)));

        Map<String, Object> added = cartService.add(session, 1);
        assertEquals(1, added.get("count"));
        assertEquals("Producto agregado al carrito.", added.get("message"));

        cartService.add(session, 1);
        Map<String, Object> overflow = cartService.add(session, 1);
        assertEquals(true, overflow.get("error"));
        assertEquals(2, overflow.get("count"));
    }

    @Test
    void rejectsMissingOrOutOfStockProducts() {
        when(catalogRepository.findById(99)).thenReturn(Optional.empty());
        Map<String, Object> missing = cartService.add(session, 99);
        assertEquals(true, missing.get("error"));
        assertEquals("El producto no existe.", missing.get("message"));

        when(catalogRepository.findById(2)).thenReturn(Optional.of(product(2, "Funda", 0)));
        Map<String, Object> empty = cartService.add(session, 2);
        assertEquals("Este producto está agotado.", empty.get("message"));
    }

    @Test
    void updatesRemovesAndClearsItems() {
        when(catalogRepository.findById(1)).thenReturn(Optional.of(product(1, "Cargador", 5)));
        cartService.add(session, 1);

        Map<String, Object> updated = cartService.update(session, 1, 3);
        assertEquals(3, updated.get("count"));

        cartService.remove(session, 1);
        assertTrue((Boolean) cartService.view(session).get("empty"));

        cartService.add(session, 1);
        cartService.clear(session);
        assertTrue((Boolean) cartService.view(session).get("empty"));
    }

    private Map<String, Object> product(int id, String name, int stock) {
        Map<String, Object> product = new LinkedHashMap<>();
        product.put("id", id);
        product.put("nombre", name);
        product.put("categoria", "Accesorios");
        product.put("image", "/images/products/product-case.png");
        product.put("precio", 1000);
        product.put("stock", stock);
        return product;
    }
}
