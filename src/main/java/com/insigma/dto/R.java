package com.insigma.dto;


import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.insigma.common.util.JsonUtils;
import com.insigma.dto.enums.SysCode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * �ӿ�ͨ�÷�����
 *
 * @author admin
 */

@ApiModel(description ="��������")
public class R<T> extends HashMap implements java.io.Serializable  {
    private static final Log log = LogFactory.getLog(R.class);
    @ApiModelProperty(example="true",name = "�Ƿ�ɹ�")
    private boolean success; // ҵ��״̬�룬Ĭ��Ϊtrue
    @ApiModelProperty(example="200",name = "������")
    private int errorcode;
    @ApiModelProperty(example="�ɹ�",name = "������Ϣ")
	private String errormsg = "";
    @ApiModelProperty(example="true",name = "���ݰ���json��")
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
     * �ɹ�����
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
     * �ɹ�����
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
     * Ĭ�ϴ��󷵻�
     * @param sysCode
     * @return
     */
    public static R fail(){
        return fail(SysCode.FAIL);
    }
    
    /**
     * Ĭ�ϴ��󷵻�
     * @param sysCode
     * @return
     */
    public static R fail(String message){
        return fail(SysCode.FAIL,message);
    }
    
    /**
     * ���󷵻�
     * @param commStatus
     * @param msg
     * @return
     */
    public static R fail(SysCode syscode){
        return fail(syscode,syscode.getCodemsg());
    }
    
    /**
     * ���󷵻�
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
     * Ĭ���쳣����
     * @param sysCode
     * @return
     */
    public static R error(){
    	 return fail(SysCode.ERROR);
    }

    /**
     * Ĭ���쳣����
     * @param sysCode
     * @return
     */
    public static R error(String message){
    	 return fail(SysCode.ERROR,message);
    }

    /**
     * �쳣����
     * @param sysCode
     * @return
     */
    public static R error(SysCode sysCode){
        return fail(sysCode,sysCode.getCodemsg());
    }
    
    /**
     * �쳣����
     * @param commStatus
     * @param msg
     * @return
     */
    public static R error(SysCode syscode,String message){
    	return fail(syscode,syscode.getCodemsg());
    }
}
