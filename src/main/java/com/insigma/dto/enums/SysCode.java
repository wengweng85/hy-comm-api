package com.insigma.dto.enums;

/**
 *  系统请求状态码
 */
public enum SysCode {
	
	SUCCESS(200,"成功"),
	NOINTERFACE(11400, "接口不存在"),
	FAIL(11300, "业务错误"),
	//错误
	NODATA(11301, "业务失败-没有数据"),
	APPKEY_EMPTY(11401,"appkey或appsecret为空"),
	APPKEY_ERROR(11402,"appkey或appsecret不正确"),
	TOKEN_EMPTY(11403,"token为空,请先登录"),
	TOKEN_ERROR(11404,"token值不正确或已经过期,请重新登录"),
	USERID_ERROR(11405,"登录信息与token信息不匹配,非法请求请确认"),
	SERVICEURL_ERROR(11406,"没有访问此服务的权限或地址地址,请确认"),
	API_RATELIMIT(11407,"访问次数受限"),
	//异常
	ERROR(11500, "接口异常"),
	SIGN_PARAM_EMPTY(11501,"签名参数为空或缺失"),
	SIGN_TIMESTAMP_EXPIRE(11502,"请求已过期"),
	SIGN_ERROR(11503,"非法请求,验签失败"),
	//限流
	REQUEST_LIMIT(11504,"请求超时");
	
	private int code;
	private String codemsg;
	
	SysCode(int code, String name){
		this.code=code;
		this.codemsg=name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getCodemsg() {
		return codemsg;
	}

	public void setCodemsg(String codemsg) {
		this.codemsg = codemsg;
	}

}
