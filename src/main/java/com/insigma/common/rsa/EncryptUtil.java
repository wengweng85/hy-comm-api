package com.insigma.common.rsa;

import com.insigma.common.util.MD5Util;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;


/**
 *
 * 加密工具类证书生成通过rsautils方法生成
 * 此方法为提供给第三方开发使用加密及签名数据用
 * @author Administrator
 *
 */
public class EncryptUtil {

    /** 算法名称 */
    private static final String ALGORITHOM = "RSA";

    /** RSA_PUBLICKEY_FILENAME */
    private static final String RSA_PUBLICKEY_FILENAME = "/publicKey.keystore";

    public static final String KEY_ALGORITHM = "AES";
    // 加密模式
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";
    // 字符集
    public static final String ENCODING = "UTF-8";
    // 向量
    public static final String IV_SEED = "1234567812345678";

    private EncryptUtil() {
    }

    //AES KEY
    private static String AES_KEY;

    //RSA PUBLIC KEY
    public static String publickey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGHPzj0dzM4CmUGkXzpXytccN+mFNSlcFPerAM5QLZvqLHAjcGUT9iTeVGAG8RnD21lXXx++lHf5hSXO91RUANqElKaiQm/wRKTGFQvjyt42R7IU7ikpqOuYc+g3a5CEM9XzjuCHjOIiadVUKZo7Rndotc59Wq85XHGmNRMspXxwIDAQAB";

    /**
     * @FieldName: SEPARATOR
     * @FieldType: char
     * @Description: 分隔符'@'
     */
    public static final String SEPARATOR = String.valueOf((char) 64);

    static {
        try {
            AES_KEY = getAesKey();
            //publickey=readPublicKey();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * readPublicKey
     */
    public static String readPublicKey() throws  Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(getPublicKeyfilePath()));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }


    /**
     * 返回生成/读取的密钥对文件的路径。
     */
    private static String getPublicKeyfilePath() {
        String urlPath = EncryptUtil.class.getResource("/").getPath();
        String filepath=new File(urlPath) + RSA_PUBLICKEY_FILENAME;
        System.out.println("证书文件路径"+filepath);
        return filepath;
    }


