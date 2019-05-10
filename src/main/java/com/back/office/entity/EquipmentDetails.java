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
    private int recordStatus;
    private String cartManufacturer;
    private float cartWeight;
    private String drawerManufacturer;
    private float draweWeight;

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

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

    public String getCartManufacturer() {
        return cartManufacturer;
    }

    public void setCartManufacturer(String cartManufacturer) {
        this.cartManufacturer = cartManufacturer;
    }

    public float getCartWeight() {
        return cartWeight;
    }

    public void setCartWeight(float cartWeight) {
        this.cartWeight = cartWeight;
    }

    public String getDrawerManufacturer() {
        return drawerManufacturer;
    }

    public void setDrawerManufacturer(String drawerManufacturer) {
        this.drawerManufacturer = drawerManufacturer;
    }

    public float getDraweWeight() {
        return draweWeight;
    }

    public void setDraweWeight(float draweWeight) {
        this.draweWeight = draweWeight;
    }

    @Override
    public String toString(){
        return packType;
    }
}
