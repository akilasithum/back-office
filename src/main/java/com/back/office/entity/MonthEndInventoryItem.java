package com.back.office.entity;

public class MonthEndInventoryItem {

    private int inventoryItemId;
    private int monthEndInventoryId;
    private String type;
    private String shelfCartNo;
    private String itemCode;
    private String itemDesc;
    private int qty;

    public int getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(int inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public int getMonthEndInventoryId() {
        return monthEndInventoryId;
    }

    public void setMonthEndInventoryId(int monthEndInventoryId) {
        this.monthEndInventoryId = monthEndInventoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getShelfCartNo() {
        return shelfCartNo;
    }

    public void setShelfCartNo(String shelfCartNo) {
        this.shelfCartNo = shelfCartNo;
    }
}
