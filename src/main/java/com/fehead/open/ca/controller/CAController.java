package com.fehead.open.ca.controller;

import com.fehead.lang.controller.BaseController;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.response.CommonReturnType;
import com.fehead.lang.response.FeheadResponse;
import com.fehead.open.ca.compoment.CertificateGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @Description: ca相关请求的控制器
 * @Author lmwis
 * @Date 2019-11-11 20:38
 * @Version 1.0
 */
@RestController
@RequestMapping("/ca")
@RequiredArgsConstructor
public class CAController {

    final CertificateGenerator certificateGenerator;

    @PostMapping("/generate")
    public Mono<FeheadResponse> getFeheadAdminCertificate(
            @RequestParam("fehead_admin_application") String application
            ) throws BusinessException {
        return certificateGenerator.generatorCertificate(application)
                .flatMap(k->Mono.just(CommonReturnType.create(k)));
    }


    @PostMapping("/validate")
    public FeheadResponse validateFeheadAdminCertificate(
            @RequestParam("encrypt_certificate") String encryptCertificate,
            @RequestParam("fehead_admin_application") String application
    ) throws BusinessException {
        return CommonReturnType.create(certificateGenerator.validateCertificate(encryptCertificate,application));
    }
}
