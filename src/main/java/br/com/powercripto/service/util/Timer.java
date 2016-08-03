package br.com.powercripto.service.util;

import java.math.BigDecimal;

/**
 * Diego 29/07/16.
 */
public class Timer {
    private long startTime = 0;
    private long endTime   = 0;

    public void start(){
        this.startTime = System.currentTimeMillis();
    }

    public void end() {
        this.endTime   = System.currentTimeMillis();
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public BigDecimal getTotalTime() {
        return new BigDecimal(this.endTime).min(new BigDecimal(this.startTime));
    }
}
