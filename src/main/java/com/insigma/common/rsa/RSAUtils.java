package com.insigma.common.rsa;

import com.insigma.common.util.MD5Util;
import com.insigma.resolver.AppException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * RSA算法加密/解密工具类
 * @author Administrator
 *
 */
public class RSAUtils {

    /** 算法名称 */
    private static final String ALGORITHOM = "RSA";

    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";

    private static final String PRIVATE_KEY = "RSAPrivateKey";

    private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtils.class);

    /** RSA_PUBLICKEY_FILENAME */
    private static final String RSA_PUBLICKEY_FILENAME = "/publicKey.keystore";

    /** RSA_PRIVATE_FILENAME */
    private static final String RSA_PRIVATE_FILENAME = "/privateKey.keystore";

    /**保存生成的密钥对的文件名称 对象。 */
    private static final String RSA_PAIR_KEYSTORE_FILENAME = "/__RSA_PAIR.keystore";

    /*默认秘钥*/
    private static final String DEFUALT_PRIVATE_KEY="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIYc/OPR3MzgKZQaRfOlfK1xw36YU1KVwU96sAzlAtm+oscCNwZRP2JN5UYAbxGcPbWVdfH76Ud/mFJc73VFQA2oSUpqJCb/BEpMYVC+PK3jZHshTuKSmo65hz6DdrkIQz1fOO4IeM4iJp1VQpmjtGd2i1zn1arzlccaY1EyylfHAgMBAAECgYABs1ZlkSCqnGEKlrayWzPUgy/GaCoOTwXPey/GShUaK7emrFmEQ/14wqIYnCLMZ13E8qs3MUmI9Y455SHIK+OfBAEI4jApE+8eSJ8LsfUd60pswSKtH/2XRVMwLJej6OJqR0FCy3CaA7etj/KvdTi7uLldIZ8tSOCq2+BjKdm8IQJBAMUK2GH9Eq/KLoYJi1zDc7RZG0pHwYJdNCYLfRuRII3w2h/1gUpBHV9vbsECpxS+kkqGyW8OA6Fc3hrWqru/L9ECQQCuPd0rQhtdpT9IPmmTvuMvPx1vE/zQjujtS9oeU7oz+8We55taSH5ytsmmWWRDHjTWqIKIZWkTZG2bC49Je0wXAkAs2ikjNP458aXhcO6+MOd3mAj0QZ001Y53Uoop6kEkzjx4pePGSUgsXysw2C+8Mx0Nxdy4YNJGuuL77P10OzLhAkADp9qfELkAQvpL6rtOVT/w+tMERJgWTBlI+UFvR3RtqMehqNxSjZjRkVIzwkZfPh//rPNoJzCILqA6E4kDEqorAkA6kK/zQc9kLeAZzgmHrsrSPpJSmmfolox7zEibgwxnfs2jeeGfhzguDCocuHCfSzcZbVigW7kRNs+mzew4sYen";

    /*默认公钥*/
    private static final String DEFUALT_PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGHPzj0dzM4CmUGkXzpXytccN+mFNSlcFPerAM5QLZvqLHAjcGUT9iTeVGAG8RnD21lXXx++lHf5hSXO91RUANqElKaiQm/wRKTGFQvjyt42R7IU7ikpqOuYc+g3a5CEM9XzjuCHjOIiadVUKZo7Rndotc59Wq85XHGmNRMspXxwIDAQAB";


    /** 密钥大小 */
    private static final int KEY_SIZE = 1024;
    /** 默认的安全服务提供者 */
    private static final Provider DEFAULT_PROVIDER = new BouncyCastleProvider();

    private static KeyPairGenerator keyPairGen = null;
    private static KeyFactory keyFactory = null;
    /** 缓存的密钥对。 */
    private static KeyPair oneKeyPair = null;

    private static File rsaPairFile = null;

    //AES 密码
    private static String AES_PASSWORD = "WXJYEPSOFT";

    //AES KEY
    private static String AES_KEY;

    //时间戳-有效期
    private static final long TIMESTAMP=5*60*1000;

    /**
     * @FieldName: SEPARATOR
     * @FieldType: char
     * @Description: 分隔符'@'
     */
    public static final String SEPARATOR = String.valueOf((char) 64);

    static {
        try {
            keyPairGen = KeyPairGenerator.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
            keyFactory = KeyFactory.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
            //aes_key获取
            AES_KEY=getAesKey();
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error(ex.getMessage());
        }
        rsaPairFile = new File(getRSAPairFilePath());

    }

    private RSAUtils() {
    }

    /**
     * 生成并返回RSA密钥对。
     */
    private static synchronized KeyPair generateKeyPair() {
        try {
            keyPairGen.initialize(KEY_SIZE, new SecureRandom(DateFormatUtils.format(new Date(),"yyyyMMdd").getBytes()));
            oneKeyPair = keyPairGen.generateKeyPair();
            saveKeyPair(oneKeyPair);
            return oneKeyPair;
        } catch (InvalidParameterException ex) {
            LOGGER.error("KeyPairGenerator does not support a key length of " + KEY_SIZE + ".", ex);
        } catch (NullPointerException ex) {
            LOGGER.error("RSAUtil#KEY_PAIR_GEN is null, can not generate KeyPairGenerator instance.",ex);
        }
        return null;
    }

    /**
     * 返回生成/读取的密钥对文件的路径。
     */
    private static String getRSAPairFilePath() {
        String urlPath = RSAUtils.class.getResource("/").getPath();
        return (new File(urlPath).getParent()+RSA_PAIR_KEYSTORE_FILENAME);
    }

    /**
     * 若需要创建新的密钥对文件，则返回 {@code true}，否则 {@code false}。
     */
    private static boolean isCreateKeyPairFile() {
        // 是否创建新的密钥对文件
        boolean createNewKeyPair = false;
        if (!rsaPairFile.exists() || rsaPairFile.isDirectory()) {
            createNewKeyPair = true;
        }
        return createNewKeyPair;
    }

    /**
     * 将指定的RSA密钥对以文件形式保存。
     *
     * @param keyPair 要保存的密钥对。
     */
    private static void saveKeyPair(KeyPair keyPair) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = FileUtils.openOutputStream(rsaPairFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(keyPair);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(fos);
        }
    }



    /**
     * 将指定的RSA密钥对以文件形式保存 保存为文本格式
     * @param publicKey
     * @param privateKey
     */
    private static void saveKeyPair(String publicKey, String privateKey) {
        Writer out = null;
        try {
            out = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(new File(RSA_PUBLICKEY_FILENAME)),"GBK"));
            out.write(publicKey);
            out.close();
            out = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(new File(RSA_PRIVATE_FILENAME)),"GBK"));
            out.write(privateKey);
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 返回RSA密钥对。
     */
    public static KeyPair getKeyPair() {
        // 首先判断是否需要重新生成新的密钥对文件
        if (isCreateKeyPairFile()) {
            // 直接强制生成密钥对文件，并存入缓存。
            return generateKeyPair();
        }
        if (oneKeyPair != null) {
            return oneKeyPair;
        }
        return readKeyPair();
    }

    // 同步读出保存的密钥对
    public static KeyPair readKeyPair() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = FileUtils.openInputStream(rsaPairFile);
            ois = new ObjectInputStream(fis);
            oneKeyPair = (KeyPair) ois.readObject();
            return oneKeyPair;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(fis);
        }
        return null;
    }

    /**
     * 将指定的RSA密钥对以文件形式保存。
     *
     * @return  keyPair 保存的密钥对。
     */
    public static KeyPair readKeyPair(String keyfilepath) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        KeyPair keyPair=null;
        try {
            fis = FileUtils.openInputStream(new File(keyfilepath));
            ois = new ObjectInputStream(fis);
            keyPair=(KeyPair)ois.readObject();
            return keyPair;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(ois);
        }
        return keyPair;
    }

    /**
     * 根据给定的系数和专用指数构造一个RSA专用的公钥对象。
     *
     * @param modulus 系数。
     * @param publicExponent 专用指数。
     * @return RSA专用公钥对象。
     */
    public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) {
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
        try {
            return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException ex) {
            LOGGER.error("RSAPublicKeySpec is unavailable.", ex);
        } catch (NullPointerException ex) {
            LOGGER.error("RSAUtil#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
        }
        return null;
    }

    /**
     * 根据给定的系数和专用指数构造一个RSA专用的私钥对象。
     *
     * @param modulus 系数。
     * @param privateExponent 专用指数。
     * @return RSA专用私钥对象。
     */
    public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) {
        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus),
                new BigInteger(privateExponent));
        try {
            return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
        } catch (InvalidKeySpecException ex) {
            LOGGER.error("RSAPrivateKeySpec is unavailable.", ex);
        } catch (NullPointerException ex) {
            LOGGER.error("RSAUtil#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
        }
        return null;
    }

    /**
     * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的私钥对象。
     *
     * @param hexModulus 系数。
     * @param hexPrivateExponent 专用指数。
     * @return RSA专用私钥对象。
     */
    public static RSAPrivateKey getRSAPrivateKey(String hexModulus, String hexPrivateExponent) {
        if(StringUtils.isBlank(hexModulus) || StringUtils.isBlank(hexPrivateExponent)) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("hexModulus and hexPrivateExponent cannot be empty. RSAPrivateKey value is null to return.");
            }
            return null;
        }
        byte[] modulus = null;
        byte[] privateExponent = null;
        try {
            modulus = Hex.decodeHex(hexModulus.toCharArray());
            privateExponent = Hex.decodeHex(hexPrivateExponent.toCharArray());
        } catch(DecoderException ex) {
            LOGGER.error("hexModulus or hexPrivateExponent value is invalid. return null(RSAPrivateKey).");
        }
        if(modulus != null && privateExponent != null) {
            return generateRSAPrivateKey(modulus, privateExponent);
        }
        return null;
    }

    /**
     * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的公钥对象。
     *
     * @param hexModulus 系数。
     * @param hexPublicExponent 专用指数。
     * @return RSA专用公钥对象。
     */
    public static RSAPublicKey getRSAPublidKey(String hexModulus, String hexPublicExponent) {
        if(StringUtils.isBlank(hexModulus) || StringUtils.isBlank(hexPublicExponent)) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("hexModulus and hexPublicExponent cannot be empty. return null(RSAPublicKey).");
            }
            return null;
        }
        byte[] modulus = null;
        byte[] publicExponent = null;
        try {
            modulus = Hex.decodeHex(hexModulus.toCharArray());
            publicExponent = Hex.decodeHex(hexPublicExponent.toCharArray());
        } catch(DecoderException ex) {
            LOGGER.error("hexModulus or hexPublicExponent value is invalid. return null(RSAPublicKey).");
        }
        if(modulus != null && publicExponent != null) {
            return generateRSAPublicKey(modulus, publicExponent);
        }
        return null;
    }

    /**
     * 使用指定的公钥加密数据。
     *
     * @param publicKey 给定的公钥。
     * @param data 要加密的数据。
     * @return 加密后的数据。
     */
    public static byte[] encrypt(PublicKey publicKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
        ci.init(Cipher.ENCRYPT_MODE, publicKey);
        return ci.doFinal(data);
    }

    /**
     * 使用指定的私钥解密数据。
     *
     * @param privateKey 给定的私钥。
     * @param data 要解密的数据。
     * @return 原数据。
     */
    public static byte[] decrypt(PrivateKey privateKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
        ci.init(Cipher.DECRYPT_MODE, privateKey);
        return ci.doFinal(data);
    }

    /**
     * 使用给定的公钥加密给定的字符串。
     * <p />
     * 若 {@code publicKey} 为 {@code null}，或者 {@code plaintext} 为 {@code null} 则返回 {@code
     * null}。
     *
     * @param publicKey 给定的公钥。
     * @param plaintext 字符串。
     * @return 给定字符串的密文。
     */
    public static String encryptString(PublicKey publicKey, String plaintext) {
        if (publicKey == null || plaintext == null) {
            return null;
        }
        byte[] data = plaintext.getBytes();
        try {
            byte[] en_data = encrypt(publicKey, data);
            return new String(Hex.encodeHex(en_data));
        } catch (Exception ex) {
            LOGGER.error(ex.getCause().getMessage());
        }
        return null;
    }

    /**
     * 使用默认的公钥加密给定的字符串。
     * <p />
     * 若{@code plaintext} 为 {@code null} 则返回 {@code null}。
     *
     * @param plaintext 字符串。
     * @return 给定字符串的密文。
     */
    public static String encryptString(String plaintext) {
        if(plaintext == null) {
            return null;
        }
        byte[] data = plaintext.getBytes();
        KeyPair keyPair = getKeyPair();
        try {
            byte[] en_data = encrypt((RSAPublicKey)keyPair.getPublic(), data);
            return new String(Hex.encodeHex(en_data));
        } catch(NullPointerException ex) {
            LOGGER.error("keyPair cannot be null.");
        } catch(Exception ex) {
            LOGGER.error(ex.getCause().getMessage());
        }
        return null;
    }

    /**
     * 使用给定的私钥解密给定的字符串。
     * <p />
     * 若私钥为 {@code null}，或者 {@code encrypttext} 为 {@code null}或空字符串则返回 {@code null}。
     * 私钥不匹配时，返回 {@code null}。
     *
     * @param privateKey 给定的私钥。
     * @param encrypttext 密文。
     * @return 原文字符串。
     */
    public static String decryptString(PrivateKey privateKey, String encrypttext) {
        if (privateKey == null || StringUtils.isBlank(encrypttext)) {
            return null;
        }
        try {
            byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
            byte[] data = decrypt(privateKey, en_data);
            return new String(data);
        } catch (Exception ex) {
            LOGGER.error(String.format("\"%s\" Decryption failed. Cause: %s", encrypttext, ex.getCause().getMessage()));
        }
        return null;
    }

    /**
     * 使用默认的私钥解密给定的字符串。
     * <p />
     * 若{@code encrypttext} 为 {@code null}或空字符串则返回 {@code null}。
     * 私钥不匹配时，返回 {@code null}。
     *
     * @param encrypttext 密文。
     * @return 原文字符串。
     */
    public static String decryptString(String encrypttext) {
        if(StringUtils.isBlank(encrypttext)) {
            return null;
        }
        KeyPair keyPair = getKeyPair();
        try {
            byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
            byte[] data = decrypt((RSAPrivateKey)keyPair.getPrivate(), en_data);
            return new String(data);
        } catch(NullPointerException ex) {
            LOGGER.error("keyPair cannot be null.");
        } catch (Exception ex) {
            LOGGER.error(String.format("\"%s\" Decryption failed. Cause: %s", encrypttext, ex.getMessage()));
        }
        return null;
    }

    /**
     * 使用默认的私钥解密由JS加密（使用此类提供的公钥加密）的字符串。
     *
     * @param encrypttext 密文。
     * @return {@code encrypttext} 的原文字符串。
     */
    public static String decryptStringByJs(String encrypttext) {
        String text = decryptString(encrypttext);
        if(text == null) {
            return null;
        }
        return text;
    }

    /** 返回已初始化的默认的公钥。*/
    public static RSAPublicKey getDefaultPublicKey() {
        KeyPair keyPair = getKeyPair();
        if(keyPair != null) {
            return (RSAPublicKey)keyPair.getPublic();
        }
        return null;
    }

    /** 返回已初始化的默认的私钥。*/
    public static RSAPrivateKey getDefaultPrivateKey() {
        KeyPair keyPair = getKeyPair();
        if(keyPair != null) {
            return (RSAPrivateKey)keyPair.getPrivate();
        }
        return null;
    }

    /** 设置public key 。*/
    public static PublicKeyMap getPublicKeyMap() {
        PublicKeyMap publicKeyMap = new PublicKeyMap();
        RSAPublicKey rsaPublicKey = getDefaultPublicKey();
        publicKeyMap.setModulus(new String(Hex.encodeHex(rsaPublicKey.getModulus().toByteArray())));
        publicKeyMap.setExponent(new String(Hex.encodeHex(rsaPublicKey.getPublicExponent().toByteArray())));
        return publicKeyMap;
    }
    /**
     * 将publickey 设置到map中
     * @param map
     */
    @SuppressWarnings({ "rawtypes", "unused", "unchecked" })
    public static void getPublicKeyMap(Map map) {
        PublicKeyMap publicKeyMap = new PublicKeyMap();
        RSAPublicKey rsaPublicKey = getDefaultPublicKey();
        map.put("publicKeyExponent", new String(Hex.encodeHex(rsaPublicKey.getPublicExponent().toByteArray())));
        map.put("publicKeyModulus", new String(Hex.encodeHex(rsaPublicKey.getModulus().toByteArray())));
    }

    /**
     * 用私钥对信息生成数字签名
     * @param data 加密数据
     * @param privateKey
     * 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHOM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }

    /**
     * 用私钥对信息生成数字签名
     * @param data 加密数据
     * @param privateKey
     * 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, PrivateKey privateKey) throws Exception {
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data 加密数据
     * @param publicKey 公钥
     * @param sign 数字签名
     *
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     *
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        // 解密由base64编码的公钥
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHOM);
        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        // 验证签名是否正常
        return signature.verify(Base64.decodeBase64(sign));
    }

    /**
     * 校验数字签名
     *
     * @param data 加密数据
     * @param publicKey 公钥
     * @param sign 数字签名
     *
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     *
     */
    public static boolean verify(byte[] data, PublicKey publicKey, String sign) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data);
        // 验证签名是否正常
        return signature.verify(Base64.decodeBase64(sign));
    }

    /**
     * 解密<br>
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64.decodeBase64(key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHOM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        //return cipher.doFinal(data);
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
     * 解密<br>
     *
     * @param encodedata
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String encodedata, String key) throws Exception {
        return new String(decryptByPrivateKey(Base64.decodeBase64(encodedata),key));
    }

    /**
     * 解密<br>
     *
     * @param encodedata
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String encodedata) throws Exception {
        return new String(decryptByPrivateKey(Base64.decodeBase64(encodedata),readPrivateKey()));
    }

    /**
     * 解密<br>
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        //return cipher.doFinal(data);
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
     * 解密<br>
     * 用私钥解密
     *
     * @param encodedata
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String encodedata, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        return  new String(decryptByPublicKey(Base64.decodeBase64(encodedata),key));
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param encodedata
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String encodedata) throws Exception {
        return  new String(decryptByPublicKey(Base64.decodeBase64(encodedata),readPublicKey()));
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
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
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
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
    public static String encryptByPublicKey(String data, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        return Base64.encodeBase64String(encryptByPublicKey(data.getBytes(), key));
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data) throws Exception {
        return Base64.encodeBase64String(encryptByPublicKey(data.getBytes(), readPublicKey()));
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data,PublicKey publicKey) throws Exception {
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
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String key) throws Exception {
        return Base64.encodeBase64String(encryptByPrivateKey(data.getBytes(), key));
    }

    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data) throws Exception {
        return Base64.encodeBase64String(encryptByPrivateKey(data.getBytes(), readPrivateKey()));
    }

    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64.decodeBase64(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHOM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        //return cipher.doFinal(data);
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
     * 用私钥加密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        //return cipher.doFinal(data);
        byte[] enBytes = null;
        for (int i = 0; i < data.length; i += 64) {
            // 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i,i + 64));
            enBytes = ArrayUtils.addAll(enBytes, doFinal);
        }
        return enBytes;
    }

    /**
     * 返回生成/读取的密钥对文件的路径。
     */
    private static String getPublicKeyfilePath() {
        String urlPath = RSAUtils.class.getResource("/").getPath();
        String filepath=new File(urlPath).getParent() + RSA_PUBLICKEY_FILENAME;
        System.out.println("key path:"+filepath);
        return filepath;
    }

    /**
     * readPrivateKey
     */
    public static String readPrivateKey() throws  Exception {
        /*try {
            BufferedReader br = new BufferedReader(new FileReader(getPrivateKeyfilePath()));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            throw new Exception("read key error:"+e.getMessage());
        }*/
        return DEFUALT_PRIVATE_KEY;
    }


    /**
     * readPublicKey
     */
    public static String readPublicKey(){
        return DEFUALT_PUBLIC_KEY;
    }

    /**
     *
     * @param keyfilepath 证书文件路径
     * @return PublicKey 保存的密钥对。
     */
    public static String readKey(String keyfilepath) throws  Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(keyfilepath));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            throw new Exception("read key error:"+e.getMessage());
        }
    }

    /**
     * 返回生成/读取的密钥对文件的路径。
     */
    private static String getPrivateKeyfilePath() {
        String urlPath = RSAUtils.class.getResource("/").getPath();
        String filepath=new File(urlPath).getParent() + RSA_PRIVATE_FILENAME;
        System.out.println("key path:"+filepath);
        return filepath;
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHOM);
        keyPairGen.initialize(1024);

        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        saveKeyPair(keyPair);

        Map<String, Object> keyMap = new HashMap<String, Object>(2);

        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        String privateKeyString = RSAUtils.getPrivateKey(keyMap);
        String publicKeyString = RSAUtils.getPublicKey(keyMap);
        System.out.print("公钥Base64编码:");
        System.out.println(publicKeyString);
        System.out.print("私钥Base64编码:");
        System.out.println(""+privateKeyString);
        saveKeyPair(publicKeyString,privateKeyString);

        return keyMap;
    }


    /**
     * 客户端加密及签名
     * 使用aes对数据进行加密
     * 使用rsa对aeskey进行加密后拼装数据
     * @param plantText 明文数据
     * @param publickey 公钥
     * @return 签名及加密数据
     * @throws Exception
     */
    public static EncryptDataStruct encryptByAesAndRsaPublickey(String plantText,String publickey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        //客户端动态生成aeskey
        String client_aes_key=getAesKey();
        RSAUtils.EncryptDataStruct encryptDataStruct=new RSAUtils.EncryptDataStruct();
        //加密
        String aes_str = AESCBCUtils.encrypt(plantText, client_aes_key); //aes密文
        String rsa_str = RSAUtils.encryptByPublicKey(client_aes_key,publickey); //rsa密文--公钥加密
        StringBuffer sb = new StringBuffer();
        long timestamp=System.currentTimeMillis()+TIMESTAMP;
        sb.append(aes_str).append(RSAUtils.SEPARATOR).append(rsa_str).append(RSAUtils.SEPARATOR).append(timestamp);//加时间戳
        String data= sb.toString();
        //对数据签名=文本+当前时间戳
        String sign= MD5Util.MD5Encode(plantText+timestamp);
        encryptDataStruct.setData(data);
        encryptDataStruct.setSign(sign);
        return encryptDataStruct;
    }


    /**
     * 客户端加密及签名
     * 使用aes对数据进行加密
     * 使用rsa对aeskey进行加密后拼装数据
     * @param plantText 明文数据
     * @return 签名及加密数据
     * @throws Exception
     */
    public static EncryptDataStruct encryptByAesAndRsaPublickey(String plantText) throws Exception{
        return encryptByAesAndRsaPublickey(plantText,DEFUALT_PUBLIC_KEY);
    }
    /**
     * 服务端加密
     * 使用aes对数据进行加密
     * 使用rsa对aeskey进行加密后拼装数据
     * @param plantText
     * @privatekey
     * @return
     * @throws Exception
     */
    public static String encryptByAesAndRsaPrivateKey(String plantText,String privatekey) throws Exception{
        //服务端动态生成aeskey
        String server_aes_key=getAesKey();
        String aes_str = AESCBCUtils.encrypt(plantText, server_aes_key); //aes密文
        String rsa_str = RSAUtils.encryptByPrivateKey(server_aes_key,privatekey); //rsa密文--公钥加密
        StringBuffer sb = new StringBuffer();
        sb.append(aes_str).append(RSAUtils.SEPARATOR).append(rsa_str);
        return sb.toString();
    }

    /**
     * 服务端加密
     * 使用aes对数据进行加密
     * 使用rsa对aeskey进行加密后拼装数据
     * @param plantText
     * @privatekey
     * @return
     * @throws Exception
     */
    public static String encryptByAesAndRsaPrivateKey(String plantText) throws Exception{
        return encryptByAesAndRsaPrivateKey(plantText,DEFUALT_PRIVATE_KEY);
    }




    /**
     * 服务端解密及验签
     * 使用rsa对aeskey进行解密出aes_key
     * 使用aes对数据进行解密
     * @param encryptDataStruct //请求或返回数据密文
     * @param privatekey
     * @return
     * @throws Exception
     */
    public static  String decryptByAesAndRsaPrivateKey(EncryptDataStruct encryptDataStruct, String privatekey) throws Exception{
        //校验报文格式
        String[] dataArr = encryptDataStruct.getData().split(RSAUtils.SEPARATOR);
        if(dataArr.length != 3){
            throw new AppException("api request data is incorrect,we need 3 param");
        }
        long timestamp=Long.parseLong(dataArr[2]);
        //计算接口时间是否过期
        Date nowTime = new Date(System.currentTimeMillis());
        Date timestamp_time= new Date(timestamp);
        //System.out.println("nowTime="+nowTime.toString());
        //System.out.println("timestamp_time="+timestamp_time.toString());
        if(timestamp_time.after(nowTime)){
            //AES密文，AES(P)
            String aesCipherText = dataArr[0];
            //RSA密文，RSA(AES_KEY)
            String rsaCipherText = dataArr[1];

            String aesKey = RSAUtils.decryptByPrivateKey(rsaCipherText,privatekey);//rsa解密--私钥解密
            //System.out.println("AES key :" + aesKey);
            String reqparams = AESCBCUtils.decrypt(aesCipherText, aesKey).trim();//aes解密--数据明文

            //System.out.println("server reqparams:" + reqparams);
            String sign=MD5Util.MD5Encode(reqparams+timestamp);
            //System.out.println("server sign:" + sign);

            //判断签名是否正确
            if(sign.equals(encryptDataStruct.getSign())) {
                //System.out.println("数字签名验证成功！");
                //System.out.println("解密后的数据明文为:" + reqparams);
                return reqparams;
            }
            else{
                throw new Exception("sign error");
            }
        }else{
            throw new AppException("timestamp is out of date,api request is not valid");
        }

    }

    /**
     * 服务端解密及验签
     * 使用rsa对aeskey进行解密出aes_key
     * 使用aes对数据进行解密
     * @param encryptDataStruct
     * @return
     * @throws Exception
     */
    public static  String decryptByAesAndRsaPrivateKey(EncryptDataStruct encryptDataStruct) throws Exception{
        return decryptByAesAndRsaPrivateKey(encryptDataStruct,DEFUALT_PRIVATE_KEY);
    }

    /**
     * 服务端解密
     * 使用rsa对aeskey进行解密出aes_key
     * 使用aes对数据进行解密
     * @param data  加密数据
     * @param sign  签名数据
     * @return
     * @throws Exception
     */
    public static  String decryptByAesAndRsaPrivateKey(String data,String sign) throws Exception{
        RSAUtils.EncryptDataStruct encryptDataStruct=new RSAUtils.EncryptDataStruct();
        encryptDataStruct.setData(data);
        encryptDataStruct.setSign(sign);
        return decryptByAesAndRsaPrivateKey(encryptDataStruct,DEFUALT_PRIVATE_KEY);
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
    public static String decryptByAesAndRsaPublickey(String responseParams,String publickey) throws AppException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        if(null!=responseParams&&!responseParams.equals("")){
            //校验报文格式
            String[] dataArr = responseParams.split(RSAUtils.SEPARATOR);
            if(dataArr.length != 2){
                throw new AppException("api request data is incorrect,we need 2 param");
            }
            //AES密文，AES(P)
            String aesCipherText = dataArr[0];
            //RSA密文，RSA(AES_KEY)
            String rsaCipherText = dataArr[1];
            String aesKey = RSAUtils.decryptByPublicKey(rsaCipherText,publickey);//rsa解密--私钥解密
            //System.out.println("AES密钥为:" + aesKey);
            String reqparams = AESCBCUtils.decrypt(aesCipherText, aesKey).trim();//aes解密--数据明文
            //System.out.println("解密后的数据明文为:" + reqparams);
            return reqparams;
        }
        return responseParams;
    }



    /**
     * 客户端解密
     * 使用rsa对aeskey进行解密出aes_key
     * 使用aes对数据进行解密
     * @param responseParams 返回的数据密文
     * @return
     * @throws Exception
     */
    public static String decryptByAesAndRsaPublickey(String responseParams) throws Exception{
        return decryptByAesAndRsaPublickey(responseParams,DEFUALT_PUBLIC_KEY);
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
        //s="8NONwyJtHesysWpM";
        //System.out.println("aes="+s);
        return s;
    }

    /**
     * 使用指定的字符串生成秘钥
     */
    private static String getAesKeyByPass() throws NoSuchAlgorithmException{
        //生成秘钥
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        // kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256
        //SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以生成的秘钥就一样。
        kg.init(128, new SecureRandom(AES_PASSWORD.getBytes()));
        SecretKey sk = kg.generateKey();
        byte[] b = sk.getEncoded();
        String s = byteToHexString(b);
        //System.out.println(s);
        //System.out.println("十六进制密钥长度为"+s.length());
        //System.out.println("二进制密钥的长度为"+s.length()*4);
        System.out.println("aes="+s);
        return s;

    }
    /**
     * byte数组转化为16进制字符串
     * @param bytes
     * @return
     */
    private static String byteToHexString(byte[] bytes){
        String hs = "";
        String stmp = "";
        for (int n = 0; n < bytes.length; n++) {
            stmp = (java.lang.Integer.toHexString(bytes[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase().substring(0,16);
    }

    /**
     * Created by wengsh
     * 加密数据格式
     * {
     *     data:'基于rsa以及aes算法加密', rsa以及aes加密串以@符号分割形如aes_key_encode@rsa_key_encode
     *     sign:'基于md5算法签名'
     * }
     *
     */
    public static class EncryptDataStruct {

        public EncryptDataStruct(){

        }
        private String data;
        private String sign;



        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }


}