package com.example.caiyue.androidstuiodemo.model;

public class ChannelModel {
    private String documentClassName, documentClassSerial, isValid, showNum;
    private Boolean isSelected;

    public String getDocumentClassName() {
        return documentClassName;
    }
    public Boolean getIsSelected() {
        return isSelected;
    }
    public String getDocumentClassSerial() {
        return documentClassSerial;
    }

    public String getIsValid() {
        return isValid;
    }

    public String getShowNum() {
        return showNum;
    }

    public void setDocumentClassName(String documentClassName) {
        this.documentClassName = documentClassName;
    }

    public void setDocumentClassSerial(String documentClassSerial) {
        this.documentClassSerial = documentClassSerial;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public void setShowNum(String showNum) {
        this.showNum = showNum;
    }
    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }
}
