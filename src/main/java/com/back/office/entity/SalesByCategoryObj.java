package com.back.office.entity;

import java.util.Date;

public class SalesByCategoryObj {

    private String category;
    private Integer quantity;
    private String qtyPercentage;
    private float retailAmount;
    private float retailAmountPercentage;
    private float discount;
    private float netAmount;
    private String netAmountPercentage;
    private Date flightDate;

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

    public String getQtyPercentage() {
        return qtyPercentage;
    }

    public void setQtyPercentage(String qtyPercentage) {
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

    public String getNetAmountPercentage() {
        return netAmountPercentage;
    }

    public void setNetAmountPercentage(String netAmountPercentage) {
        this.netAmountPercentage = netAmountPercentage;
    }

    public Date getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
    }
}
