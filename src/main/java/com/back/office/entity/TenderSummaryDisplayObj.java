package com.back.office.entity;

public class TenderSummaryDisplayObj extends TenderSummaryObj {

    private float grossSale;
    private float cashSale;
    private float voucherSale;
    private float creditCardSale;

    public float getGrossSale() {
        return grossSale;
    }

    public void setGrossSale(float grossSale) {
        this.grossSale = grossSale;
    }

    public float getCashSale() {
        return cashSale;
    }

    public void setCashSale(float cashSale) {
        this.cashSale = cashSale;
    }

    public float getVoucherSale() {
        return voucherSale;
    }

    public void setVoucherSale(float voucherSale) {
        this.voucherSale = voucherSale;
    }

    public float getCreditCardSale() {
        return creditCardSale;
    }

    public void setCreditCardSale(float creditCardSale) {
        this.creditCardSale = creditCardSale;
    }
}
