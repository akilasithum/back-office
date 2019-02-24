package com.back.office.entity;



import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class CurrencyDetails {

    private int currencyCodeId;
    @ExcelCellName("Currency Code")
    private String currencyCode;
    @ExcelCellName("Currency Description")
    private String currencyDesc;
    @ExcelCellName("Currency Rate")
    private float currencyRate;
    @ExcelCellName("Currency Type")
    private String currencyType;
    @ExcelCellName("Priority Order")
    private String priorityOrder;
    @ExcelCellName("Effective Date")
    private String effectiveDate;
    private int recordStatus;

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    public int getCurrencyCodeId() {
        return currencyCodeId;
    }

    public void setCurrencyCodeId(int currencyCodeId) {
        this.currencyCodeId = currencyCodeId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyDesc() {
        return currencyDesc;
    }

    public void setCurrencyDesc(String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }

    public float getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(float currencyRate) {
        this.currencyRate = currencyRate;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getPriorityOrder() {
        return priorityOrder;
    }

    public void setPriorityOrder(String priorityOrder) {
        this.priorityOrder = priorityOrder;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
