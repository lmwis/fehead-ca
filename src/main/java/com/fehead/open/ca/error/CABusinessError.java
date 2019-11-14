package com.fehead.open.ca.error;

import com.fehead.lang.error.CommonError;
import com.fehead.lang.error.EmBusinessError;

/**
 * @Description:
 * @Author lmwis
 * @Date 2019-11-13 15:10
 * @Version 1.0
 */
public enum CABusinessError implements CommonError {
    CA_AGENCY_NOT_CORRECT(20001,"CA证书颁发机构不合法"),
    CA_CERTIFICATE_NOT_EXIST(20002,"应用未被授权"),

    ;

    private CABusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;

    @Override
    public int getErrorCode() {
        return 0;
    }

    @Override
    public String getErrorMsg() {
        return null;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        return null;
    }
}
