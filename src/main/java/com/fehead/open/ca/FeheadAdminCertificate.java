package com.fehead.open.ca;

import java.util.Date;

/**
 * @Description: FeheadAdmin证书
 * @Author lmwis
 * @Date 2019-11-11 20:29
 * @Version 1.0
 */
public class FeheadAdminCertificate {

    /**
     * application name
     * or
     * encrypted certificate
     */
    private String principal;

    /**
     * create time
     */
    private Date createTime;

    /**
     * agency
     */
    private final String CAAgency = "FEHEADCA";

    public FeheadAdminCertificate(String principal, Date createTime) {
        this.principal = principal;
        this.createTime = createTime;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCAAgency() {
        return CAAgency;
    }
}
