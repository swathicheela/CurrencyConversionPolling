package com.test.example;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.test.example.services.CurrencyConvertionPollingService;

@Component
public class Scheduler {

	final static Logger logger = Logger.getLogger(Scheduler.class);

	@Autowired
	CurrencyConvertionPollingService currencyConvertionPollingService;

	@Scheduled(fixedDelay = 60000)
	public void scheduleFixedDelayTask() {
		logger.info("===== CurrencyConvertionPollingService started ===== ");
		currencyConvertionPollingService.poll();
		logger.info("===== CurrencyConvertionPollingService ended ===== ");
	}
}
