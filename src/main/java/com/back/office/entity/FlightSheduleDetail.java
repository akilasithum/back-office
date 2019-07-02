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

    public int getflightId() {
        return flightId;
    }

    public void setflightId(int flightId) {
        this.flightId = flightId;
    }

    public Date getflightDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        try {
            return format.parse(format.format(flightDateTime));
        } catch (ParseException e) {
            return null;
        }
    }

    public void setflightDateTime(Date flightDateTime) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        try {
            this.flightDateTime = format.parse(format.format(flightDateTime));
        } catch (ParseException e) {
            this.flightDateTime = null;
        }

    }

    public String getflightTime() {
        return flightTime;
    }

    public void setflightTime(String flightTime) {
        this.flightTime = flightTime;
    }

    public String getaircraftRegistration() {
        return aircraftRegistration;
    }

    public void setaircraftRegistration(String aircraftRegistration) {
        this.aircraftRegistration = aircraftRegistration;
    }

    public String getaircraftType() {
        return aircraftType;
    }

    public void setaircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getflightNumber() {
        return flightNumber;
    }

    public void setflightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getservices() {
        return services;
    }

    public void setservices(String services) {
        this.services = services;
    }

    public String getbaseStation() {
        return baseStation;
    }

    public void setbaseStation(String baseStation) {
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
}

