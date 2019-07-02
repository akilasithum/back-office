package com.back.office.entity;

import java.util.Date;

public class PassengerPurchases {

    private int paxId;
    private String PNR;
    private String paxName;
    private String email;
    private String telephoneNo;
    private String frequentFlyerNo;
    private Date deparureDate;
    private String flightNo;
    private String orderId;

    public int getpaxId() {
        return paxId;
    }

    public void setpaxId(int paxId) {
        this.paxId = paxId;
    }

    public String getPNR() {
        return PNR;
    }

    public void setPNR(String PNR) {
        this.PNR = PNR;
    }

    public String getpaxName() {
        return paxName;
    }

    public void setpaxName(String paxName) {
        this.paxName = paxName;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String gettelephoneNo() {
        return telephoneNo;
    }

    public void settelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public String getfrequentFlyerNo() {
        return frequentFlyerNo;
    }

    public void setfrequentFlyerNo(String frequentFlyerNo) {
        this.frequentFlyerNo = frequentFlyerNo;
    }

    public Date getdeparureDate() {
        return deparureDate;
    }

    public void setdeparureDate(Date deparureDate) {
        this.deparureDate = deparureDate;
    }

    public String getflightNo() {
        return flightNo;
    }

    public void setflightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getorderId () {
        return orderId ;
    }

    public void setorderId (String orderId ) {
        this.orderId  = orderId;
    }
}
