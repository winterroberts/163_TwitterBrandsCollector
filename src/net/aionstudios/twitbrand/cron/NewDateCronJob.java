package net.aionstudios.twitbrand.cron;

import net.aionstudios.twitbrand.util.DateTimeUtils;

public class NewDateCronJob extends CronJob {

	public NewDateCronJob(CronDateTime cdt) {
		super(cdt);
	}

	@Override
	public void run() {
		System.out.println("New date GMT! "+DateTimeUtils.getServerTime()+" "+DateTimeUtils.getEpochTime());
	}
	
	

}
