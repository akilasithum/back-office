package com.back.office.entity;

import java.util.Date;

public class SIFDetails {

    private int SIFNo;
    private String deviceId;
    private Date downloaded;
    private String packedFor;
    private Date packedTime;
    private Date crewOpenedTime;
    private Date crewClosedTime;

    public int getSIFNo() {
        return SIFNo;
    }

    public void setSIFNo(int SIFNo) {
        this.SIFNo = SIFNo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(Date downloaded) {
        this.downloaded = downloaded;
    }

    public String getPackedFor() {
        return packedFor;
    }

    public void setPackedFor(String packedFor) {
        this.packedFor = packedFor;
    }

    public Date getPackedTime() {
        return packedTime;
    }

    public void setPackedTime(Date packedTime) {
        this.packedTime = packedTime;
    }

    public Date getCrewOpenedTime() {
        return crewOpenedTime;
    }

    public void setCrewOpenedTime(Date crewOpenedTime) {
        this.crewOpenedTime = crewOpenedTime;
    }

    public Date getCrewClosedTime() {
        return crewClosedTime;
    }

    public void setCrewClosedTime(Date crewClosedTime) {
        this.crewClosedTime = crewClosedTime;
    }
}
