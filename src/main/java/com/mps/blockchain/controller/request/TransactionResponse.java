package com.mps.blockchain.controller.request;

import java.util.Map;

public class TransactionResponse {
	private String result;

	private Map<String, Object> data;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
