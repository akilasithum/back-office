package com.back.office.entity;

import com.back.office.utils.BackOfficeUtils;
import com.poiji.annotation.ExcelCellName;

import java.util.Date;

public class DepartureFlight {

    private int depFlightId;
    @ExcelCellName("Flight No")
    private String flightNo;
    private Date flightTime;
    @ExcelCellName("Time")
    private String dateString;
    @ExcelCellName("Airline")
    private String airline;
    @ExcelCellName("Destination")
    private String destination;
    @ExcelCellName("Check-In")
    private String checkin;
    @ExcelCellName("Gate")
    private String gate;
    @ExcelCellName("Status")
    private String status;

    public int getDepFlightId() {
        return depFlightId;
    }

    public void setDepFlightId(int depFlightId) {
        this.depFlightId = depFlightId;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public Date getFlightTime() {
        if(dateString == null) {
            return flightTime;
        }
        else{
            return BackOfficeUtils.getDateFromDateTimeStr(dateString);
        }
    }

    public void setFlightTime(Date flightTime) {
        this.flightTime = flightTime;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
