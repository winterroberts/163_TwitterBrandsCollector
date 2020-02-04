package net.aionstudios.twitbrand.request;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.aionstudios.twitbrand.TwitterOAuthSession;
import net.aionstudios.twitbrand.util.DateTimeUtils;
import net.aionstudios.twitbrand.util.FormatUtils;

/**
 * @author Winter Roberts
 * Used to make requests to the Twitter Search API
 * for a keyword.
 */
public class TweetProcessor {

	private String searchKeyword;
	
	private List<Tweet> tweets;
	private long tweetVolume;
	
	/**
	 * Creates a new StockProcessor for the given keyword.
	 * 
	 * @param keyword The keyword this TweetProcessor should use.
	 */
	public TweetProcessor(String keyword) {
		searchKeyword = keyword;
		tweets = new LinkedList<>();
	}
	
	/**
	 * @return The keyword used by this TweetProcessor.
	 */
	public String getSearchKeyword() {
		return searchKeyword;
	}
	
	/**
	 * Requests and stores the 100 most recent tweets for the given keyword.
	 * 
	 * @throws JSONException If keys are missing or of the wrong type in the response.
	 */
	public void makeRequest() throws JSONException {
		String search = "https://api.twitter.com/1.1/search/tweets.json?q="+FormatUtils.encodeValue(searchKeyword)+"&result_type=recent&count=100&include_entities=false&lang=en";
		JSONObject results = new JSONObject(TwitterOAuthSession.getInstance().makeAPIRequest(search).getResponse());
		JSONArray statuses = results.getJSONArray("statuses");
		int totalTweets = 0;
		long minUnix = 0;
		long maxUnix = 0;
		tweets = new LinkedList<>();
		for(int i = 0; i < statuses.length(); i++) {
			JSONObject status = statuses.getJSONObject(i);
			String createdAt = status.getString("created_at");
			String tweetID = status.getString("id_str");
			String text = status.getString("text");
			Integer unixCreatedAt = DateTimeUtils.twitUnixTime(createdAt);
			if(unixCreatedAt < DateTimeUtils.getEpochTime()-3540) {
				continue;
			}
			if(minUnix == 0) {
				minUnix = unixCreatedAt;
				maxUnix = unixCreatedAt;
			}
			if(minUnix > unixCreatedAt) {
				minUnix = unixCreatedAt;
			}
			if(maxUnix < unixCreatedAt) {
				maxUnix = unixCreatedAt;
			}
			tweets.add(new Tweet(unixCreatedAt, tweetID, text));
			//System.out.println(unixCreatedAt + " " + tweetID + " " + text);
			totalTweets++;
		}
		tweetVolume = totalTweets > 99 ? (long) Math.ceil((double) totalTweets*((double) 3600/(maxUnix-minUnix))) : totalTweets;
		System.out.println("Estimated tweet volume '"+searchKeyword+"': "+tweetVolume);
	}

	/**
	 * @return The {@link Tweet}s reported by the last successful request to the
	 * Twitter Search API for this TweetProcessor's keyword.
	 */
	public List<Tweet> getTweets() {
		return tweets;
	}

	/**
	 * @return An estimate of the number of Tweets which occured in the last hour
	 * built from information from the last successful request to the Twitter Searhc
	 * API for this TweetProcessor's keyword.
	 */
	public long getTweetVolume() {
		return tweetVolume;
	}
	
}
