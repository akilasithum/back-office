package com.back.office.entity;

public class OrderNowItems {

    private int oedernowitemid;
    private String itemNo;
    private String orderId;
    private String itemDescription;
    private int Quantity;

    public int getOedernowitemid() {
        return oedernowitemid;
    }

    public void setOedernowitemid(int oedernowitemid) {
        this.oedernowitemid = oedernowitemid;
    }

    public String getItemNo() {
        return itemNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
