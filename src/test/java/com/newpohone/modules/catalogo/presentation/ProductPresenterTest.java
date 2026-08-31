package com.newpohone.modules.catalogo.presentation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ProductPresenterTest {

    private final ProductPresenter presenter = new ProductPresenter();

    @Test
    void marksAvailabilityAndReviewSummary() {
        Map<String, Object> product = product("Newphone Buds", "Audio", 8, 2, 4.5);
        presenter.enrich(product);

        assertTrue((Boolean) product.get("available"));
        assertEquals("Últimas unidades", product.get("stockLabel"));
        assertEquals("/images/products/product-earbuds.png", product.get("image"));
        assertEquals("4.5 · 2 reseñas", product.get("reviewSummary"));
        assertEquals(90, product.get("starPercent"));
    }

    @Test
    void marksOutOfStockAndMissingReviews() {
        Map<String, Object> product = product("Smartphone X", "Celulares", 0, 0, 0);
        presenter.enrich(product);

        assertFalse((Boolean) product.get("available"));
        assertEquals("Agotado", product.get("stockLabel"));
        assertEquals("/images/products/product-smartphone.png", product.get("image"));
        assertEquals("Sin reseñas", product.get("reviewSummary"));
    }

    private Map<String, Object> product(String name, String category, int stock, int reviews, double avg) {
        Map<String, Object> product = new LinkedHashMap<>();
        product.put("nombre", name);
        product.put("categoria", category);
        product.put("stock", stock);
        product.put("reviewCount", reviews);
        product.put("reviewAvg", avg);
        return product;
    }
}
