package com.back.office.entity;

public class SalesByCategoryObj {

    private String category;
    private Integer quantity;
    private float qtyPercentage;
    private float retailAmount;
    private float retailAmountPercentage;
    private float discount;
    private float netAmount;
    private float netAmountPercentage;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public float getQtyPercentage() {
        return qtyPercentage;
    }

    public void setQtyPercentage(float qtyPercentage) {
        this.qtyPercentage = qtyPercentage;
    }

    public float getRetailAmount() {
        return retailAmount;
    }

    public void setRetailAmount(float retailAmount) {
        this.retailAmount = retailAmount;
    }

    public float getRetailAmountPercentage() {
        return retailAmountPercentage;
    }

    public void setRetailAmountPercentage(float retailAmountPercentage) {
        this.retailAmountPercentage = retailAmountPercentage;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(float netAmount) {
        this.netAmount = netAmount;
    }

    public float getNetAmountPercentage() {
        return netAmountPercentage;
    }

    public void setNetAmountPercentage(float netAmountPercentage) {
        this.netAmountPercentage = netAmountPercentage;
    }
}
