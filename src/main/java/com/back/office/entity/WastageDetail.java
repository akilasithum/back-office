package com.back.office.entity;

import java.util.Date;

public class WastageDetail {

    private Integer closingInventoryItemId;
    private String itemId;
    private int quantity;
    private String cartNo;
    private String drawer;
    private String flightNo;
    private String sifNo;
    private Date flightDate;


    public Integer getclosingInventoryItemId() {
        return closingInventoryItemId;
    }

    public void setclosingInventoryItemId(Integer closingInventoryItemId) {
        this.closingInventoryItemId = closingInventoryItemId;
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

    public String getcartNo() {
        return cartNo;
    }

    public void setcartNo(String cartNo) {
        this.cartNo = cartNo;
    }

    public String getdrawer() {
        return drawer;
    }

    public void setdrawer(String drawer) {
        this.drawer = drawer;
    }

    public String getflightNo() {
        return flightNo;
    }

    public void setflightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getsifNo() {
        return sifNo;
    }

    public void setsifNo(String sifNo) {
        this.sifNo = sifNo;
    }

    public Date getflightDate() {
        return flightDate;
    }

    public void setflightDate(Date flightDate) {
        this.flightDate = flightDate;
    }


}
