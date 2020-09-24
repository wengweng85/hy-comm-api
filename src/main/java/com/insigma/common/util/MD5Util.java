package com.insigma.common.util;


import java.security.MessageDigest;

public class MD5Util
{
  private static final String[] HEX_DIGITS = {
    "0", "1", "2", "3", "4", "5", "6", "7", 
    "8", "9", "a", "b", "c", "d", "e", "f" };

  public static String byteArrayToHexString(byte[] b)
  {
    StringBuffer resultSb = new StringBuffer();
    for (int i = 0; i < b.length; ++i) {
      resultSb.append(byteToHexString(b[i]));
    }

    return resultSb.toString();
  }

  private static String byteToHexString(byte b) {
    int n = b;
    if (n < 0) {
      n += 256;
    }
    int d1 = n / 16;
    int d2 = n % 16;
    return HEX_DIGITS[d1] + HEX_DIGITS[d2];
  }

  public static String MD5Encode(String origin) {
    String resultString = null;
    try
    {
      resultString = new String(origin);
      MessageDigest md = MessageDigest.getInstance("MD5");
      resultString = byteArrayToHexString(md.digest(resultString.getBytes("UTF-8")));
    }
    catch (Exception md)
    {
    }
    return resultString;
  }

  public static String MD5Encode_16(String origin) {
    return MD5Encode(origin).substring(8, 24).toUpperCase();
  }

  public static void main(String[] args) {
    String param="{\"app_user_syn_id\":\"123456\",\"mobile\":\"13767304752\",\"fullName\":\"ÎÌÉÜ»Ô\",\"idNumber\":\"362326198702244545\"}";
    System.out.println(MD5Encode("123456"));
  }
}