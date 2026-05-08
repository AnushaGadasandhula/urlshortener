package org.jsp.urlshortener.model;

public class LinkData {
	private String longUrl;
	private int clicks;

	public LinkData(String longUrl) {
		this.longUrl = longUrl;
		this.clicks = 0;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public int getClicks() {
		return clicks;
	}

	public void setClicks(int clicks) {
		this.clicks = clicks;
	}

	public void incrementClicks() {
		this.clicks++;
	}
}
