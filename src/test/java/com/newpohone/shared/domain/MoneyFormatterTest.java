package com.newpohone.shared.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MoneyFormatterTest {

    @Test
    void formatsRoundedIntegerAmount() {
        assertEquals("$1,250", MoneyFormatter.format(1249.6));
    }

    @Test
    void formatsZero() {
        assertEquals("$0", MoneyFormatter.format(0));
    }
}
