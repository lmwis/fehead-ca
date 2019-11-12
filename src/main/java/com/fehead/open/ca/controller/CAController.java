package com.fehead.open.ca.controller;

import com.fehead.lang.controller.BaseController;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.response.CommonReturnType;
import com.fehead.lang.response.FeheadResponse;
import com.fehead.open.ca.compoment.CertificateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: ca相关请求的控制器
 * @Author lmwis
 * @Date 2019-11-11 20:38
 * @Version 1.0
 */
@RestController
@RequestMapping("/ca")
public class CAController extends BaseController {

    @Autowired
    CertificateGenerator certificateGenerator;

    @PostMapping("/generate}")
    public FeheadResponse getFeheadAdminCertificate(
            @RequestParam("fehead_admin_application") String application
            ){
        return CommonReturnType.create(certificateGenerator.generatorCertificate(application));
    }


    @PostMapping("/validate")
    public FeheadResponse validateFeheadAdminCertificate(@RequestParam("text") String text) throws BusinessException {
        return CommonReturnType.create(certificateGenerator.validateCertificate(text));
    }
}
