package com.back.office.entity;

public class Sector {

    private int sectorId;
    private int flightId;
    private String sectorFrom;
    private String sectorTo;
    private String sectorType;

    public int getSectorId() {
        return sectorId;
    }

    public void setSectorId(int sectorId) {
        this.sectorId = sectorId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getSectorFrom() {
        return sectorFrom;
    }

    public void setSectorFrom(String sectorFrom) {
        this.sectorFrom = sectorFrom;
    }

    public String getSectorTo() {
        return sectorTo;
    }

    public void setSectorTo(String sectorTo) {
        this.sectorTo = sectorTo;
    }

    public String getSectorType() {
        return sectorType;
    }

    public void setSectorType(String sectorType) {
        this.sectorType = sectorType;
    }
}
