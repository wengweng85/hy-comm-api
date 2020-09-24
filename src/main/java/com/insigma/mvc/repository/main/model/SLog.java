package com.insigma.mvc.repository.main.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ϵͳ��־��
 *
 * @author admin
 */
@TableName("s_log")//ָ������
@Data
@NoArgsConstructor
public class SLog implements java.io.Serializable {

    private String messagetype;//��Ϣ����
    private String logid;//			��־id
    private String interfacetype;//			�ӿ����ʹ���
    private String interfacecode;//			�ӿ����ʹ���
    private String message;//		��־����
    private Date logtime;//		����ʱ��
    private String logtime_string;//����ʱ��
    private String cost;//		����ķ�ʱ��
    private String stackmsg;//		�쳣ջ��Ϣ
    private String exceptiontype;//		�쳣����
    private String usergent;//		user-agent
    private String ipaddr;//		�ͻ���ip��ַ
    private String referer;//		refer
    private String url;//		����ĵ�ַ
    private String userid;//		��ǰ������Աid
    private String cookie;//		cookie
    private String appkey;//		appkey
    private String queryparam;//		���������Ϣ
    private String method;//		���󷽷�����
    private String success;//	�����Ƿ�ɹ�
    private String responsemsg;//	������Ϣ
    private String token;//����jwt״̬��
    private String content;
    private String logtype;
}