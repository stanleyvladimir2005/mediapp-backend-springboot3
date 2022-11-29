package com.mitocode.utils;

import lombok.Data;
import java.util.Map;

@Data
public class Mail {

	private String from;
	private String to;
	private String subject;
	private Map<String, Object> model;

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}
}