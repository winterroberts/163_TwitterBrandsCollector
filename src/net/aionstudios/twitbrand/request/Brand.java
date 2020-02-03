package net.aionstudios.twitbrand.request;

import org.json.JSONException;

import net.aionstudios.twitbrand.util.DatabaseUtils;

public class Brand {

	private TweetProcessor tweetProcessor;
	private StockProcessor stockProcessor;
	
	private String insertPriceQuery = "INSERT INTO `twitbrands`.`stock_volume` (`symbol`, `matchingID`, `stock_price_current`, `tweet_volume`, `unix_timestamp`, `currency`) VALUES (?, ?, ?, ?, ?, ?);";
	private String insertTweetQuery = "INSERT INTO `twitbrands`.`tweets` (`tweetID`, `matchingID`, `tweet`, `unix_timestamp`) VALUES (?, ?, ?, ?);";
	
	public Brand(String symbol, String name) {
		tweetProcessor = new TweetProcessor(name);
		stockProcessor = new StockProcessor(symbol);
		BrandManager.getInstance().getBrands().add(this);
	}
	
	public void process() throws JSONException {
		if(stockProcessor.makeRequest()) {
			tweetProcessor.makeRequest();
			String matchingID = stockProcessor.getSymbol() + "_" + stockProcessor.getLastCollected();
			DatabaseUtils.prepareAndExecute(insertPriceQuery, true, stockProcessor.getSymbol(), matchingID, stockProcessor.getPrice(), tweetProcessor.getTweetVolume(), stockProcessor.getLastCollected(), stockProcessor.getCurrency());
			for(Tweet t : tweetProcessor.getTweets()) {
				DatabaseUtils.prepareAndExecute(insertTweetQuery, true, stockProcessor.getSymbol() + "_" + t.getTweetID(), matchingID, t.getText(), t.getUnixTime());
			}
			System.out.println("["+stockProcessor.getSymbol()+"] Price: "+stockProcessor.getPrice()+" "+stockProcessor.getCurrency()+", Tweet Volume: "+tweetProcessor.getTweetVolume());
		}
	}
	
}
