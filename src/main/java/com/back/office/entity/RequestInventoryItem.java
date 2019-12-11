package com.back.office.entity;

public class RequestInventoryItem {

    private int reqItemId;
    private int reqInventoryId;
    private String itemCode;
    private String itemName;
    private int qty;

    public int getReqItemId() {
        return reqItemId;
    }

    public void setReqItemId(int reqItemId) {
        this.reqItemId = reqItemId;
    }

    public int getReqInventoryId() {
        return reqInventoryId;
    }

    public void setReqInventoryId(int reqInventoryId) {
        this.reqInventoryId = reqInventoryId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
