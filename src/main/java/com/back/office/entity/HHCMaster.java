package com.back.office.entity;

import java.util.Date;

public class HHCMaster {

    private Integer hhcMasterId;
    private String hhcId;
    private String type;
    private String status;
    private String flightNo;
    private Integer recordStatus;
    private Date lastUsedDate;


    public Integer getHhcMasterId() {
        return hhcMasterId;
    }

    public void setHhcMasterId(Integer hhcMasterId) {
        this.hhcMasterId = hhcMasterId;
    }

    public String getHhcId() {
        return hhcId;
    }

    public void setHhcId(String hhcId) {
        this.hhcId = hhcId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Date getLastUsedDate() {
        return lastUsedDate;
    }

    public void setLastUsedDate(Date lastUsedDate) {
        this.lastUsedDate = lastUsedDate;
    }
}
