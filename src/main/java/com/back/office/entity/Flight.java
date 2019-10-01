package com.back.office.entity;

public class Flight {

    private int flightId;
    private String baseStation;
    private String obFlightNo;
    private String obFlightFrom;
    private String obFlightTo;
    private int obNoOfSectors;
    private String ibFlightNo;
    private String ibFlightFrom;
    private String ibFlightTo;
    private int ibNoOfSectors;
    private int recordStatus;
    private String country;
    private String aircraftRegNo;

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getBaseStation() {
        return baseStation;
    }

    public void setBaseStation(String baseStation) {
        this.baseStation = baseStation;
    }

    public String getObFlightNo() {
        return obFlightNo;
    }

    public void setObFlightNo(String obFlightNo) {
        this.obFlightNo = obFlightNo;
    }

    public String getObFlightFrom() {
        return obFlightFrom;
    }

    public void setObFlightFrom(String obFlightFrom) {
        this.obFlightFrom = obFlightFrom;
    }

    public String getObFlightTo() {
        return obFlightTo;
    }

    public void setObFlightTo(String obFlightTo) {
        this.obFlightTo = obFlightTo;
    }

    public int getObNoOfSectors() {
        return obNoOfSectors;
    }

    public void setObNoOfSectors(int obNoOfSectors) {
        this.obNoOfSectors = obNoOfSectors;
    }

    public String getIbFlightNo() {
        return ibFlightNo;
    }

    public void setIbFlightNo(String ibFlightNo) {
        this.ibFlightNo = ibFlightNo;
    }

    public String getIbFlightFrom() {
        return ibFlightFrom;
    }

    public void setIbFlightFrom(String ibFlightFrom) {
        this.ibFlightFrom = ibFlightFrom;
    }

    public String getIbFlightTo() {
        return ibFlightTo;
    }

    public void setIbFlightTo(String ibFlightTo) {
        this.ibFlightTo = ibFlightTo;
    }

    public int getIbNoOfSectors() {
        return ibNoOfSectors;
    }

    public void setIbNoOfSectors(int ibNoOfSectors) {
        this.ibNoOfSectors = ibNoOfSectors;
    }

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAircraftRegNo() {
        return aircraftRegNo;
    }

    public void setAircraftRegNo(String aircraftRegNo) {
        this.aircraftRegNo = aircraftRegNo;
    }
}
