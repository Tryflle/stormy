package dev.stormy.client.utils.math;

import dev.stormy.client.utils.IMethods;
import java.util.Random;

/**
 * @author sassan
 * 23.11.2023, 2023
 */
public class MathUtils implements IMethods {
    public static Random rand() {
        return rand;
    }

    public static double round(double n, int d) {
        if (d == 0) {
            return (double) Math.round(n);
        } else {
            double p = Math.pow(10.0D, d);
            return (double) Math.round(n * p) / p;
        }
    }

    // will be implemented soon
    public static int randomInt(double inputMin, double v) {
        return (int) (Math.random() * (v - inputMin) + inputMin);
    }
}
