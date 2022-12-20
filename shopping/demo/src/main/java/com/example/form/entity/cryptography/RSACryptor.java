package com.example.form.entity.cryptography;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSACryptor
{
    //前端的公钥
    String pubKey =
            //"-----BEGIN PUBLIC KEY-----" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkBmc9KuWk9kkl/6mgL4Om1ONC" +
                    "jdKHR1UnNz0cbQzg1yHHk5EQh+483wg/aM46ogOXRk0vCTp5pE7+57AG0RaqXg8v" +
                    "qzmPZ3cvowAUY4SBF/l8NnTlh0yuAxyoiwD0jylS6rIBQ1NfueCZbZiNGZ1q8ach" +
                    "vuFCIvevvP3rsTDD+QIDAQAB" ;
    //        + "-----END PUBLIC KEY-----";

    //后端的私钥
    String privKey =
            //"-----BEGIN PRIVATE KEY-----" +
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOgc/dxVDSrGGfr2" +
                    "sFLM+tH0KXrt8Ye06P4pgx1Dn0EwcQ1Qeg5cG62sLd2Ux0cFklL+l9R2ixcJv7wJ" +
                    "8VxXH6NbZcLaGXV6RR0Rp/uFaqtMzpmbseMyl3oj7ZQlKhptSPyeje5UBnAdoe4K" +
                    "ioOVhUDKd0oepd4+T2pDZHx2lQ+xAgMBAAECgYEAlyragna53pOqM/IZn7Nz3uz/" +
                    "Xto8pT9DRtROWe7CdzhPSHaTJ2sBc7hhHqBYsH6Gb5rz0yFpMJXe014+XP840HOY" +
                    "K8GCfa+84Z24olH2WN3QN0eilgHsnPAPK5CPPIgU1glTc4LrAgENtxJi1/UhTe8T" +
                    "5dyfzQNxxuHBypnCH0kCQQD3K+A851F7Qtdk4/6I88xv3f52JJHOsFkaVoP8/cOp" +
                    "qK2vvBYurb3B7LuIwUSbUEdnd9k2sboE/xSwMCU810FTAkEA8Gdsu/zAq6Krn2ON" +
                    "/ItXnFYqv3tEPWq9ltOhsLQNNe/Z3bFkFdtfSaft5yyXnRnPPlocXouKkEowxmj2" +
                    "su32awJAHl06f+De5qiQc2l61HI21CtOXk8hxmVtnMmyDztRdR8urx8JDpTzccZW" +
                    "R3eOqRHUo8ZaXdi5hHGdpgNUH8RF1wJAB+xguRFriDykS2yMkXZGPPDaF9WIOj36" +
                    "Ya439gr5JH3zQEtIcS/5xAO045mkpZjkK7nslXEj7/2B/ggXmW4eYQJAUVN1py+P" +
                    "f4l+BZP9JFsr0aYHpSet8I3Ox6CpnHAXSbQj/1fELDgBPvs03Bp0tEnG7GHCFIfL" +
                    "W2W1qP7lStU/CQ==";
//            +
//            "-----END PRIVATE KEY-----";

    String CAPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQNi15KedoMtc2lTgoySrqbN6NLM1TM"+
            "absqcQbvX5xZtfB0+GTkl9uG2rJhXTliOZJ6TnurLXSGh7asz8V8JDZSyh/dpv0fC6zm6mJuBZpoyMl"+
            "Kca9X3BOCwkgjhWWRkPZ1z3k6Zr4Qv1lMJvYedUAHh3viV+/IPztfBqlRzE3dwIDAQAB";

    VerifyCert verifyCert = new VerifyCert();

    public String Encrypt(String str) throws Exception
    {
        byte[] decodedPubKey = Base64.decodeBase64(this.pubKey);
        RSAPublicKey RSAPubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decodedPubKey));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,RSAPubKey);
        String data = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return data;
    }

    public String Decrypt(String str) throws Exception
    {
        byte[] inputBytes = Base64.decodeBase64(str.getBytes("UTF-8"));
        byte[] decodedPrivKey = Base64.decodeBase64(privKey);
        RSAPrivateKey RSAPrivKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decodedPrivKey));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,RSAPrivKey);
        String data = new String(cipher.doFinal(inputBytes));
        return data;
    }

    public String ECEncrypt(String str) throws Exception
    {
        String PubKeyDHY = verifyCert.downloadCert("刁浩宇");
        if(PubKeyDHY == null)
        {
            System.out.println("未获取到电商证书有效公钥。");
            return null;
        }
        System.out.println(PubKeyDHY);
        byte[] decodedPubKey = Base64.decodeBase64(PubKeyDHY);
        RSAPublicKey RSAPubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decodedPubKey));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,RSAPubKey);
        String data = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return data;
    }

    public String CAEncrypt(String str) throws Exception
    {
        byte[] decodedPubKey = Base64.decodeBase64(this.CAPubKey);
        RSAPublicKey RSAPubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decodedPubKey));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,RSAPubKey);
        String data = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return data;
    }

    public String getPrivKey()
    {
        return this.privKey;
    }
}
