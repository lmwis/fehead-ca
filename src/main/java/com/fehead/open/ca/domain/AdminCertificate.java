package com.fehead.open.ca.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 证书实体类
 * @Author lmwis
 * @Date 2019-11-12 17:07
 * @Version 1.0
 */
@Document("fehead_admin_certificate")
@Data
public class AdminCertificate {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 证书加密后的文本
     */
    @NotBlank
    private String fehead_admin_certificate_encode_text;

    /**
     * 绑定服务
     */
    @NotBlank
    private String fehead_admin_application;


}
