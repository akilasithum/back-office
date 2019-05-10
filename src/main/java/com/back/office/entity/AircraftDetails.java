package com.back.office.entity;

import java.util.Date;

public class AircraftDetails {

    private int aircraftId;
    private String aircraftName;
    private String galleyPosition;
    private boolean active;
    private Date UpdateDateAndtime;
    private String registrationNumber;
    private int ecoClassSeatCount;
    private int businessClassSeatCount;
    private int frontFullCarts;
    private int frontHalfCarts;
    private int frontContainers;
    private int middleFullCarts;
    private int middleHalfCarts;
    private int middleContainers;
    private int rearFullCarts;
    private int rearHalfCarts;
    private int rearContainers;
    private int recordStatus;


    public int getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(int aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getAircraftName() {
        return aircraftName;
    }

    public void setAircraftName(String aircraftName) {
        this.aircraftName = aircraftName;
    }

    public String getGalleyPosition() {
        return galleyPosition;
    }

    public void setGalleyPosition(String galleyPosition) {
        this.galleyPosition = galleyPosition;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getUpdateDateAndtime() {
        return UpdateDateAndtime;
    }

    public void setUpdateDateAndtime(Date updateDateAndtime) {
        UpdateDateAndtime = updateDateAndtime;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getEcoClassSeatCount() {
        return ecoClassSeatCount;
    }

    public void setEcoClassSeatCount(int ecoClassSeatCount) {
        this.ecoClassSeatCount = ecoClassSeatCount;
    }

    public int getBusinessClassSeatCount() {
        return businessClassSeatCount;
    }

    public void setBusinessClassSeatCount(int businessClassSeatCount) {
        this.businessClassSeatCount = businessClassSeatCount;
    }

    public int getFrontFullCarts() {
        return frontFullCarts;
    }

    public void setFrontFullCarts(int frontFullCarts) {
        this.frontFullCarts = frontFullCarts;
    }

    public int getFrontHalfCarts() {
        return frontHalfCarts;
    }

    public void setFrontHalfCarts(int frontHalfCarts) {
        this.frontHalfCarts = frontHalfCarts;
    }

    public int getFrontContainers() {
        return frontContainers;
    }

    public void setFrontContainers(int frontContainers) {
        this.frontContainers = frontContainers;
    }

    public int getMiddleFullCarts() {
        return middleFullCarts;
    }

    public void setMiddleFullCarts(int middleFullCarts) {
        this.middleFullCarts = middleFullCarts;
    }

    public int getMiddleHalfCarts() {
        return middleHalfCarts;
    }

    public void setMiddleHalfCarts(int middleHalfCarts) {
        this.middleHalfCarts = middleHalfCarts;
    }

    public int getMiddleContainers() {
        return middleContainers;
    }

    public void setMiddleContainers(int middleContainers) {
        this.middleContainers = middleContainers;
    }

    public int getRearFullCarts() {
        return rearFullCarts;
    }

    public void setRearFullCarts(int rearFullCarts) {
        this.rearFullCarts = rearFullCarts;
    }

    public int getRearHalfCarts() {
        return rearHalfCarts;
    }

    public void setRearHalfCarts(int rearHalfCarts) {
        this.rearHalfCarts = rearHalfCarts;
    }

    public int getRearContainers() {
        return rearContainers;
    }

    public void setRearContainers(int rearContainers) {
        this.rearContainers = rearContainers;
    }

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }
}
