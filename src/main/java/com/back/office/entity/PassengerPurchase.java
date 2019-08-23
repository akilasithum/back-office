package com.back.office.entity;

import java.util.Date;

public class PassengerPurchase {

    private String id;
    private String paxName;
    private String seatNo;
    private String pnr;
    private String email;
    private String flightId;
    private String orderId;
    private Date flightDate;
    private String frequentFlyerNo;
    private String telephoneNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaxName() {
        return paxName;
    }

    public void setPaxName(String paxName) {
        this.paxName = paxName;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
    }

    public String getFrequentFlyerNo() {
        return frequentFlyerNo;
    }

    public void setFrequentFlyerNo(String frequentFlyerNo) {
        this.frequentFlyerNo = frequentFlyerNo;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
