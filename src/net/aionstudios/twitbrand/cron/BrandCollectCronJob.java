package net.aionstudios.twitbrand.cron;

import net.aionstudios.twitbrand.request.BrandManager;

public class BrandCollectCronJob extends CronJob {

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
