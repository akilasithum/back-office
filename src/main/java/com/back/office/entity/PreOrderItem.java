package com.back.office.entity;

import java.util.Date;

public class PreOrderItem {

    private int preOrderItemId;
    private int preOrderId;
    private String category;
    private String itemNo;
    private int quantity;

    public int getPreOrderItemId() {
        return preOrderItemId;
    }

    public void setPreOrderItemId(int preOrderItemId) {
        this.preOrderItemId = preOrderItemId;
    }

    public int getPreOrderId() {
        return preOrderId;
    }

    public void setPreOrderId(int preOrderId) {
        this.preOrderId = preOrderId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
