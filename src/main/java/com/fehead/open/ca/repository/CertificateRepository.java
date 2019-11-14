package com.fehead.open.ca.repository;

import com.fehead.open.ca.entity.AdminCertificate;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @Description:
 * @Author lmwis
 * @Date 2019-11-12 17:19
 * @Version 1.0
 */
@Repository
public interface CertificateRepository extends ReactiveMongoRepository<AdminCertificate,String> {


    @Query("{'fehead_admin_application':'fehead-ca'}")
    Mono<AdminCertificate> findByFeheadAdminApplication(String application);

}
