package com.insigma.common.rsa;

import net.sf.json.JSONObject;

/**
 * admin
 */
public class RSAUtilsTest {

    public static  void main2(String [] a){
        try{
            //1.
            //生成证书
            RSAUtils.initKey();
            //2.
            //要加密的字符串
            String inputStr = "{\"aac002\":\"420624198411037915\"}";
            //从文件中获取公钥
            String publickey=RSAUtils.readKey("d:/publicKey.keystore");
            //加密数据base64
            String encodedata=RSAUtils.encryptByPublicKey(inputStr,publickey);

            //3.解密
            //从文件中获取公钥
            String privatekey=RSAUtils.readKey("d:/privateKey.keystore");
            String outputStr=RSAUtils.decryptByPrivateKey(encodedata,privatekey);
            System.out.println("outputstr="+outputStr);

            String server_inputStr = "{\"aac003\":\"420624198411037915\"}";
            //服务端加密base64
            String serverencodedata=RSAUtils.encryptByPrivateKey(server_inputStr,privatekey);
            String server_outputStr=RSAUtils.decryptByPublicKey(serverencodedata,publickey);
            System.out.println("server_outputStr="+server_outputStr);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * main
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        //请求数据
        String inputStr="{\"code_type\":\"JJ_AREA\",\"code_value\":\"c099\"}";
        // aac002=420624198411037915
        //加密数据
        RSAUtils.EncryptDataStruct encryptDataStruct = RSAUtils.encryptByAesAndRsaPublickey(inputStr);
        String data= JSONObject.fromObject(encryptDataStruct).toString();
        System.out.println("request data:"+data);

        //模拟服务端解密
        RSAUtils.decryptByAesAndRsaPrivateKey(encryptDataStruct);

        //服务端加密
        String outputStr="";

        String responseparam="T1/gUoPDW/mAawkszRsYk7oSavzjCUrZDNK2EWLsbWWnmgWjyq/JrptFUt/D+hQ0/DS0OFmbRvMsLEIwdhtZpA==@RAsBIEW4HGnqfhiny0+qnUKGXOCXgHTUMiEISLa1usDYUKxSmVkalahB7/jqPpDbjbkn+ZZR/PjUCGLVk1pgpVUXOiwK7dxq5beirOSqxVfuV8Ph3Kgg+Ra0kHX5Ma05s3HV59SlMy6Q1e3BYq0ZWkRGNbHU9kkVl6LTjHfjXaI=";
        String decodedata= RSAUtils.decryptByAesAndRsaPublickey(responseparam);
        System.out.println("response data:"+decodedata);
    }
}
