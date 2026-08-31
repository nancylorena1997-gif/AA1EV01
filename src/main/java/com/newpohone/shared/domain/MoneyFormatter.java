package com.newpohone.shared.domain;

import java.text.NumberFormat;
import java.util.Locale;

public final class MoneyFormatter {

    private MoneyFormatter() {
    }

    public static String format(double value) {
        NumberFormat format = NumberFormat.getIntegerInstance(Locale.US);
        return "$" + format.format(Math.round(value));
    }
}
