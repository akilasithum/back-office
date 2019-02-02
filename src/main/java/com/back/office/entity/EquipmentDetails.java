package com.back.office.entity;
import com.poiji.annotation.ExcelCellName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class EquipmentDetails {

    private int equipmentId;
    @ExcelCellName("Pack Type")
    private String packType;
    @ExcelCellName("Pack Description")
    private String packDescription;
    @ExcelCellName("Equipment Type")
    private String equipmentType;
    @ExcelCellName("No of Drawers")
    private int noOfDrawers;
    @ExcelCellName("No of Seals")
    private int noOfSeals;

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

    public int getNoOfDrawers() {
        return noOfDrawers;
    }

    public void setNoOfDrawers(int noOfDrawers) {
        this.noOfDrawers = noOfDrawers;
    }

    public int getNoOfSeals() {
        return noOfSeals;
    }

    public void setNoOfSeals(int noOfSeals) {
        this.noOfSeals = noOfSeals;
    }

    @Override
    public String toString(){
        return packType;
    }
}
