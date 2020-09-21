package com.billing.app.util;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hafiz
 */
public class TransactionIdGenerator implements SequenceGenerator{

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static AtomicLong numberGenerator = new AtomicLong(1);
	
	@Override
	public long getNext() {
        logger.info("AtomicLong Generation Current Number: " + numberGenerator.get());
		if (numberGenerator.get() >= 99999){
		    numberGenerator.set(1);
            logger.info("AtomicLong Generation Reset to: " + numberGenerator.get());
        }
		return numberGenerator.incrementAndGet();
	}

	public void setNumberGenerator (long seqNumber){
		numberGenerator.set(seqNumber);
	}
}
