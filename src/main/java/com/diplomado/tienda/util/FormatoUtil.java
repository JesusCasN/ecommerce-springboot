package com.diplomado.tienda.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class FormatoUtil {

    public static String formatDecimal(BigDecimal value) {
        if (value == null) {
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(value);
    }
}
