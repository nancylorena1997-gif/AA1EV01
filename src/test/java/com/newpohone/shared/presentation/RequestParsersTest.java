package com.newpohone.shared.presentation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class RequestParsersTest {

    @Test
    void parsesIntegerAndDouble() {
        assertEquals(12, RequestParsers.parseInteger("12"));
        assertEquals(19.99, RequestParsers.parseDouble("19.99"));
    }

    @Test
    void returnsNullForInvalidOrBlankValues() {
        assertNull(RequestParsers.parseInteger(""));
        assertNull(RequestParsers.parseInteger("abc"));
        assertNull(RequestParsers.parseDouble(null));
        assertNull(RequestParsers.parseDouble("no-num"));
    }
}
