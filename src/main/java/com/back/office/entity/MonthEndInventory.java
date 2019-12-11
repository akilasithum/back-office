package com.back.office.entity;

public class MonthEndInventory {

    private int monthEndInventoryId;
    private String month;
    private int year;
    private String station;
    private String user;
    private String types;

    public int getMonthEndInventoryId() {
        return monthEndInventoryId;
    }

    public void setMonthEndInventoryId(int monthEndInventoryId) {
        this.monthEndInventoryId = monthEndInventoryId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }
}
