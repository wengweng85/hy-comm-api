package com.insigma.dto.enums;

/**
 *  ϵͳ����״̬��
 */
public enum SysCode {
	
	SUCCESS(200,"�ɹ�"),
	NOINTERFACE(11400, "�ӿڲ�����"),
	FAIL(11300, "ҵ�����"),
	//����
	NODATA(11301, "ҵ��ʧ��-û������"),
	APPKEY_EMPTY(11401,"appkey��appsecretΪ��"),
	APPKEY_ERROR(11402,"appkey��appsecret����ȷ"),
	TOKEN_EMPTY(11403,"tokenΪ��,���ȵ�¼"),
	TOKEN_ERROR(11404,"tokenֵ����ȷ���Ѿ�����,�����µ�¼"),
	USERID_ERROR(11405,"��¼��Ϣ��token��Ϣ��ƥ��,�Ƿ�������ȷ��"),
	SERVICEURL_ERROR(11406,"û�з��ʴ˷����Ȩ�޻��ַ��ַ,��ȷ��"),
	API_RATELIMIT(11407,"���ʴ�������"),
	//�쳣
	ERROR(11500, "�ӿ��쳣"),
	SIGN_PARAM_EMPTY(11501,"ǩ������Ϊ�ջ�ȱʧ"),
	SIGN_TIMESTAMP_EXPIRE(11502,"�����ѹ���"),
	SIGN_ERROR(11503,"�Ƿ�����,��ǩʧ��"),
	//����
	REQUEST_LIMIT(11504,"����ʱ");
	
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
