package com.newpohone.modules.favoritos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FavoriteRedirectsTest {

    @Test
    void allowsKnownStorePathsAndFallsBackToFavorites() {
        assertEquals("/favoritos", FavoriteRedirects.safeNext(null));
        assertEquals("/catalog", FavoriteRedirects.safeNext("/catalog"));
        assertEquals("/catalog/12", FavoriteRedirects.safeNext("/catalog/12"));
        assertEquals("/cart", FavoriteRedirects.safeNext("/cart"));
        assertEquals("/favoritos", FavoriteRedirects.safeNext("https://externo.test"));
    }
}
