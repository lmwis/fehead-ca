package com.fehead.open.ca.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 证书实体类
 * @Author lmwis
 * @Date 2019-11-12 17:07
 * @Version 1.0
 */
@Document(collection = "fehead_admin_certificate")
@Data
@RequiredArgsConstructor
//@NoArgsConstructor
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
    @Field("fehead_admin_certificate_encode_text")
    private String feheadAdminCertificateEncodeText;

    /**
     * 绑定服务
     */
    @NotBlank
    @Field("fehead_admin_application")
    private String feheadAdminApplication;




}
