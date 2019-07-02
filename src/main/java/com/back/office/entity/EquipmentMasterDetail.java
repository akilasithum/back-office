package com.back.office.entity;

import java.util.Date;

public class EquipmentMasterDetail {

    private Integer equipmentMasterId;
    private String equipmentId;
    private String type;
    private String status;
    private String lastUsed;
    private String flightNumber;
    private Date lastUsedDate;


    public Integer getEquipmentMasterId() {
        return equipmentMasterId;
    }

    public void setEquipmentMasterId(Integer equipmentMasterId) {
        this.equipmentMasterId = equipmentMasterId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
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

    public String getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(String lastUsed) {
        this.lastUsed = lastUsed;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Date getLastUsedDate() {
        return lastUsedDate;
    }

    public void setLastUsedDate(Date lastUsedDate) {
        this.lastUsedDate = lastUsedDate;
    }
}

