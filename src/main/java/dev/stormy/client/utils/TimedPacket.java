package dev.stormy.client.utils;

import net.minecraft.network.Packet;

public record TimedPacket(Packet<?> packet, long time) {
}