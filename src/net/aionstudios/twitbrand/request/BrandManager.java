package net.aionstudios.twitbrand.request;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;

import net.aionstudios.twitbrand.util.DateTimeUtils;

/**
 * @author Winter Roberts
 * Manages {@link Brand}s for the application, including calling them
 * to collect tweet and stock information.
 */
public class BrandManager {
	
	private static BrandManager self;
	private List<Brand> brands;
	
	/**
	 * Creates a new BrandMananger.
	 */
	private BrandManager() {
		brands = new LinkedList<>();
	}
	
	/**
	 * Called by a {@link CronJob} to collect tweet and stock
	 * information for every {@link Brand}.
	 */
	public void process() {
		for(Brand b : brands) {
			try {
				b.process();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			DateTimeUtils.safeWait(2000);
		}
	}
	
	
	/**
	 * @return A list of all {@link Brand}s that have been created.
	 */
	public List<Brand> getBrands() {
		return brands;
	}
	
	/**
	 * @return A singleton instance of this class.
	 */
	public static BrandManager getInstance() {
		if(self == null) {
			self = new BrandManager();
		}
		return self;
	}

}
