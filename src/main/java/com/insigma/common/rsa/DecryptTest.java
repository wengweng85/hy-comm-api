package com.insigma.common.rsa;


import java.util.HashMap;
import java.util.Map;

public class DecryptTest {

	public static void main(String[] args) {
		/**
		 * 数据解密
		 */
		//请求返回签名
		String  dateSign= "a6a14813d8635958da6428bcee3e33c2573d608e9da7ff17e04a9717ac53366bee00f71def052cde8d5afb3100447d153175a49a61f51c17f777e6bb0f443c1b";
		//请求或返回数据密文
		String requestParams = "L0z61Jej2DC1RBGIIPVN3e0YT0LCxcqJZZ0H4DFKMSVDDZOgqtqG0gLpeKGjGMYpAQl+4dSC+textX3nAWiK8PhP7Yys62kdK66GDWDLfXntN2HD1W/eQw7J1VSBO3zeLV1IhikGWHS4Bmo+aog3HQAjt67anBOWrOtgMFwvbyrf/g9v20sK6wVmrwc/jcGPatbKn8yADH8XKOuu8xdbsJjpnvAhr1C1Jf8I8GJ3rycGOmAIriMFu0FU8DZdM6+IgzQQlRikwzj4NvKA9ScetTUqiqFoxwzYXIEHmeTKyOs=";
		//rsa私钥
		String default_rsa_private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMWfWrEPD1lLQ/S67n+J6gEcOy1bNzp4YkFqso2OHlDfBpk9yB2rSa48QxW0FP92w7mXhhkuyNERNvJY0qgWUEe2GMYk/hfzG40uwvqm6dMje8evpiXyFhxKKzuDZ6uA78eDcZOblbI3ZWLeslsIFLS1HsFvr94uzyROukQKAeeDAgMBAAECgYEAq5ZUQV3pHsfqBij0gcp2YmZnv1bu2hbCgTgqNhxKmD89VHq/MXuS6vSp7sNAZBtzj8ZPibgEZvqCcePLbGSLaGHG4nvLuxkRUzLekBkKNGrCc2+qA3MmybDAmfYX1LMih/bG3TyBVJe8Jlqbc/fx3PF7YlaIGup0CDzHSqDL9zECQQDjb457PCH7Bg0D4yrBYWc/B6vzP3BPfjqhjgpNRyO4npN+JW/LEjis5/PmbzPH0Tto2XPVpLzgsz6Uhv36CAsJAkEA3nFACXdIyRLAKclfLvCYdodF0EC0+RnsJKNRBkA52TWf5eaGBynY+bsEr0qRw8swCoJRW9JpIJXCkcVcD8HlKwJBAKc/6CVTySFSB8wuB8kBme1N9PMCWdL7Xp5jV8wQXoJriscfPKC3G5p7UG7Ko+dzRB8MmhQn+wW6inCq/KYbuBECQGdjog0ESWtT8Mc7Vqddoq+szjEuci858j1RP261WW3w88IKtI8Nz1C7sUC8WyXzAgBQc/pGwSApG4dfgRu91oMCQEBpgQu2rDL+MvBm+pQtTbhOBHJeQaO0PRmOg6R1p5InGEK+u1RXaCdYGCifEoH8te2DKU0qRLP0I0LePPApjTc=";


		//校验报文格式
		String[] dataArr = requestParams.split(RSAUtils.SEPARATOR);
		if(dataArr.length != 2){
			System.out.println("解密错误，不是有效的密文格式");
		}
		//AES密文，AES(P)
		String aesCipherText = dataArr[0];
		//RSA密文，RSA(AES_KEY)
		String rsaCipherText = dataArr[1];

		//1 请求参数map
		Map<String,String> map = new HashMap<String,String>();

		map.put("aes", aesCipherText);
		map.put("rsa", rsaCipherText);

		String sign = SignUtils.createSign(map);

		System.out.println("现数据签名为:" + sign);

		System.out.println("数据摘要是否一致：" + dateSign.equalsIgnoreCase(sign));

		try{
			String aesKey = RSAUtils.decryptByPrivateKey(rsaCipherText, default_rsa_private_key);//rsa解密--私钥解密
			System.out.println("AES密钥为:" + aesKey);
			String reqparams = AESCBCUtils.decrypt(aesCipherText, aesKey).trim();//aes解密--数据明文
			System.out.println("解密后的数据明文为:" + reqparams);
			System.out.println(reqparams);
		}catch (Exception e){
			e.printStackTrace();
		}


	}
}
