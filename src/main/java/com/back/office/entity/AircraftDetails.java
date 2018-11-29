package com.back.office.entity;

public class AircraftDetails {

    private int aircraftId;
    private String aircraftName;
    private String galleyPosition;
    private String equipmentType;
    private int noOfFullCrts;
    private int totalNoOfSeats;
    private boolean active;
    private String UpdateDateAndtime;

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

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public int getNoOfFullCrts() {
        return noOfFullCrts;
    }

    public void setNoOfFullCrts(int noOfFullCrts) {
        this.noOfFullCrts = noOfFullCrts;
    }

    public int getTotalNoOfSeats() {
        return totalNoOfSeats;
    }

    public void setTotalNoOfSeats(int totalNoOfSeats) {
        this.totalNoOfSeats = totalNoOfSeats;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUpdateDateAndtime() {
        return UpdateDateAndtime;
    }

    public void setUpdateDateAndtime(String updateDateAndtime) {
        UpdateDateAndtime = updateDateAndtime;
    }
}
