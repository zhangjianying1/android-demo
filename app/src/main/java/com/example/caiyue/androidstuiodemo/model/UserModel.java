package com.example.caiyue.androidstuiodemo.model;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String fundAcctId,lastip,securityAcctId,hasPwd, mobile,
    headPath,bodyCode,userName,userId,token;

    public void setFundAcctId(String fundAcctId) {
        this.fundAcctId = fundAcctId;
    }

    public void setLastip(String lastip) {
        this.lastip = lastip;
    }

    public void setSecurityAcctId(String securityAcctId) {
        this.securityAcctId = securityAcctId;
    }

    public void setHasPwd(String hasPwd) {
        this.hasPwd = hasPwd;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public void setBodyCode(String bodyCode) {
        this.bodyCode = bodyCode;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFundAcctId() {
        return fundAcctId;
    }

    public String getLastip() {
        return lastip;
    }

    public String getSecurityAcctId() {
        return securityAcctId;
    }

    public String getHasPwd() {
        return hasPwd;
    }

    public String getMobile() {
        return mobile;
    }

    public String getHeadPath() {
        return headPath;
    }

    public String getBodyCode() {
        return bodyCode;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}
