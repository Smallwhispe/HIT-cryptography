package com.example.form.entity;

import com.example.form.*;
import com.example.form.entity.cryptography.MyRSA;
import com.example.form.entity.cryptography.VerifyCert;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class RSAED {

    public RuleForm login(LoginMessage loginMessage) throws Exception {
        String key = loginMessage.getKey();
        String username = loginMessage.getUsername();
        String password = loginMessage.getPassword();

        String path = "key/prikey.txt";
        Path readingpath = Paths.get(path);
        String prikey = Files.readString(readingpath);
        byte[] aes_key = decrypt(key, prikey).getBytes();

        username = AesDecrypt(username, aes_key);
        password = AesDecrypt(password, aes_key);

        RuleForm ruleForm = new RuleForm();
        ruleForm.setPassword(password);
        ruleForm.setUsername(username);
        return ruleForm;
    }

    public User user_register(UserForm userForm) throws Exception {
        String key = userForm.getKey();
        String username = userForm.getUsername();
        String password = userForm.getPassword();

        String path = "key/prikey.txt";
        Path readingpath = Paths.get(path);
        String prikey = Files.readString(readingpath);
        byte[] aes_key = decrypt(key, prikey).getBytes();

        username = AesDecrypt(username, aes_key);
        password = AesDecrypt(password, aes_key);

        User user = new User();
        user.setId(userForm.getId());
        user.setPassword(password);
        user.setUsername(username);
        user.setName(userForm.getName());
        user.setGender(userForm.getGender());
        user.setAccount(userForm.getAccount());
        return user;
    }

    public Shop shop_register(ShopForm shopForm) throws Exception {
        String key = shopForm.getKey();
        String username = shopForm.getUsername();
        String password = shopForm.getPassword();

        String path = "key/prikey.txt";
        Path readingpath = Paths.get(path);
        String prikey = Files.readString(readingpath);
        byte[] aes_key = decrypt(key, prikey).getBytes();

        username = AesDecrypt(username, aes_key);
        password = AesDecrypt(password, aes_key);

        Shop shop = new Shop();
        shop.setId(shopForm.getId());
        shop.setPassword(password);
        shop.setUsername(username);
        shop.setName(shopForm.getName());
        shop.setGender(shopForm.getGender());
        shop.setAccount(shopForm.getAccount());
        return shop;
    }

    public ConfirmForm confirm(ConfirmForm confirmForm) throws Exception {

//        String ca_prikey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMS/ukh1x7EfJMJY\n" +
//                "DtTqWVpTQIodlFqeOqQ0TwCw9CPJC/rpJFQ9qJSq6LSTAL+IgTv1xhRpiS7OT8Qk\n" +
//                "GGz8yQy558MoF9GXRlJY2u3RzlsDn+agXqtUrrjj59J2fzYaxWh5C+MMbvwFhgmV\n" +
//                "bw1RPFxG6mSwM3wYHXCGGEcXaqZtAgMBAAECgYBhSs47dsLA+72ZvSHYd+qnl/Oa\n" +
//                "WPY7uXMWMCfBF8Ai0hByi0fDwJnUqo9YfDkqfb4qgGbrQSTtWGhpfc/2VSq1mlX+\n" +
//                "/PRVvX9kZBYyP2rydmjhBHoXmSU8fsZr9wR475hJ89bO8J3eUqmyRiUowMom9/wD\n" +
//                "bd3FruLfeLZjFQTCoQJBAOU0PYyObmW69JR9hTEWT4+GIQV7VexXOQDhZjNzGzIs\n" +
//                "hZfJJKl9YJl/Z3zchrqPInqceyALP4cQTr8Getl4kr8CQQDbwCZ6FKV4SAypTBtL\n" +
//                "/2BQDbTQ9S93J32Ug8dYVx35FMxJdknML/z9X2HUIy+mQ0A6HMiPlDUi1godWLwp\n" +
//                "6g3TAkEAzBmr4VkH97D2kLABhgefC2heSVpd1hqJmT4d2xzaD+DTialE62TJO4bX\n" +
//                "Kbnag5BoHJTxk0RQ6r3b0YE8riEPUwJBAK0b+nOTTiKpx2eab6p3m7yUf4tYirK3\n" +
//                "5kKXaPMbdZ4hFykLyOjUKNzERcGikkfMlIzy3b/VheJScJdbrqbqHUcCQHOTEws/\n" +
//                "Sfuv7d94a8CuOs38xwk+5RA3ubOqWdgWnHNJR/Z6u8e0IQciPqT6WPjL3yjq2wiy\n" +
//                "EuGqBjWDCJuDLw4=";
        String path = "key/prikey.txt";
        Path readingpath = Paths.get(path);
        String ca_prikey = Files.readString(readingpath);

        VerifyCert verifyCert = new VerifyCert();
        if(MyRSA.verifySign(verifyCert.downloadCert("bank"),confirmForm.getOrderhash(),confirmForm.getSignature())){
            ConfirmForm confirmForm1 = new ConfirmForm();
            String key = confirmForm.getKey();
            byte[] aes_key = decrypt(key, ca_prikey).getBytes();
            confirmForm1.setCode(AesDecrypt(confirmForm.getCode(), aes_key));
            confirmForm1.setOrderhash(AesDecrypt(confirmForm.getOrderhash(), aes_key));

            return confirmForm1;
        }
        else return null;
    }

    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = org.apache.commons.codec.binary.Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }


    // 解密
    public static String AesDecrypt(String sSrc, byte[] sKey) throws Exception {
        try {

            SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);//先用base64解密
            System.out.println(encrypted1);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }


//    public String sha256(String text) throws NoSuchAlgorithmException {
//        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//        byte[] bytes = messageDigest.digest(text.getBytes());
//        return Hex.encodeHexString(bytes);
//    }
//
//
//    public boolean very(String SignedStr, String verStr) {
//        boolean verfy = false;
//        InputStream fis = null;
//        try {
//            fis = new FileInputStream("../../rescources/cert/CaFront_1671242965406.cer");
//            CertificateFactory cf = CertificateFactory.getInstance("x509");
//            Certificate cerCert = cf.generateCertificate(fis);
//            PublicKey pubKey = cerCert.getPublicKey();
//            byte[] signed = Base64.getDecoder().decode(SignedStr);
//            Signature sign = Signature.getInstance("SHA256withRSA");
//            sign.initVerify(pubKey);
//            sign.update(verStr.getBytes("UTF-8"));
//
//            System.out.println(signed);
//            System.out.println(verStr.getBytes("UTF-8"));
//
//            verfy = sign.verify(signed);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return verfy;
//    }
}




