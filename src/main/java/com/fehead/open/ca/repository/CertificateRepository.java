package com.fehead.open.ca.repository;

import com.fehead.open.ca.domain.AdminCertificate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * @Description:
 * @Author lmwis
 * @Date 2019-11-12 17:19
 * @Version 1.0
 */
public interface CertificateRepository extends ReactiveMongoRepository<AdminCertificate,String> {


    public Mono<AdminCertificate> findByFehead_admin_application(String application);

}
