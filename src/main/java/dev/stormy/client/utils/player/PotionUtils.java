package dev.stormy.client.utils.player;

import java.util.HashMap;

public class PotionUtils {
    private static final HashMap<Integer, Integer> GOOD_POTIONS = new HashMap<>() {{
        put(6, 1); // Instant Health
        put(10, 2); // Regeneration
        put(11, 3); // Resistance
        put(21, 4); // Health Boost
        put(22, 5); // Absorption
        put(23, 6); // Saturation
        put(5, 7); // Strength
        put(1, 8); // Speed
        put(12, 9); // Fire Resistance
        put(14, 10); // Invisibility
        put(3, 11); // Haste
        put(13, 12); // Water Breathing
    }};
    public static boolean goodPotion(final int id) {
        return GOOD_POTIONS.containsKey(id);
    }

    public static int potionRanking(final int id) {
        return GOOD_POTIONS.getOrDefault(id, -1);
    }
}
