package com.ubcn.psam.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:config/psam.properties")
public class Scheduler {

	@Autowired
	private psamStateSchedule pSamStat;
	
	@Scheduled(cron = "${spring.PSAM.state.cron}")
	public void checkState() {
		pSamStat.jobStart();
		
	}
}
