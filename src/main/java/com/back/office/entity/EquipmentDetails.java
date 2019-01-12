package com.back.office.entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class EquipmentDetails {

    private int equipmentId;
    private String packType;
    private String packDescription;
    private String equipmentType;
    private String kitCode;
    private int noOfDrawers;

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getPackDescription() {
        return packDescription;
    }

    public void setPackDescription(String packDescription) {
        this.packDescription = packDescription;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getKitCode() {
        return kitCode;
    }

    public void setKitCode(String kitCode) {
        this.kitCode = kitCode;
    }

    public int getNoOfDrawers() {
        return noOfDrawers;
    }

    public void setNoOfDrawers(int noOfDrawers) {
        this.noOfDrawers = noOfDrawers;
    }

    @Override
    public String toString(){
        return packType;
    }
}
