package net.aionstudios.twitbrand.request;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import net.aionstudios.twitbrand.util.DateTimeUtils;

/**
 * @author Winter Roberts
 * Used to make requests to the Yahoo Finance API
 * for a stock symbol.
 */
@SuppressWarnings("deprecation")
public class StockProcessor {
	
	private String symbol;
	
	private String currency;
	private double price;
	private long lastCollected;
	
	/**
	 * Creates a new StockProcessor for the given symbol.
	 * 
	 * @param symbol The stock symbol this StockProcessor should use.
	 */
	public StockProcessor(String symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * @return The stock symbol used by this StockProcessor.
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * Requests and stores the price, currency and last collection time
	 * for stock information of the symbol associated with this StockProcessor
	 * if inside the trading period.
	 * 
	 * @return True if the request succeeded and was inside the trading period.
	 * @throws JSONException If keys are missing or of the wrong type in the response.
	 */
	public boolean makeRequest() throws JSONException {
		String search = "https://query1.finance.yahoo.com/v7/finance/chart/"+symbol+"?range=1h&interval=60m&includeTimestamps=true";
		String response = webRequest(search);
		long gmtEpoch = DateTimeUtils.getEpochTime();
		if(response!=null) {
			JSONObject results = new JSONObject(response);
			JSONObject resMeta = results.getJSONObject("chart").getJSONArray("result").getJSONObject(0).getJSONObject("meta");
			
			JSONObject regularTradePeriod = resMeta.getJSONObject("currentTradingPeriod").getJSONObject("regular");
			//API reports open/close in GMT by default
			long unixGMTTradeEnd = regularTradePeriod.getInt("end");
			long unixGMTTradeStart = regularTradePeriod.getInt("start");
			if(gmtEpoch > unixGMTTradeStart && gmtEpoch < unixGMTTradeEnd+1800) {
				//Additional 1800 at end for extreme resistance to waiting.
				lastCollected = gmtEpoch;
				price = resMeta.getDouble("regularMarketPrice");
				currency = resMeta.getString("currency");
				return true;
			}
		}
		System.out.println("["+getSymbol()+"] Outside of trading period!");
		return false;
	}
	
	/**
	 * Makes a web request to the given URL.
	 * 
	 * @param url The URL to which this request should be made.
	 * @return The response of this request or null if it failed.
	 */
	@SuppressWarnings("resource")
	private String webRequest(String url) {
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpGet);
			return IOUtils.toString(httpResponse.getEntity().getContent());
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return The currency reported by the last successful request to the
	 * Yahoo Finance API for this StockProccesor's stock symbol.
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @return The price reported by the last successful request to the
	 * Yahoo Finance API for this StockProccesor's stock symbol.
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @return The unix epoch time (GMT) at the last successful request to the
	 * Yahoo Finance API for this StockProccesor's stock symbol.
	 */
	public long getLastCollected() {
		return lastCollected;
	}
	
}
