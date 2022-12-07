package com.healthcare.api.auth;

/**
 * 
 * @author orange
 *
 */
public class KeyEntry {
	private String key;
	private long timeCreate;

	public KeyEntry(String k, long t) {
		this.key = k;
		this.timeCreate = t;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getTimeCreate() {
		return timeCreate;
	}

	public void setTimeCreate(long timeCreate) {
		this.timeCreate = timeCreate;
	}

}
