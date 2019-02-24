package com.back.office.entity;

import com.poiji.annotation.ExcelCellName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ItemDetails {
    private int itemId;
    @ExcelCellName("Item Description")
    private String itemName;
    @ExcelCellName("Service Type")
    private String serviceType;
    @ExcelCellName("Category")
    private String category;
    @ExcelCellName("Catalogue No")
    private String catalogue;
    @ExcelCellName("Weight (Grams)")
    private float weight;
    @ExcelCellName("Cost Currency")
    private String costCurrency;
    @ExcelCellName("Cost Price")
    private float costPrice;
    @ExcelCellName("Base Currency")
    private String baseCurrency;
    @ExcelCellName("Base Price")
    private float basePrice;
    @ExcelCellName("Activate Date")
    private String activateDate;
    @ExcelCellName("Item No")
    private String itemCode;
    @ExcelCellName("Second Currency")
    private String secondCurrency;
    @ExcelCellName("Second Price")
    private float secondPrice;
    @ExcelCellName("De listed")
    private String deListed;
    @ExcelCellName("RFID")
    private String nfcId;
    @ExcelCellName("Barcode")
    private String barcode;
    private int recordStatus;
    private byte[] image;

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(String catalogue) {
        this.catalogue = catalogue;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getCostCurrency() {
        return costCurrency;
    }

    public void setCostCurrency(String costCurrency) {
        this.costCurrency = costCurrency;
    }

    public float getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(float costPrice) {
        this.costPrice = costPrice;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public String getActivateDate() {
        return activateDate;
    }

    public void setActivateDate(String activateDate) {
        this.activateDate = activateDate;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getSecondCurrency() {
        return secondCurrency;
    }

    public void setSecondCurrency(String secondCurrency) {
        this.secondCurrency = secondCurrency;
    }

    public float getSecondPrice() {
        return secondPrice;
    }

    public void setSecondPrice(float secondPrice) {
        this.secondPrice = secondPrice;
    }

    public String getDeListed() {
        return deListed;
    }

    public void setDeListed(String deListed) {
        this.deListed = deListed;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getNfcId() {
        return nfcId;
    }

    public void setNfcId(String nfcId) {
        this.nfcId = nfcId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }



    @Override
    public String toString(){
        return itemName;
    }
}
