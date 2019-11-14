package com.fehead.open.ca.service.impl;

import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.fehead.open.ca.compoment.CertificateGenerator;
import com.fehead.open.ca.entity.AdminCertificate;
import com.fehead.open.ca.error.CABusinessError;
import com.fehead.open.ca.repository.CertificateRepository;
import com.fehead.open.ca.service.CertificateService;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @Description:
 * @Author lmwis
 * @Date 2019-11-13 13:22
 * @Version 1.0
 */
@Service
public class CertificateServiceImpl implements CertificateService {

    private static final Logger logger = LoggerFactory.getLogger(CertificateServiceImpl.class);

    /**
     * 证书仓库
     */
    final CertificateRepository certificateRepository;

    public CertificateServiceImpl(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    /**
     * 将证书写入数据库
     *  若数据库中存在该证书，则返回数据库中的证书
     * @param adminCertificate 证书信息
     * @return
     * @throws BusinessException
     */
    @Override
    public Mono<AdminCertificate> saveCertificate(AdminCertificate adminCertificate) throws BusinessException {
        return this.certificateRepository
                .findByFeheadAdminApplication(adminCertificate.getFeheadAdminApplication())
                .defaultIfEmpty(
                        new AdminCertificate() {{
                            setFeheadAdminApplication(adminCertificate.getFeheadAdminApplication());
                            setFeheadAdminCertificateEncodeText(adminCertificate.getFeheadAdminCertificateEncodeText());
                            setId(null);
                        }})
                .filter(k -> k.getId() == null)
                .flatMap(k -> this.certificateRepository.save(
                        new AdminCertificate() {{
                            setFeheadAdminApplication(k.getFeheadAdminApplication());
                            setFeheadAdminCertificateEncodeText(k.getFeheadAdminCertificateEncodeText());
                            setId(k.getId());
                        }})
                );

    }

    /**
     * 根据application查找证书
     * @param application
     * @return
     */
    @Override
    public Mono<AdminCertificate> getCertificateByApplication(String application) {

        return this.certificateRepository
                .findByFeheadAdminApplication(application)
                .defaultIfEmpty(new AdminCertificate())
                .then(Mono.error(new BusinessException(CABusinessError.CA_CERTIFICATE_NOT_EXIST)));

//        AdminCertificate block = byFeheadAdminApplication.block();
//        logger.info(new ReflectionToStringBuilder(block).toString());

//        return byFeheadAdminApplication;
    }
}
