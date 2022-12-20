package com.example.form.entity.cryptography;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;



/**
 *
 * @author 李佳勇
 *
 */
public class MyRSA {

    private static final String KEY_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 1024;//设置长度
    private static final String PUBLIC_KEY = "publicKey";
    private static final String PRIVATE_KEY = "privateKey";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String ENCODE_ALGORITHM = "SHA-256";

    /*
从证书中获得公钥字符串
 */
    public String getPubKeystr(String cert_path) throws FileNotFoundException, CertificateException {
        FileInputStream fis = new FileInputStream(cert_path);
        CertificateFactory cf = CertificateFactory.getInstance("x509");
        java.security.cert.Certificate cerCert = cf.generateCertificate(fis);
        PublicKey pubKey = cerCert.getPublicKey();
        return org.apache.commons.codec.binary.Base64.encodeBase64String(pubKey.getEncoded());
    }

    /*
    根据私钥文件txt获取私钥字符串
     */
    public String getPriKeystr(String private_txt_path){

        Path ca_pri_path = Paths.get(private_txt_path);

        String ca_private = "";
        try {
            ca_private  = Files.readString(ca_pri_path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ca_private;
    }


    /**
     * 解码PublicKey
     * @param key
     * @return
     */
    public static PublicKey getPublicKey(String key) {
        try {
            byte[] byteKey = Base64.getDecoder().decode(key);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解码PrivateKey
     * @param key
     * @return
     */
    public static PrivateKey getPrivateKey(String key) {
        try {
            byte[] byteKey = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 签名
     * @param key	私钥
     * @param requestData	请求参数
     * @return
     */
    public static String sign(String key, String requestData){
        String signature = null;
        byte[] signed = null;
        try {
            PrivateKey privateKey = getPrivateKey(key);

            Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            Sign.initSign(privateKey);
            Sign.update(requestData.getBytes());
            signed = Sign.sign();

            signature = Base64.getEncoder().encodeToString(signed);
            System.out.println("===签名结果："+signature);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return signature;
    }

    /**
     * 验签
     * @param key	公钥
     * @param requestData	请求参数
     * @param signature	签名
     * @return
     */
    public static boolean verifySign(String key, String requestData, String signature){
        boolean verifySignSuccess = false;
        try {
            PublicKey publicKey = getPublicKey(key);

            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(publicKey);
            verifySign.update(requestData.getBytes());

            verifySignSuccess = verifySign.verify(Base64.getDecoder().decode(signature));
            System.out.println("===验签结果："+verifySignSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return verifySignSuccess;
    }

}
