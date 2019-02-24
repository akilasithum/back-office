package com.back.office.entity;
import com.poiji.annotation.ExcelCellName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class KitCodes {

    private int kitCodeId;
    @ExcelCellName("Kit Code")
    private String kitCode;
    @ExcelCellName("Description")
    private String description;
    @ExcelCellName("Service Type")
    private String serviceType;
    @ExcelCellName("Activate Date")
    private String activateDate;
    @ExcelCellName("No of Equipments")
    private int noOfEquipments;
    @ExcelCellName("Pack Types")
    private String packTypes;
    private int recordStatus;

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    public int getKitCodeId() {
        return kitCodeId;
    }

    public void setKitCodeId(int kitCodeId) {
        this.kitCodeId = kitCodeId;
    }

    public String getKitCode() {
        return kitCode;
    }

    public void setKitCode(String kitCode) {
        this.kitCode = kitCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getActivateDate() {
        return activateDate;
    }

    public void setActivateDate(String activateDate) {
        this.activateDate = activateDate;
    }

    public int getNoOfEquipments() {
        return noOfEquipments;
    }

    public void setNoOfEquipments(int noOfEquipments) {
        this.noOfEquipments = noOfEquipments;
    }

    public String getPackTypes() {
        return packTypes;
    }

    public void setPackTypes(String packTypes) {
        this.packTypes = packTypes;
    }
}
