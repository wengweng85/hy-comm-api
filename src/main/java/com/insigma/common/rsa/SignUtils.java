package com.insigma.common.rsa;

import com.insigma.common.util.RandomNumUtil;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * SignUtils
 */
public class SignUtils {

	private final static String KEY = "123456789F";

	private final static String ALGORITHM = "SHA-1";

	private final static String SIGN_SIGNATURE="signature";

	private final static String SIGN_TIMESTAMP="timestamp";

	private final static String SIGN_NONCE="nonce";

	//时间戳有效果期
	private static final long TIMESTAMP=1*60*1000;

	/**
	 * 根据给定摘要算法名称创建一个消息摘要实例
	 */
	static MessageDigest getDigest(String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		}
	}


	/**
	 * 获取 SHA-512 消息摘要实例
	 *
	 * @return SHA-512 消息摘要实例
	 * @throws RuntimeException 当 {@link NoSuchAlgorithmException} 发生时
	 */
	public static MessageDigest getSha512Digest() {
		return getDigest(ALGORITHM);
	}

	/**
	 * 获取摘要的十六进制字符串
	 * @param data
	 * @return 摘要的十六进制字符串
	 */
	public static String getHexString(byte[] data) {
		return Hex.encodeHexString(data);
	}


	public static String getKey(){
		return KEY;
	}


	/**
	 * 除去数组中的空值和签名参数
	 * @param sArray 签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {
		Map<String, String> result = new HashMap<String, String>();
		if (sArray == null || sArray.size() <= 0) {
			return result;
		}
		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
					|| key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			result.put(key, value);
		}
		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * @param params 需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			try {
				if(CharUtil.isChinese(value)){
					value = URLEncoder.encode(value, "UTF-8");
				}
			} catch (UnsupportedEncodingException e) {
				System.out.println(e.getMessage());
			}

			if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	/**
	 * 生成签名
	 * @Title: createSign
	 * @param paramMap
	 * @return
	 */
	public static String createSign(Map<String,String> paramMap){
		//1.对请求参数过滤，过滤掉sign_type和sign字段内容
		paramMap = paraFilter(paramMap);
		//2.生成带签名字符串
		String s = createLinkString(paramMap);
		//3.加上key
		s = s + getKey();
		byte[] shaByte = getSha512Digest().digest(s.getBytes());
		String ed = getHexString(shaByte);
		return ed;
	}


	/**
	 * 签名并生成地址栏目
	 * @return
	 */
	public static String signature() {
		HashMap map=new HashMap();
		String nonce= UUID.randomUUID().toString();
		String timestamp=new Long(System.currentTimeMillis()+TIMESTAMP).toString();
		map.put(SIGN_NONCE, nonce);
		map.put(SIGN_TIMESTAMP,timestamp);
		String sign=signature(timestamp,nonce);
		map.put(SIGN_SIGNATURE,sign);
		return createLinkString(map);
	}

	/**
	 * 签名
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	private static String signature(String timestamp, String nonce) {
		String[] arr = new String[]{KEY, timestamp, nonce};
		Arrays.sort(arr);//将token、timestamp、nonce三个参数进行字典序排序
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String sign = null;
		try {
			md = MessageDigest.getInstance(ALGORITHM);
			byte[] digest = md.digest(content.toString().getBytes());// 将三个参数字符串拼接成一个字符串进行sha1加密
			sign = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sign;
	}

	/**
	 * 验签
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		String[] arr = new String[]{KEY, timestamp, nonce};
		Arrays.sort(arr);//将token、timestamp、nonce三个参数进行字典序排序
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;
		try {
			md = MessageDigest.getInstance(ALGORITHM);
			byte[] digest = md.digest(content.toString().getBytes());// 将三个参数字符串拼接成一个字符串进行sha1加密
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	/**
	 * 将字节转换为十六进制字符串
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];
		String s = new String(tempArr);
		return s;
	}

	public static void main(String [] args){
		String param=signature();
		System.out.println(param);
	}



}