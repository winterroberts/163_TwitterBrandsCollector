package net.aionstudios.twitbrand.cron;

import net.aionstudios.twitbrand.util.DateTimeUtils;

/**
 * @author Winter Roberts
 * A {@link CronJob} that notifies the application host console
 * textually that it has passed midnight in GMT.
 * (via a PST definition in a {@link CronDateTime}).
 */
public class NewDateCronJob extends CronJob {

	/**
	 * Creates a new NewDateCronJob.
	 * To notify when the time passes midnight GMT.
	 * 
	 * Only ONE should be created!
	 * 
	 * @param cdt The {@link CronDateTime} that defines times at which
	 * this {@link CronJob} should be run.
	 */
	public NewDateCronJob(CronDateTime cdt) {
		super(cdt);
	}

	@Override
	public void run() {
		System.out.println("New date GMT! "+DateTimeUtils.getServerTime()+" "+DateTimeUtils.getEpochTime());
	}
	
	

}
