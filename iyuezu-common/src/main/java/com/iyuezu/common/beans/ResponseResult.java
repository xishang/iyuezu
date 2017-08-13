package com.iyuezu.common.beans;

public class ResponseResult <T> {

	private String code;
	private String msg;
	private T data;
	
	public ResponseResult(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public ResponseResult(String code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
