package com.orange.clients.service;

import com.orange.clients.constant.ErrorCodeConst;

public class ResultMsg {
	private int code;
	private Object datas;
	
	public ResultMsg(){
		this.code = ErrorCodeConst.SUCC;
	}
	
	public ResultMsg(int code){
		this.code = code;
	}
	
	public ResultMsg(Object datas){
		this.code = ErrorCodeConst.SUCC;
		this.datas = datas;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Object getDatas() {
		return datas;
	}
	public void setDatas(Object datas) {
		this.datas = datas;
	}
}
