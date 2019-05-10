package com.back.office.entity;

public class ItemGross {

    private String itemId;
    private float costPrice;
    private float basePrice;
    private float margin;
    private float marginPresentage;
    private String itemDescription;


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public float getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(float costPrice) {
        this.costPrice = costPrice;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public float getMargine() {
        return margin;
    }

    public void setMargine(float margin) {
        this.margin = margin;
    }

    public float getMarginPresentage() {
        return marginPresentage;
    }

    public void setMarginPresentage(float marginPresentage) {
        this.marginPresentage = marginPresentage;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}
