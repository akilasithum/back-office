package com.back.office.entity;

import java.util.Date;

public class OrderNow {

    private int ordernowid;
    private String flightNo;
    private String time;
    private String destination;
    private String paxName;
    private String PNR;
    private String seatNo;
    private String orderNo;
    private Date orderInTime;
    private String status;

    public int getOrdernowid() {
        return ordernowid;
    }

    public void setOrdernowid(int ordernowid) {
        this.ordernowid = ordernowid;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPaxName() {
        return paxName;
    }

    public void setPaxName(String paxName) {
        this.paxName = paxName;
    }

    public String getPNR() {
        return PNR;
    }

    public void setPNR(String PNR) {
        this.PNR = PNR;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getOrderInTime() {
        return orderInTime;
    }

    public void setOrderInTime(Date orderInTime) {
        this.orderInTime = orderInTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
