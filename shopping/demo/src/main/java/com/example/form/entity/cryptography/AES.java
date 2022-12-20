package com.example.form.entity.cryptography;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;


public class AES
{
    public String AESGenerateKey(int size)
    {
        //如密钥size不是16的倍数，自行设定为32位
        if(size % 16 != 0)
        {
            size = 32;
        }
        byte[] bytes = new byte[size];
        for(int i = 0; i < bytes.length; i++){
            bytes[i] = (byte)(new Random().nextInt(96)+32);
        }
        return new String(bytes);
    }



    public String AESEncrypt(String str, String key) throws Exception
    {
        KeyGenerator kgen = null;
        try {
            kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
            byte[] cipherBytes = cipher.doFinal(str.getBytes("utf-8"));
            return base64Encode(cipherBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public String AESDecrypt(String sSrc, byte[] sKey) throws Exception
    {
        try
        {
            SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);//先用base64解密
            //System.out.println(encrypted1);
            try
            {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                //System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            //System.out.println(ex.toString());
            return null;
        }
    }

    private static String base64Encode(byte[] bytes)
    {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(bytes);
    }
}
