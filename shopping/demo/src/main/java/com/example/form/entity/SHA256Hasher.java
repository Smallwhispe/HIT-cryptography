package com.example.form.entity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Hasher
{
    public String SHA256(String str)
    {
        MessageDigest messageDigest;
        String encodeStr = "";
        try
        {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byteToHex(messageDigest.digest());
        }catch (NoSuchAlgorithmException | UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return encodeStr;
    }

    public String byteToHex(byte[] bytes)
    {
        StringBuffer stringBuffer = new StringBuffer();
        String temp;
        for(int i = 0;i < bytes.length;i++)
        {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if(temp.length() == 1)//长度为1，补0
            {
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}
