package com.back.office.entity;

public class SIFSheet {

    private int sifSheetId;
    private int sifNo;
    private String itemNo;
    private String itemDesc;
    private float price;
    private String cart;
    private String drawer;
    private int obOpenQty;
    private int obSoldQty;
    private int obClosingQty;
    private int ibOpenQty;
    private int ibSoldQty;
    private int ibClosingQty;
    private String serviceType;

    public int getSifNo() {
        return sifNo;
    }

    public void setSifNo(int sifNo) {
        this.sifNo = sifNo;
    }

    public int getSifSheetId() {
        return sifSheetId;
    }

    public void setSifSheetId(int sifSheetId) {
        this.sifSheetId = sifSheetId;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public String getDrawer() {
        return drawer;
    }

    public void setDrawer(String drawer) {
        this.drawer = drawer;
    }

    public int getObOpenQty() {
        return obOpenQty;
    }

    public void setObOpenQty(int obOpenQty) {
        this.obOpenQty = obOpenQty;
    }

    public int getObSoldQty() {
        return obSoldQty;
    }

    public void setObSoldQty(int obSoldQty) {
        this.obSoldQty = obSoldQty;
    }

    public int getObClosingQty() {
        return obClosingQty;
    }

    public void setObClosingQty(int obClosingQty) {
        this.obClosingQty = obClosingQty;
    }

    public int getIbOpenQty() {
        return ibOpenQty;
    }

    public void setIbOpenQty(int ibOpenQty) {
        this.ibOpenQty = ibOpenQty;
    }

    public int getIbSoldQty() {
        return ibSoldQty;
    }

    public void setIbSoldQty(int ibSoldQty) {
        this.ibSoldQty = ibSoldQty;
    }

    public int getIbClosingQty() {
        return ibClosingQty;
    }

    public void setIbClosingQty(int ibClosingQty) {
        this.ibClosingQty = ibClosingQty;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
