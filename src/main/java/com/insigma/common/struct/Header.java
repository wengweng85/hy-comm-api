package com.insigma.common.struct;

/**
 * Created by wengsh on 2019/6/5.
 */
public class Header {

    //服务代码
    private String serviceCode;
    //信息系统节点代码
    private String appCode;
    //服务区域代码
    private String serviceAreaCode;
    //请求流水号 9位发起业务系统ID+8位日期+9位流水号。该流水号不允许重复
    private String serviceReqId;
    //业务请求日期 YYYYMMDDHH24MISS
    private String serviceReqTime;
    //	响应流水号
    private String serviceResId;
    //业务响应日期 YYYYMMDDHH24MISS
    private String serviceResTime;
    /**
     *   00业务成功
         90其他错误
         10业务失败
         20系统错误
         30签名失败
         40解密失败
         50权限不足
     */
    private String commStatus;//响应状态
    private String busiStatus;//业务状态
    private String msg;//返回信息

    //数字签名
    private String signature;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getServiceAreaCode() {
        return serviceAreaCode;
    }

    public void setServiceAreaCode(String serviceAreaCode) {
        this.serviceAreaCode = serviceAreaCode;
    }

    public String getServiceReqId() {
        return serviceReqId;
    }

    public void setServiceReqId(String serviceReqId) {
        this.serviceReqId = serviceReqId;
    }

    public String getServiceReqTime() {
        return serviceReqTime;
    }

    public void setServiceReqTime(String serviceReqTime) {
        this.serviceReqTime = serviceReqTime;
    }

    public String getServiceResId() {
        return serviceResId;
    }

    public void setServiceResId(String serviceResId) {
        this.serviceResId = serviceResId;
    }

    public String getServiceResTime() {
        return serviceResTime;
    }

    public void setServiceResTime(String serviceResTime) {
        this.serviceResTime = serviceResTime;
    }

    public String getCommStatus() {
        return commStatus;
    }

    public void setCommStatus(String commStatus) {
        this.commStatus = commStatus;
    }

    public String getBusiStatus() {
        return busiStatus;
    }

    public void setBusiStatus(String busiStatus) {
        this.busiStatus = busiStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
    
}
