package com.back.office.entity;

import java.util.Date;

public class FlightAmountSummary {

    private int noOfFlights;
    private String flightNo;
    private Float cashAmount;
    private Float creditCardAmount;
    private Float voucherAmount;
    private Float grossSale;
    private Float discount;
    private Float netSale;

    public int getNoOfFlights() {
        return noOfFlights;
    }

    public void setNoOfFlights(int noOfFlights) {
        this.noOfFlights = noOfFlights;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public Float getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Float cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Float getCreditCardAmount() {
        return creditCardAmount;
    }

    public void setCreditCardAmount(Float creditCardAmount) {
        this.creditCardAmount = creditCardAmount;
    }

    public Float getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(Float voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public Float getGrossSale() {
        return grossSale;
    }

    public void setGrossSale(Float grossSale) {
        this.grossSale = grossSale;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Float getNetSale() {
        return netSale;
    }

    public void setNetSale(Float netSale) {
        this.netSale = netSale;
    }
}
