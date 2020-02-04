package net.aionstudios.twitbrand.request;

/**
 * @author Winter Roberts
 * A tweet containing minimal information to record time,
 * unique ID and text.
 */
public class Tweet {
	
	private long unixTime;
	private String tweetID;
	private String text;
	
	/**
	 * Creates a new tweet with the given creation time, ID
	 * and text.
	 * 
	 * @param unixTime The unix time at which this tweet was created.
	 * @param tweetID The Tweet ID for this tweet.
	 * @param text The text of this tweet.
	 */
	public Tweet(long unixTime, String tweetID, String text) {
		this.unixTime = unixTime;
		this.tweetID = tweetID;
		this.text = text;
	}

	/**
	 * @return The time at which this tweet was created.
	 */
	public long getUnixTime() {
		return unixTime;
	}

	/**
	 * @return The ID of this tweet.
	 */
	public String getTweetID() {
		return tweetID;
	}

	/**
	 * @return The text of this tweet.
	 */
	public String getText() {
		return text;
	}

}
