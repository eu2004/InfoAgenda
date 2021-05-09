package ro.eu.infoagenda.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class LocalCash<T> {
    private static final Logger logger = LogManager.getLogger(LocalCash.class);
    private final int evictionPeriod;
    private T cashedValue;
    private volatile long lastCall = -1;

    public LocalCash(int evictionPeriod) {
        this.evictionPeriod = evictionPeriod;
    }

    public T getValue() {
        if (lastCall == -1) {
            resetCashedValue();
            return cashedValue;
        }

        long timeSinceLastCall = System.currentTimeMillis() - lastCall;
        if (evictionPeriod <= timeSinceLastCall) {
            logger.info("cashedValue has expired! EvictionPeriod:"
                    + evictionPeriod
                    + " <= timeSinceLastCall:"
                    + timeSinceLastCall);
            cashedValue = null;
        }
        return cashedValue;
    }

    public void setValue(T value) {
        lastCall = System.currentTimeMillis();
        setCashedValue(value);
    }

    @Override
    public String toString() {
        if (cashedValue != null)
            return cashedValue.toString();

        return "";
    }

    protected void setCashedValue(T value) {
        cashedValue = value;
    }

    protected void resetCashedValue() {
        cashedValue = null;
    }
}