    /**
     * 通过调用方法签名并加密相关数据
     * @param inputStr 要加密并签名的数据
     * @return 加密及签名结果数据
     * @throws Exception
     */
    public static String encryptAndSign(String inputStr) throws  Exception{
        String [] dataanssign= encryptByAesAndRsaPublickey(inputStr,publickey);
        return "data="+dataanssign[0]+"&sign="+dataanssign[1];
    }



    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        // 对公钥解密
        byte[] keyBytes = Base64.decodeBase64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHOM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        //return cipher.doFinal(data);
        // 加密时超过117字节就报错。为此采用分段加密的办法来加密
        byte[] enBytes = null;
        for (int i = 0; i < data.length; i += 64) {
            // 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i,i + 64));
            enBytes = ArrayUtils.addAll(enBytes, doFinal);
        }
        return enBytes;
    }


    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static String encryptByPublicKey(String data, String key) throws Exception {
        return Base64.encodeBase64String(encryptByPublicKey(data.getBytes(), key));
    }

    /**
     * 客户端加密
     * 使用aes对数据进行加密
     * 使用rsa对aeskey进行加密后拼装数据
     * @param plantText
     * @return 签名及加密数据数组 [0]加密数据 [1]md5签名
     * @throws Exception
     */
    public static String[] encryptByAesAndRsaPublickey(String plantText) throws Exception{
        return encryptByAesAndRsaPublickey(plantText,readPublicKey());
    }

    /**
     * 客户端加密
     * 使用aes对数据进行加密
     * 使用rsa对aeskey进行加密后拼装数据
     * @param plantText
     * @param publickey
     * @return 签名及加密数据数组 [0]加密数据 [1]md5签名
     * @throws Exception
     */
    public static String[] encryptByAesAndRsaPublickey(String plantText,String publickey) throws Exception{
        //AES_KEY = getAesKey();
        String [] dataandsign=new String[2];
        String aes_str = aesEncrypt(plantText, AES_KEY); //aes密文
        String rsa_str = encryptByPublicKey(AES_KEY,publickey); //rsa密文--公钥加密
        StringBuffer sb = new StringBuffer();
        sb.append(aes_str).append(EncryptUtil.SEPARATOR).append(rsa_str);
        String data= sb.toString();
        String sign= MD5Util.MD5Encode(plantText);
        dataandsign[0]=data;
        dataandsign[1]=sign;
        return dataandsign;
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param encodedata
     * @param key
     * @return
     * @throws Exception
     */
    private static String decryptByPublicKey(String encodedata, String key) throws Exception {
        return  new String(decryptByPublicKey(Base64.decodeBase64(encodedata),key));
    }


    /**
     * 解密<br>
     * 用解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64.decodeBase64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHOM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        // return cipher.doFinal(data);

        byte[] enBytes = null;
        // 解密时超过128字节就报错。为此采用分段解密的办法来解密
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i += 128) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + 128));
            enBytes = ArrayUtils.addAll(enBytes, doFinal);
        }
        return enBytes;
    }

    /**
     * 客户端解密
     * 使用rsa对aeskey进行解密出aes_key
     * 使用aes对数据进行解密
     * @param responseParams 返回的数据密文
     * @return
     * @throws Exception
     */
    private static String decryptByAesAndRsaPublickey(String responseParams) throws Exception{
       return decryptByAesAndRsaPublickey(responseParams,readPublicKey());
    }

    /**
     * 客户端解密
     * 使用rsa对aeskey进行解密出aes_key
     * 使用aes对数据进行解密
     * @param responseParams 返回的数据密文
     * @param publickey
     * @return
     * @throws Exception
     */
    public static String decryptByAesAndRsaPublickey(String responseParams,String publickey) throws Exception{
        //校验报文格式
        String[] dataArr = responseParams.split(EncryptUtil.SEPARATOR);
        if(dataArr.length != 2){
            throw new Exception("解密错误，不是有效的密文格式");
        }
        //AES密文，AES(P)
        String aesCipherText = dataArr[0];
        //RSA密文，RSA(AES_KEY)
        String rsaCipherText = dataArr[1];
        String aesKey = decryptByPublicKey(rsaCipherText,publickey);//rsa解密--私钥解密
        //System.out.println("AES密钥为:" + aesKey);
        String reqparams = aesDecrypt(aesCipherText, aesKey).trim();//aes解密--数据明文
        //System.out.println("解密后的数据明文为:" + reqparams);
        return reqparams;
    }

    /**
     * AES加密算法
     *
     * @param str 密文
     * @param key 密key
     * @return
     */
    public static String aesEncrypt(String str, String key) {
        try {
            if (str == null) {
                System.out.println("AES加密出错:Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (key.length() != 16) {
                System.out.println("AES加密出错:Key长度不是16位");
                return null;
            }
            byte[] raw = key.getBytes(ENCODING);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_SEED.getBytes(ENCODING));
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] srawt = str.getBytes(ENCODING);
			/*int len = srawt.length;
			*//* 计算补空格后的长度 *//*
			while (len % 16 != 0)
				len++;
			byte[] sraw = new byte[len];
			*//* 在最后空格 *//*
			for (int i = 0; i < len; ++i) {
				if (i < srawt.length) {
					sraw[i] = srawt[i];
				} else {
					sraw[i] = 32;
				}
			}*/
            byte[] encrypted = cipher.doFinal(srawt);
            return formatString(new String(Base64.encodeBase64(encrypted), "UTF-8"));
        } catch (Exception ex) {
            System.out.println("AES加密出错：" + ex.toString());
            return null;
        }
    }

    /**
     * AES解密算法
     *
     * @param str 密文
     * @param key 密key
     * @return
     */
    public static String aesDecrypt(String str, String key) {
        try {
            // 判断Key是否正确
            if (key == null) {
                System.out.println("AES解密出错:Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (key.length() != 16) {
                System.out.println("AES解密出错：Key长度不是16位");
                return null;
            }
            byte[] raw = key.getBytes(ENCODING);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_SEED.getBytes(ENCODING));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] bytes = Base64.decodeBase64(str.getBytes("UTF-8"));
            bytes = cipher.doFinal(bytes);
            return new String(bytes, ENCODING);
        } catch (Exception ex) {
            System.out.println("AES解密出错：" + ex.toString());
            return null;
        }
    }


    private static String formatString(String sourceStr) {
        if (sourceStr == null) {
            return null;
        }
        return sourceStr.replaceAll("\\r", "").replaceAll("\\n", "");
    }

    /**
     * 随机生成秘钥
     */
    private static String getAesKey() throws  NoSuchAlgorithmException{
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256
        SecretKey sk = kg.generateKey();
        byte[] b = sk.getEncoded();
        String s = byteToHexString(b);
        //System.out.println(s);
        //System.out.println("十六进制密钥长度为"+s.length());
        //System.out.println("二进制密钥的长度为"+s.length()*4);
        return s;
    }

    /*
     * byte数组转化为16进制字符串
     * @param bytes
     * @return
     */
    private static String byteToHexString(byte[] bytes){
        String hs = "";
        String stmp = "";
        for (int n = 0; n < bytes.length; n++) {
            stmp = (Integer.toHexString(bytes[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase().substring(0,16);
    }


    /**
     * main
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        String inputStr = "{\"aac002\":\"420624198411037915\"}";
        System.out.println(encryptAndSign(inputStr));
    }
}