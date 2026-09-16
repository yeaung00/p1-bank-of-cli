package com.revature.utility;

import java.math.BigDecimal;

public class MoneyUtils {
    private MoneyUtils() {}

    public static BigDecimal centsToDollars(int cents) {
        return BigDecimal.valueOf(cents, 2);
    }

    public static int dollarsToCents(BigDecimal dollars) {
        return dollars.movePointRight(2).intValueExact();
    }
}
