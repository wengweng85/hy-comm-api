package com.insigma.rpc.client.entity;

public class Head {
	private String Flag;
	private String Mess;
	
	public String getFlag() {
		return Flag;
	}
	public void setFlag(String flag) {
		Flag = flag;
	}
	public String getMess() {
		return Mess;
	}
	public void setMess(String mess) {
		Mess = mess;
	}
	
	public Head() {
		super();
	}

	public Head(String Flag, String Mess) {
		super();
		this.Flag = Flag;
		this.Mess = Mess;
	}
	
	

}
