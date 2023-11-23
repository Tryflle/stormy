package dev.stormy.client.utils;

import net.minecraft.client.Minecraft;

import java.util.Random;

/**
 * @author sassan
 * 23.11.2023, 2023
 */
public interface IMethods {
    Minecraft mc = Minecraft.getMinecraft();

    Random rand = new Random();
}
