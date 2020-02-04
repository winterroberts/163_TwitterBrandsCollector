package net.aionstudios.twitbrand.cron;

import net.aionstudios.twitbrand.request.BrandManager;

/**
 * @author Winter Roberts
 * A {@link CronJob} that collects calls each {@link Brand}
 * to collect and store stock and tweet information.
 */
public class BrandCollectCronJob extends CronJob {

	/**
	 * Creates a new BrandCollectCronJob.
	 * While this enables multiple {@link CronJob}s to
	 * be run the best method is to pass a {@link CronDateTime}
	 * which specifies multiple run periods.
	 * 
	 * Only ONE should be created!
	 * 
	 * @param cdt The {@link CronDateTime} that defines times at which
	 * this {@link CronJob} should be run.
	 */
	public BrandCollectCronJob(CronDateTime cdt) {
		super(cdt);
	}

	@Override
	public void run() {
		System.out.println("Brand Collect CronJob started!");
		BrandManager.getInstance().process();
		System.out.println("Brand Collect CronJob completed or skipped!");
	}

}
