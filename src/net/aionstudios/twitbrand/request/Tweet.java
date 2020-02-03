package net.aionstudios.twitbrand.request;

public class Tweet {
	
	private long unixTime;
	private String tweetID;
	private String text;
	
	public Tweet(long unixTime, String tweetID, String text) {
		this.unixTime = unixTime;
		this.tweetID = tweetID;
		this.text = text;
	}

	public long getUnixTime() {
		return unixTime;
	}

	public String getTweetID() {
		return tweetID;
	}

	public String getText() {
		return text;
	}

}
