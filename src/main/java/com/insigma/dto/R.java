package com.insigma.dto;


import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.insigma.common.util.JsonUtils;
import com.insigma.dto.enums.SysCode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 接口通用返回类
 *
 * @author admin
 */

@ApiModel(description ="公共出参")
public class R<T> extends HashMap implements java.io.Serializable  {
    private static final Log log = LogFactory.getLog(R.class);
    @ApiModelProperty(example="true",name = "是否成功")
    private boolean success; // 业务状态码，默认为true
    @ApiModelProperty(example="200",name = "错误码")
    private int errorcode;
    @ApiModelProperty(example="成功",name = "错误信息")
	private String errormsg = "";
    @ApiModelProperty(example="true",name = "数据包体json串")
    private String data;
    
	public void setData(String data) {
		this.put("data", data);
		this.data = data;
	}

	public int getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(int errorcode) {
		this.put("errorcode", errorcode);
		this.errorcode = errorcode;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.put("errormsg", errormsg);
		this.errormsg = errormsg;
	}
	
	
	public String getData() {
		return data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.put("success", success);
		this.success = success;
	}
	
	public R() {
		setSuccess(true);
		setErrormsg(SysCode.SUCCESS.getCodemsg());
		setErrorcode(SysCode.SUCCESS.getCode());
    }
    
	 /**
     * 成功返回
     *
     * @param message
     * @return
     */
    public static R success(String data) {
        R dto = new R();
        dto.setSuccess(true);
        dto.setData(data);
        log.debug(JsonUtils.objectToJson(dto));
        return dto;
    }
    
    /**
     * 成功返回
     *
     * @param message
     * @return
     */
    public static R success(Object data) {
        R dto = new R();
        dto.setSuccess(true);
        dto.setData(JsonUtils.objectToJson(data));
        log.debug(JsonUtils.objectToJson(dto));
        return dto;
    }
    
    /**
     * 默认错误返回
     * @param sysCode
     * @return
     */
    public static R fail(){
        return fail(SysCode.FAIL);
    }
    
    /**
     * 默认错误返回
     * @param sysCode
     * @return
     */
    public static R fail(String message){
        return fail(SysCode.FAIL,message);
    }
    
    /**
     * 错误返回
     * @param commStatus
     * @param msg
     * @return
     */
    public static R fail(SysCode syscode){
        return fail(syscode,syscode.getCodemsg());
    }
    
    /**
     * 错误返回
     * @param commStatus
     * @param msg
     * @return
     */
    public static R fail(SysCode syscode,String message){
        R dto=new R();
        dto.setSuccess(false);
        dto.setErrorcode(syscode.getCode());
        dto.setErrormsg(message);
        dto.setData("{}");
        log.debug(JsonUtils.objectToJson(dto));
        return dto;
    }
    
    /**
     * 默认异常返回
     * @param sysCode
     * @return
     */
    public static R error(){
    	 return fail(SysCode.ERROR);
    }

    /**
     * 默认异常返回
     * @param sysCode
     * @return
     */
    public static R error(String message){
    	 return fail(SysCode.ERROR,message);
    }

    /**
     * 异常返回
     * @param sysCode
     * @return
     */
    public static R error(SysCode sysCode){
        return fail(sysCode,sysCode.getCodemsg());
    }
    
    /**
     * 异常返回
     * @param commStatus
     * @param msg
     * @return
     */
    public static R error(SysCode syscode,String message){
    	return fail(syscode,syscode.getCodemsg());
    }
}
