package com.back.office.entity;

public class PosItemSaleDetail {

    private int saleOrderId;

    private String 	orderId;

    private String itemId;

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

    public String getitemId() {
        return itemId;
    }

    public void setitemId(String itemId) {
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
