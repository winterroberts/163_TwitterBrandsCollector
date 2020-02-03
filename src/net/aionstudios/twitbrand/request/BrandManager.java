package net.aionstudios.twitbrand.request;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;

import net.aionstudios.twitbrand.util.DateTimeUtils;

public class BrandManager {
	
	private static BrandManager self;
	private List<Brand> brands;
	
	private BrandManager() {
		brands = new LinkedList<>();
	}
	
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
	
	public List<Brand> getBrands() {
		return brands;
	}
	
	public static BrandManager getInstance() {
		if(self == null) {
			self = new BrandManager();
		}
		return self;
	}

}
