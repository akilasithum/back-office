package com.back.office.entity;

import com.back.office.utils.BackOfficeUtils;
import com.poiji.annotation.ExcelCellName;

import java.util.Date;

public class ArrivalFlight {

    private int arrFlightId;
    @ExcelCellName("Flight No")
    private String flightNo;
    private Date flightTime;
    @ExcelCellName("Time")
    private String dateString;
    @ExcelCellName("Airline")
    private String airline;
    @ExcelCellName("From")
    private String from;
    @ExcelCellName("Belt")
    private String belt;
    @ExcelCellName("Gate")
    private String gate;
    @ExcelCellName("Status")
    private String status;

    public int getArrFlightId() {
        return arrFlightId;
    }

    public void setArrFlightId(int arrFlightId) {
        this.arrFlightId = arrFlightId;
    }

    public String getFlightNo() {
        return flightNo;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBelt() {
        return belt;
    }

    public void setBelt(String belt) {
        this.belt = belt;
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
