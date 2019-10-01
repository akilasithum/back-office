package com.back.office.entity;

import java.util.Date;

public class FACommission {

    private String id;
    private String flightNo;
    private String sector;
    private Date flightDate;
    private float totalSale;
    private int sifNo;
    private int faCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Date getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
    }

    public float getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(float totalSale) {
        this.totalSale = totalSale;
    }

    public int getSifNo() {
        return sifNo;
    }

    public void setSifNo(int sifNo) {
        this.sifNo = sifNo;
    }

    public int getFaCount() {
        return faCount;
    }

    public void setFaCount(int faCount) {
        this.faCount = faCount;
    }
}
