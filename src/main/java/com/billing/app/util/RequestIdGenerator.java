package com.billing.app.util;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hafiz
 */
public class RequestIdGenerator implements SequenceGenerator{

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static AtomicLong numberGenerator = new AtomicLong(900000001);	
	private static final AtomicLong LAST_TIME_MS = new AtomicLong();
	
	@Override
	public long getNext() {
		return numberGenerator.getAndIncrement();
	}

	public static long uniqueCurrentTimeMS() {
        long now = System.currentTimeMillis()/10;
        while (true) {
            long lastTime = LAST_TIME_MS.get();
            if (lastTime >= now)
                now = lastTime + 1;
            if (LAST_TIME_MS.compareAndSet(lastTime, now))
                return now;
        }
    }
}
