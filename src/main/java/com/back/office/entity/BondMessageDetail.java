package com.back.office.entity;
import java.util.Date;

public class BondMessageDetail {

    private int bondMessageId;
    private String flightNo;
    private Date flightDate ;
    private String messageBody;


    public int getBondMessageId() {
        return bondMessageId;
    }

    public void setBondMessageId(int bondMessageId) {
        this.bondMessageId = bondMessageId;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public Date getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
