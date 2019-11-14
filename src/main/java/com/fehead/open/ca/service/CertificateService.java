package com.fehead.open.ca.service;

import com.fehead.lang.error.BusinessException;
import com.fehead.open.ca.entity.AdminCertificate;
import reactor.core.publisher.Mono;

/**
 * @Description:
 * @Author lmwis
 * @Date 2019-11-13 13:22
 * @Version 1.0
 */
public interface CertificateService {

    /**
     * 将证书写入数据库
     *  若数据库中存在该证书，则返回数据库中的证书
     * @param adminCertificate 证书信息
     * @return
     * @throws BusinessException 业务异常
     */
    public Mono<AdminCertificate> saveCertificate(AdminCertificate adminCertificate) throws BusinessException;

    public Mono<AdminCertificate> getCertificateByApplication(String application);
}
