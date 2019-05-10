package com.back.office.entity;
import java.util.Date;


public class FlightSheduleDetail {

    private int flightId;
    private Date flightDateTime;
    private String flightTime;
    private String aircraftRegistration;
    private String aircraftType;
    private String flightNumber;
    private String root;
    private String services;
    private String baseStation;

    public int getflightId() {
        return flightId;
    }

    public void setflightId(int flightId) {
        this.flightId = flightId;
    }

    public Date getflightDateTime() {
        return flightDateTime;
    }

    public void setflightDateTime(Date flightDateTime) {
        this.flightDateTime = flightDateTime;
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

    public String getroot() {
        return root;
    }

    public void setroot(String root) {
        this.root = root;
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
}

