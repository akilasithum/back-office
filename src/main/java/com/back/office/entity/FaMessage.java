package com.back.office.entity;

import java.util.Date;

public class FaMessage {

    private Integer faMessageId;
    private String flightNumber;
    private Date flightDate;
    private String faName;
    private String comment;


    public Integer getfaMessageId() {
        return faMessageId;
    }

    public void setfaMessageId(Integer faMessageId) {
        this.faMessageId = faMessageId;
    }

    public String getflightNumber() {
        return flightNumber;
    }

    public void setflightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Date getflightDate() {
        return flightDate;
    }

    public void setflightDate(Date flightDate) {
        this.flightDate = flightDate;
    }

    public String getfaName() {
        return faName;
    }

    public void setfaName(String faName) {
        this.faName = faName;
    }

    public String getcomment() {
        return comment;
    }

    public void setcomment(String comment) {
        this.comment = comment;
    }


}
