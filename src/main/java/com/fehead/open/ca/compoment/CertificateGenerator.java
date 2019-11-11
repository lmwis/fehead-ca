package com.fehead.open.ca.compoment;

import com.fehead.open.ca.FeheadAdminCertificate;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

/**
 * @Description: 证书生成器
 * @Author lmwis
 * @Date 2019-11-11 20:45
 * @Version 1.0
 */
@Component
public class CertificateGenerator {

    private static final String ALGORITH="RSA";

    static {
        KeyPairGenerator instance = null;
        try {
            instance = KeyPairGenerator.getInstance(ALGORITH);
            // 初始化密钥对生成器，密钥大小为96-1024位
            instance.initialize(512,new SecureRandom());
            keyPair = instance.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private static final String PUBLIC_KEY=initPublicKey();

    private static final String PRIVATE_KEY=initPrivateKey();


    // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
    static KeyPair keyPair = null;




//    PasswordEncoder passwordEncoder = new ;

    public FeheadAdminCertificate generatorCertificate(String text) {

        // 生成一个密钥对，保存在keyPair中

        System.out.println("要加密的文本："+text);
        System.out.println("加密后："+encrypt(text,PUBLIC_KEY));
        try {
            System.out.println("解密后："+decrypt(text,PRIVATE_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new FeheadAdminCertificate(encrypt(text,PUBLIC_KEY),new Date());
    }

    /**
     * 公钥加密
     * @param str
     * @param publicKey
     * @return
     * @throws Exception
     */
    private String encrypt( String str, String publicKey ){
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = null;
        Cipher cipher = null;
        try {
            pubKey = (RSAPublicKey) KeyFactory.getInstance(ALGORITH).generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            cipher = Cipher.getInstance(ALGORITH);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }


        try {
            return Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥解密
     * @param str
     * @param privateKey
     * @return
     * @throws Exception
     */
    private String decrypt(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(ALGORITH).generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance(ALGORITH);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    private static String initPublicKey(){
        return new String( Base64.encodeBase64(keyPair.getPublic().getEncoded()));
    }

    private static String initPrivateKey(){
        return new String( Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
    }

    public static String getPublicKey() {
        return PUBLIC_KEY;
    }

    public static String getPrivateKey() {
        return PRIVATE_KEY;
    }
}
