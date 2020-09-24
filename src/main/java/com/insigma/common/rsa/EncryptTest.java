package com.insigma.common.rsa;

import java.util.HashMap;
import java.util.Map;

public class EncryptTest {

	public static void main(String[] args) {
		/**
		 * ???????
		 */
		//aes???-16¦Ë???
		String aes_key = "JAL8eZQINC785YHF";
		//rsa???
		String default_rsa_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFn1qxDw9ZS0P0uu5/ieoBHDstWzc6eGJBarKNjh5Q3waZPcgdq0muPEMVtBT/dsO5l4YZLsjRETbyWNKoFlBHthjGJP4X8xuNLsL6punTI3vHr6Yl8hYcSis7g2ergO/Hg3GTm5WyN2Vi3rJbCBS0tR7Bb6/eLs8kTrpECgHngwIDAQAB";

		String plantText="{'idCard':'330382199405051146','name':'????','dateTime':'2018-01-22 14:22:45'}";

		String aes_str = AESCBCUtils.encrypt(plantText, aes_key); //aes????

		try{

		String rsa_str = RSAUtils.encryptByPublicKey(aes_key, default_rsa_public_key); //rsa????--???????

		//1 ???????map
		Map<String,String> map = new HashMap<String,String>();

		map.put("aes", aes_str);
		map.put("rsa", rsa_str);

		String sign = SignUtils.createSign(map);
		StringBuffer sb = new StringBuffer();

		sb.append(aes_str).append(RSAUtils.SEPARATOR).append(rsa_str);
		System.out.println("???????????" + sb.toString());
		System.out.println("??????????" + sign);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
