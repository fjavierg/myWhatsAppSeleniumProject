package org.openqa.selenium.example;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class Authentication {
    public static String encode(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);

//        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
        return Base64.encodeBase64String(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
      }

      public static void main(String [] args) throws Exception {
        System.out.println(encode("key", "The quick brown fox jumps over the lazy dog"));
      }
}
