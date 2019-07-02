package com.back.office.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class BuildTime {

    private int SIFNo;
    private String deviceId;
    private Date downloaded;
    private String packedFor;
    private Date packedTime;
    private Date crewOpenedTime;
    private Date crewClosedTime;
    private long buildTime;

    public int getSIFNo() {
        return SIFNo;
    }

    public void setSIFNo(int SIFNo) {
        this.SIFNo = SIFNo;
    }

    public String getdeviceId() {
        return deviceId;
    }

    public void setdeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getdownloaded() {
        return downloaded;
    }

    public void setdownloaded(Date downloaded) {
        this.downloaded = downloaded;
    }

    public String getpackedFor() {
        return packedFor;
    }

    public void setpackedFor(String packedFor) {
        this.packedFor = packedFor;
    }

    public Date getpackedTime() {
        return packedTime;
    }

    public void setpackedTime(Date packedTime) {
        this.packedTime = packedTime;
    }

    public Date getcrewOpenedTime() {
        return crewOpenedTime;
    }

    public void setcrewOpenedTime(Date crewOpenedTime) {
        this.crewOpenedTime = crewOpenedTime;
    }

    public Date getcrewClosedTime() {
        return crewClosedTime;
    }

    public void setcrewClosedTime(Date 	crewClosedTime) {
        this.crewClosedTime = crewClosedTime;
    }

    public long getbuildTime() {
//    	buildTime=packedTime.getTime()-downloaded.getTime();
        if(packedTime != null && downloaded != null) {
            Duration timeElapsed = Duration.between(downloaded.toInstant(), packedTime.toInstant());
            return timeElapsed.toMinutes();
        }
        else return 0;
    }

    public void setbuildTime(long buildTime) {
        this.buildTime = buildTime;
    }


}
