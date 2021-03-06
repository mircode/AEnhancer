package com.baidu.aenhancer.conf.runtime;

public class ShortCircuitSlidingWindowConfig implements Cloneable {
    private int windowSize; // 时间窗口的大小
    private int interval; // millisecond

    public ShortCircuitSlidingWindowConfig(int windowSize, int interval) {
        this.windowSize = windowSize;
        this.interval = interval;
    }

    public ShortCircuitSlidingWindowConfig() {
        this(30, 5000);
    }

    public int getWindowSize() {
        return windowSize;
    }

    public int getInterval() {
        return interval;
    }
}
