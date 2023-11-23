package dev.stormy.client.utils.packet;

import net.minecraft.network.Packet;

public record TimedPacket(Packet<?> packet, long time) {
}