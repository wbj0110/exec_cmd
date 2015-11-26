package com.soledede.exec.entity;

import java.util.List;

public class Msg {

	private String info;
	
	private int code;
	

	public Msg() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Msg(String info, int code) {
		super();
		this.info = info;
		this.code = code;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
