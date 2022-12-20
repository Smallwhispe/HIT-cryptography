package com.example.form.entity.cryptography;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class VerifyCert {


    // 修改这个
    private final String fpath = "certificate/";

    /*
        考软构前也在摸鱼，忘了怎么写注释了。怕大家IDE没有都装中文，有道翻译。
        方法功能：根据用户名下载CA的证书
        param:证书系统的用户名，比如要下载刁哥的证书就param="刁浩宇"，下载海博的（银行）就是param="bank"
        注意：这个方法需要修改下载证书的本地路径（把证书下载到哪），在TODO处修改。默认路径是在后端根目录下的certificate文件夹。
     */

    // 返回文件名称
    // 返回空则说明用户名不对
    public String getCertificate(String user_name) {

        String result;
        //Url是CA下载的url，直接写在程序里
        String Url = "http://172.24.67.216:8181/caCert/down_username";
        HttpPost httpPost = new HttpPost(Url);
        CloseableHttpClient client = HttpClients.createDefault();

        StringEntity entity = new StringEntity(user_name,"UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("text/plain");
        httpPost.setEntity(entity);
        try
        {
            HttpResponse response = client.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == 200)
            {
                result = EntityUtils.toString(response.getEntity(),"UTF-8");
                if(result.length()==0){
                    return "";
                }
                FileWriter fileWriter = new FileWriter(fpath + user_name + ".cer");
                fileWriter.write(result);
                fileWriter.flush();
                fileWriter.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return user_name+".cer";

    }

    // 验证证书的有效性
    public boolean verify_cert(String filepath) throws InvalidKeyException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, FileNotFoundException, CertificateException, FileNotFoundException, ParseException {

        //  证书实例，将文件输入流 转换为 证书
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        // 读取 ca 证书 文件，获取证书的 公钥
        FileInputStream fis_ca = new FileInputStream(fpath+"CA.cer");
        Certificate ca_cert = cf.generateCertificate(fis_ca);
        PublicKey ca_publicKey = ca_cert.getPublicKey();

        // 读取末端证书，获取证书
        FileInputStream verify_fis = new FileInputStream(filepath);
        Certificate verify_cert = cf.generateCertificate(verify_fis);

        FileInputStream oc = new FileInputStream(filepath);
        X509Certificate oCer = (X509Certificate) cf.generateCertificate(oc);

        // 验证证书是由 某公钥签发的：使用公钥对 该证书信息解密得到签名值 一致，或者 使用公钥对 签名值 解密，得到 证书信息一致
        try {
            verify_cert.verify(ca_publicKey);
        } catch (SignatureException e) {
            return false;
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        System.out.println("该证书属于CA签发");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Date beforedate = oCer.getNotBefore();
        beforedate = simpleDateFormat.parse(simpleDateFormat.format(beforedate));

        Date afterdate = oCer.getNotAfter();
        afterdate = simpleDateFormat.parse(simpleDateFormat.format(afterdate));
        System.out.println(beforedate);
        System.out.println(afterdate);

        Date now = new Date();
        if( now.before(beforedate) || now.after(afterdate)){
            return false;
        }
        String serial_number = oCer.getSerialNumber().toString();

        String isValid = isValid(serial_number);

        return isValid.equals("true");
    }

    /*
    方法功能：根据证书序列号判断证书是否有效
    serial_number:序列号，字符串类型
    返回值：true/false，字符串类型而不是bool类型。
     */
    public String isValid(String serial_number)
    {
        AES aes = new AES();
        RSACryptor rsaCryptor = new RSACryptor();
        String AESKey = aes.AESGenerateKey(16);
        String AESKeyBackup = AESKey;
        System.out.println(AESKeyBackup);
        try
        {
            serial_number = aes.AESEncrypt(serial_number,AESKey);
            AESKey = rsaCryptor.CAEncrypt(AESKey);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        JSONObject param = new JSONObject();

        param.put("serial_number",serial_number);
        param.put("key",AESKey);

        String result;

        String tempUrl = "http://172.24.67.216:8181/caCert/Getvalid";

        HttpPost httpPost = new HttpPost(tempUrl);
        CloseableHttpClient client = HttpClients.createDefault();

        StringEntity entity = new StringEntity(param.toString(),"UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        try
        {
            HttpResponse response = client.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == 200)
            {
                result = EntityUtils.toString(response.getEntity(),"UTF-8");
                result = aes.AESDecrypt(result, AESKeyBackup.getBytes());
                return result;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }



    // 根据cert用户名下载证书，返回公钥串，如果为""说明下载失败
    public String downloadCert(String user_name) throws FileNotFoundException, CertificateException, NoSuchAlgorithmException, ParseException, InvalidKeyException, NoSuchProviderException {
        String filename = getCertificate(user_name);
        if(filename.equals("")){
            return "";
        }else{
            if(verify_cert(fpath+filename)){
                FileInputStream fis = new FileInputStream(fpath+filename);
                CertificateFactory cf = CertificateFactory.getInstance("x509");
                java.security.cert.Certificate cerCert = cf.generateCertificate(fis);
                PublicKey pubKey = cerCert.getPublicKey();
                return org.apache.commons.codec.binary.Base64.encodeBase64String(pubKey.getEncoded());
            }else{
                return "";
            }
        }
    }

//    //测试用代码
//    public static void main(String[] args) throws Exception
//    {
//        boolean very = new VerifyCert().verify_cert("certificate/刁浩宇.cer");
//        System.out.println(very);
//    }
}