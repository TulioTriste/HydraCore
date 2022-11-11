package me.arjona.hydracore.utilities;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MoneyUtil {

    public String format(double money) {
        if (money < 1000) return String.valueOf(Math.ceil(money));
        if (money < 1000000) return String.format("%.1fK", Math.ceil(money / 1000));
        if (money < 1000000000) return String.format("%.1fM", Math.ceil(money / 1000000));
        return String.format("%.1fB", Math.ceil(money / 1000000000));
    }
}
