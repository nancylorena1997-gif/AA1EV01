package com.newpohone.modules.favoritos.domain;

public final class FavoriteRedirects {

    private FavoriteRedirects() {
    }

    public static String safeNext(String next) {
        if (next == null || next.isBlank()) {
            return "/favoritos";
        }
        String value = next.trim();
        if (value.equals("/favoritos") || value.equals("/cart") || value.equals("/catalog")
                || value.matches("/catalog/\\d+")) {
            return value;
        }
        return "/favoritos";
    }
}
