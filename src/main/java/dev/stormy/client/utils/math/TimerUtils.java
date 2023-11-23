package dev.stormy.client.utils.math;

public class TimerUtils {
    private long lastMS;

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(final double milliseconds) {
        return this.getCurrentMS() - this.lastMS >= milliseconds;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }
}