package com.insigma.common.util;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.druid.support.json.JSONUtils;

/**
 * ǰ������ݴ�����ܹ�����
 * @author
 *
 */
public class AesUtil {
	//�����õ�Constant�У�����ȡ�����ļ�ע��,16λ,�Լ�����
	private static final String KEY = "1234567887654321";

	//�����ֱ���� �㷨����/����ģʽ/������䷽ʽ
	private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

	/**
	 * ����
	 * @param content ���ܵ��ַ���
	 * @param encryptKey keyֵ
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String content, String encryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);
		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
		byte[] b = cipher.doFinal(content.getBytes("utf-8"));
		// ����base64�㷨����ת��,���������������
		return Base64.encodeBase64String(b);

	}

	/**
	 * ����
	 * @param encryptStr ���ܵ��ַ���
	 * @param decryptKey ���ܵ�keyֵ
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String encryptStr, String decryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);
		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
		// ����base64�㷨����ת��,���������������
		byte[] encryptBytes = Base64.decodeBase64(encryptStr);
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		return new String(decryptBytes,"utf-8");
	}

	public static String encrypt(String content) throws Exception {
		return encrypt(content, KEY);
	}
	public static String decrypt(String encryptStr) throws Exception {
		return decrypt(encryptStr, KEY);
	}


	public static void main(String[] args) throws Exception {
		Map map=new HashMap<String,String>();
		map.put("key","value");
		map.put("����","����");
		String content = JSONUtils.toJSONString(map);
		System.out.println("����ǰ��" + content);

		String encrypt = encrypt(content, KEY);
		System.out.println("���ܺ�" + encrypt);

		String decrypt = decrypt(encrypt, KEY);
		System.out.println("���ܺ�" + decrypt);
	}
}
