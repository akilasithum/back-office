package com.back.office.entity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.poiji.annotation.ExcelCellName;

@XmlAccessorType(XmlAccessType.FIELD)


public class FlightSheduleDetail {

    private int flightId;
    @ExcelCellName("Date")
    private Date flightDateTime;
    @ExcelCellName("Time")
    private String flightTime;
    @ExcelCellName("ACFT Reg")
    private String aircraftRegistration;
    @ExcelCellName("Type")
    private String aircraftType;
    @ExcelCellName("Flight Number")
    private String flightNumber;
    @ExcelCellName("Services")
    private String services;
    @ExcelCellName("Base Station")
    private String baseStation;
    @ExcelCellName("From")
    private String from;
    @ExcelCellName("To")
    private String to;
    private String status;
    private Date buildStartTime;
    private Date buildEndTime;
    private String totalBuildTime;

    public Date getFlightDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        try {
            return format.parse(format.format(flightDateTime));
        } catch (ParseException e) {
            return null;
        }
    }

    public void setFlightDateTime(Date flightDateTime) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        try {
            this.flightDateTime = format.parse(format.format(flightDateTime));
        } catch (ParseException e) {
            this.flightDateTime = null;
        }

    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(String flightTime) {
        this.flightTime = flightTime;
    }

    public String getAircraftRegistration() {
        return aircraftRegistration;
    }

    public void setAircraftRegistration(String aircraftRegistration) {
        this.aircraftRegistration = aircraftRegistration;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getBaseStation() {
        return baseStation;
    }

    public void setBaseStation(String baseStation) {
        this.baseStation = baseStation;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getBuildStartTime() {
        return buildStartTime;
    }

    public void setBuildStartTime(Date buildStartTime) {
        this.buildStartTime = buildStartTime;
    }

    public Date getBuildEndTime() {
        return buildEndTime;
    }

    public void setBuildEndTime(Date buildEndTime) {
        this.buildEndTime = buildEndTime;
    }

    public String getTotalBuildTime() {
        return totalBuildTime;
    }

    public void setTotalBuildTime(String totalBuildTime) {
        this.totalBuildTime = totalBuildTime;
    }
}

