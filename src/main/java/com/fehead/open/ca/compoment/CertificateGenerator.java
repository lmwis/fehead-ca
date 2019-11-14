package com.fehead.open.ca.compoment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fehead.lang.error.BusinessException;
import com.fehead.open.ca.FeheadAdminCertificate;
import com.fehead.open.ca.entity.AdminCertificate;
import com.fehead.open.ca.repository.CertificateRepository;
import com.fehead.open.ca.service.CertificateService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import static com.fehead.lang.error.EmBusinessError.FEHEAD_CA_CERTIFICATE_GENERATOR_FAIL;

/**
 * @Description: 证书生成器
 * @Author lmwis
 * @Date 2019-11-11 20:45
 * @Version 1.0
 */
@Component
public class CertificateGenerator {

    private static final String CAAGENCY = "FEHEADCA";

    private static final Logger logger = LoggerFactory.getLogger(CertificateGenerator.class);

    final CertificateRepository certificateRepository;

    /**
     * object to json转化工具
     */

    private final ObjectMapper objectMapper;

    private final CertificateService certificateService;

    /**
     * webflux推荐构造函数方式注入bean
     *
     * @param objectMapper
     * @param certificateService
     */
    public CertificateGenerator(ObjectMapper objectMapper
            , CertificateService certificateService
            ,CertificateRepository certificateRepository) {
        this.objectMapper = objectMapper;
        this.certificateService = certificateService;
        this.certificateRepository = certificateRepository;
    }

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
     *
     * @param application 应用名
     * @return FeheadAdminCertificate电子证书
     */
    public Mono<FeheadAdminCertificate> generatorCertificate(String application) throws BusinessException {

        // 加密后的信息
        String encrypt;
        // 证书json格式
        String feheadAdminCertificateJsonView;

        FeheadAdminCertificate feheadAdminCertificate = new FeheadAdminCertificate(application, new Date());
        try {
            // 转化为json格式
            feheadAdminCertificateJsonView = objectMapper.writeValueAsString(feheadAdminCertificate);
            try {
                // 信息加密
                encrypt = encrypt(feheadAdminCertificateJsonView, PUBLIC_KEY);

                return this.certificateService.saveCertificate(new AdminCertificate() {{ // 写入数据库
                    setFeheadAdminCertificateEncodeText(encrypt);
                    setFeheadAdminApplication(application);
                }})
                        .map(k -> new FeheadAdminCertificate(k.getFeheadAdminCertificateEncodeText(), new Date()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        throw new BusinessException(FEHEAD_CA_CERTIFICATE_GENERATOR_FAIL);
    }

    public Mono<ResponseEntity<Boolean>> validateCertificate(String encryptCertificate, String application) throws BusinessException {
        String decrypt;

//        this.certificateRepository
//                .findByFeheadAdminApplication(application)
//                .flatMap(k->{
//                    return Mono.just(new ResponseEntity());
//                });

//        this.certificateService.getCertificateByApplication(application).;
        return null;

    }

//    public boolean validateCertificate(String encryptCertificate, String application) throws BusinessException {
//        String decrypt;
//        final FeheadAdminCertificate certificate;
//        FeheadAdminCertificate savedCertificate;
//        AdminCertificate block = this.certificateService.getCertificateByApplication(application).block();
//        if(block==null
//                || StringUtils.isEmpty(block.getFeheadAdminApplication())
//                || StringUtils.isEmpty(block.getFeheadAdminCertificateEncodeText())){
//            throw new BusinessException(CA_CERTIFICATE_NOT_EXIST);
//        }else{
//            try {
//                savedCertificate = objectMapper
//                        .readValue(
//                                decrypt(block.getFeheadAdminCertificateEncodeText()
//                                        ,PRIVATE_KEY)
//                                , FeheadAdminCertificate.class);
//            } catch (Exception e) {
//                if (e instanceof BadPaddingException) {
//                    logger.warn("需要校验的证书文本长度不合法：" + encryptCertificate);
//                    throw new BusinessException(FEHEAD_CA_CERTIFICATE_ILLEGL);
//                }
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        try {
//            // 执行解密
//            decrypt = decrypt(encryptCertificate, PRIVATE_KEY);
//            try {
//                certificate = objectMapper.readValue(decrypt, FeheadAdminCertificate.class);
//                if (StringUtils.equals(CAAGENCY, certificate.getCAAgency())) { // 校验证书颁发机构
//
//                    if (StringUtils.isEmpty(savedCertificate.getPrincipal())) { // 为空则未在数据库中找到证书
//                        throw new BusinessException(CA_CERTIFICATE_NOT_EXIST); // 证书不存在
//                    } else { // 最终校验
//                        if (StringUtils.equals(
//                                savedCertificate.getPrincipal(), certificate.getPrincipal()) // 证书应用名比较
//                                &&
//                                savedCertificate.getCreateTime().getTime() == certificate.getCreateTime().getTime() // 证书创建时间比较
//                                &&
//                                StringUtils.equals(savedCertificate.getCAAgency(), certificate.getCAAgency()) // 证书机构比较
//                                &&
//                                StringUtils.equals(savedCertificate.getCAAgency(), CAAGENCY) // 证书机构比较
//
//                        ) {
//                            return true;
//                        }
//                    }
//                } else {
//                    throw new BusinessException(CABusinessError.CA_AGENCY_NOT_CORRECT);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            if (e instanceof BadPaddingException) {
//                logger.warn("需要校验的证书文本长度不合法：" + encryptCertificate);
//                throw new BusinessException(FEHEAD_CA_CERTIFICATE_ILLEGL);
//            }
//            e.printStackTrace();
//        }
//
//
//        return false;
//
//    }

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
