package com.fehead.open.ca.compoment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fehead.lang.error.BusinessException;
import com.fehead.open.ca.FeheadAdminCertificate;
import com.fehead.open.ca.domain.AdminCertificate;
import com.fehead.open.ca.repository.CertificateRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import static com.fehead.lang.error.EmBusinessError.FEHEAD_CA_CERTIFICATE_ILLEGL;

/**
 * @Description: 证书生成器
 * @Author lmwis
 * @Date 2019-11-11 20:45
 * @Version 1.0
 */
@Component
public class CertificateGenerator {

    private static final Logger logger = LoggerFactory.getLogger(CertificateGenerator.class);

    /**
     * object to json转化工具
     */
    @Autowired
    ObjectMapper objectMapper;

    /**
     * 证书仓库
     */
    @Autowired
    CertificateRepository certificateRepository;

    private static final String ALGORITH = "RSA";
    // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
    static KeyPair keyPair = null;

    static {
        KeyPairGenerator instance;
        try {
            instance = KeyPairGenerator.getInstance(ALGORITH);
            // 初始化密钥对生成器，密钥大小为96-1024位
            instance.initialize(1024, new SecureRandom());
            keyPair = instance.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private static final String PUBLIC_KEY = initPublicKey();

    private static final String PRIVATE_KEY = initPrivateKey();

    /**
     * 证书生成器
     * @param application 应用名
     * @return FeheadAdminCertificate电子证书
     */
    public FeheadAdminCertificate generatorCertificate(String application) {

        AdminCertificate a = new AdminCertificate(){{
            setFehead_admin_application("");
            setFehead_admin_certificate_encode_text("");
            setId("");
        }};

        // 加密后的信息
        String encrypt = "";
        // 证书json格式
        String feheadAdminCertificateJsonView = "";

//        System.out.println("要加密的文本：" + text);

        FeheadAdminCertificate feheadAdminCertificate = new FeheadAdminCertificate(application,new Date());
        try {
            // 转化为json格式
            feheadAdminCertificateJsonView = objectMapper.writeValueAsString(feheadAdminCertificate);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            // 信息加密
            encrypt = encrypt(feheadAdminCertificateJsonView, PUBLIC_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
//            System.out.println("加密后：" + encrypt);
//            System.out.println("解密后：" + decrypt(encrypt, PRIVATE_KEY));

        return new FeheadAdminCertificate(encrypt, new Date());
    }

    public AdminCertificate saveCertificate(AdminCertificate adminCertificate){

//        Mono<AdminCertificate> savedAdminCertificate
//                = this.certificateRepository
//                .findByFehead_admin_application(adminCertificate.getFehead_admin_application())
//                .map(k->1)
//                .defaultIfEmpty(this.certificateRepository
//                        .save(adminCertificate).map(k-> {
//                            new AdminCertificate(){{
//                                setFehead_admin_application(k.getFehead_admin_application());
//                                setFehead_admin_certificate_encode_text(k.getFehead_admin_certificate_encode_text());
//                                setId(k.getId());
//                            }}
//                        }));


//        return savedAdminCertificate.map(k-> {
//            new AdminCertificate(){{
//                setFehead_admin_application(k.getFehead_admin_application());
//                setFehead_admin_certificate_encode_text(k.getFehead_admin_certificate_encode_text());
//                setId(k.getId());
//            }};
//        }).then();
        return null;
    }
    public FeheadAdminCertificate validateCertificate(String encryptText) throws BusinessException {
        String decrypt = "";
        FeheadAdminCertificate certificate = null;
        try {
            // 执行解密
            decrypt = decrypt(encryptText, PRIVATE_KEY);
            try {
                certificate = objectMapper.readValue(decrypt, FeheadAdminCertificate.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            if(e instanceof BadPaddingException){
                logger.warn("需要校验的证书文本长度不合法："+encryptText);
                throw new BusinessException(FEHEAD_CA_CERTIFICATE_ILLEGL);
            }
            e.printStackTrace();
        }


        return certificate;

    }

    /**
     * 公钥加密
     *
     * @param str
     * @param publicKey
     * @return
     * @throws Exception
     */
    private String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = null;
        Cipher cipher = null;

        pubKey = (RSAPublicKey) KeyFactory.getInstance(ALGORITH).generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        cipher = Cipher.getInstance(ALGORITH);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));

    }

    /**
     * 私钥解密
     *
     * @param str
     * @param privateKey
     * @return
     * @throws Exception
     */
    private String decrypt(String str, String privateKey) throws Exception {
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

    private static String initPublicKey() {
        return new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
    }

    private static String initPrivateKey() {
        return new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
    }

    public static String getPublicKey() {
        return PUBLIC_KEY;
    }

    public static String getPrivateKey() {
        return PRIVATE_KEY;
    }
}
