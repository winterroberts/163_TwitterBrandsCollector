package net.aionstudios.twitbrand;

import java.io.File;
import java.io.IOException;
import org.json.JSONException;

import net.aionstudios.aionlog.AnsiOut;
import net.aionstudios.aionlog.Logger;
import net.aionstudios.aionlog.StandardOverride;
import net.aionstudios.twitbrand.cron.BrandCollectCronJob;
import net.aionstudios.twitbrand.cron.CronDateTime;
import net.aionstudios.twitbrand.cron.CronManager;
import net.aionstudios.twitbrand.cron.NewDateCronJob;
import net.aionstudios.twitbrand.request.Brand;

/**
 * @author Winter Roberts
 */
public class Main {

	public static void main(String[] args) {
		File f = new File("./logs/");
		f.mkdirs();
		Logger.setup();
		AnsiOut.initialize();
		AnsiOut.setStreamPrefix("Twitter Brands Collector");
		StandardOverride.enableOverride();
		try {
			TwitterBrandsInfo.readConfigsAtStart();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TwitterOAuthSession.getInstance(); //Creates an OAuth Session with Twitter credentials
		TwitterBrandsInfo.setupDB();
		
		CronDateTime cdt = new CronDateTime();
		cdt.setMinuteRange(1, 1); //hourly on minute 1
		CronManager.addJob(new BrandCollectCronJob(cdt));
		
		CronDateTime gmtUp = new CronDateTime();
		gmtUp.setMinuteRange(1, 1);
		gmtUp.setHourRange(16, 16);//hour 24 in GMT from PST
		CronManager.addJob(new NewDateCronJob(gmtUp));
		
		new Brand("AAPL", "Apple");
		new Brand("MSFT", "Microsoft");
		new Brand("V", "Visa");
		new Brand("INTC", "Intel");
		new Brand("MA", "MasterCard");
		new Brand("CSCO", "Cisco");
		new Brand("ADBE", "Adobe");
		new Brand("CRM", "Salesforce");
		new Brand("NVDA", "Nvidia");
		new Brand("PYPL", "PayPal");
		new Brand("AMZN", "Amazon");
		new Brand("GOOG", "Google");
		new Brand("FB", "Facebook");
		new Brand("T", "AT&T");
		new Brand("VZ", "Verizon");
		new Brand("JWN", "Nordstrom");
		new Brand("ORCL", "Oracle");
		new Brand("TWTR", "Twitter");
		new Brand("NFLX", "Netflix");
		new Brand("SNAP", "Snapchat");
		new Brand("TMUS", "T-Mobile");
		new Brand("S", "Sprint");
		new Brand("UBER", "Uber");
		new Brand("LYFT", "Lyft");
		new Brand("TSLA", "Tesla");
		new Brand("HPQ", "HP");
		new Brand("SNE", "Sony");
		new Brand("AMD", "AMD");
		new Brand("SBUX", "Starbucks");
		new Brand("066570.KS", "LG");
		new Brand("005930.KS", "Samsung");
		new Brand("DELL", "Dell");
		new Brand("SQ", "Square");
		new Brand("IFN", "OnePlus");
		new Brand("BA", "Boeing");
		new Brand("COST", "Costco");
		new Brand("WMT", "Walmart");
		new Brand("KR", "Kroger");
		new Brand("DPZ", "Dominos");
		new Brand("MCD", "McDonalds");
		new Brand("WEN", "Wendys");
		new Brand("CMG", "Chipotle");
		new Brand("DQ", "Dairy Queen");
		new Brand("K", "Kelloggs");
		new Brand("LNVGF", "Lenovo");
		
		CronManager.startCron();
		
//		TweetProcessor apple = new TweetProcessor("Apple");
//		apple.makeRequest();
//		StockProcessor AAPL = new StockProcessor("AAPL");
//		try {
//			if(AAPL.makeRequest()) {
//				System.out.println(AAPL.getSymbol()+": "+AAPL.getPrice()+" "+AAPL.getCurrency());
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
	}
	
}