package com.back.office.entity;

public class Cart {
    private int cartNumberId;
    private String cartNumber;
    private String sifNo;
    private String serviceType;
    private String packType;

    public int getCartNumberId() {
        return cartNumberId;
    }

    public void setCartNumberId(int cartNumberId) {
        this.cartNumberId = cartNumberId;
    }

    public String getCartNumber() {
        return cartNumber;
    }

    public void setCartNumber(String cartNumber) {
        this.cartNumber = cartNumber;
    }

    public String getSifNo() {
        return sifNo;
    }

    public void setSifNo(String sifNo) {
        this.sifNo = sifNo;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }
}
