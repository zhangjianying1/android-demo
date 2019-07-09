package com.example.caiyue.androidstuiodemo.model;

import java.util.List;

public class IndexBannerModel {
    private  String serialNumber,name,iconPath,linkUrl,operator,operateTime;
    private int showSort, status;

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public void setShowSort(int showSort) {
        this.showSort = showSort;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getName() {
        return name;
    }

    public String getIconPath() {
        return iconPath;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public String getOperator() {
        return operator;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public int getShowSort() {
        return showSort;
    }

    public int getStatus() {
        return status;
    }

}
