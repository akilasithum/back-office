package com.back.office.entity;

public class PosItemSaleDetail {

    private int saleOrderId;

    private String 	orderId;

    private int itemId;

    private int quantity;

    private float price;




    public int getsaleOrderId() {
        return saleOrderId;
    }

    public void setsaleOrderId(int saleOrderId) {
        this.saleOrderId = saleOrderId;
    }

    public String getorderId() {
        return 	orderId;
    }

    public void setorderId(String orderId) {
        this.orderId = orderId;
    }

    public int getitemId() {
        return itemId;
    }

    public void setitemId(int itemId) {
        this.itemId = itemId;
    }

    public int getquantity() {
        return quantity;
    }

    public void setquantity(int quantity) {
        this.quantity = quantity;
    }

    public float getprice() {
        return price;
    }

    public void setprice(float price) {
        this.price = price;
    }


}
