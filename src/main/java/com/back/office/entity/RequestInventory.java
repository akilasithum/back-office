package com.back.office.entity;

import java.util.Date;

public class RequestInventory {

    private int reqInventoryId;
    private Date requestedDate;
    private String requestedUser;
    private String requestedWareHouse;
    private String requestedFrom;

    public int getReqInventoryId() {
        return reqInventoryId;
    }

    public void setReqInventoryId(int reqInventoryId) {
        this.reqInventoryId = reqInventoryId;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getRequestedUser() {
        return requestedUser;
    }

    public void setRequestedUser(String requestedUser) {
        this.requestedUser = requestedUser;
    }

    public String getRequestedWareHouse() {
        return requestedWareHouse;
    }

    public void setRequestedWareHouse(String requestedWareHouse) {
        this.requestedWareHouse = requestedWareHouse;
    }

    public String getRequestedFrom() {
        return requestedFrom;
    }

    public void setRequestedFrom(String requestedFrom) {
        this.requestedFrom = requestedFrom;
    }
}
