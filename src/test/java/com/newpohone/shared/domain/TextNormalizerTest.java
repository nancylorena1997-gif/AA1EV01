package com.newpohone.shared.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class TextNormalizerTest {

    @Test
    void extractsDigitsFromPhone() {
        assertEquals("573001001001", TextNormalizer.digits("+57 300-100-1001"));
    }

    @Test
    void digitsOfNullIsEmpty() {
        assertEquals("", TextNormalizer.digits(null));
    }

    @Test
    void blankToNullTrimsOrClears() {
        assertEquals("texto", TextNormalizer.blankToNull("  texto  "));
        assertNull(TextNormalizer.blankToNull("   "));
        assertNull(TextNormalizer.blankToNull(null));
    }
}
