package com.billing.app.model;

public class KeyValuePair {
	public String key;
	public String value;

	public KeyValuePair(String key, String value) {
		if (key == null) key = "";
		if (value == null) value = "";
		
		this.key = key;
		this.value = value;
	}
}
