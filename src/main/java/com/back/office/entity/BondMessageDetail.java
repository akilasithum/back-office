package com.back.office.entity;
import java.util.Date;

public class BondMessageDetail {

    private int bondMessageId;
    private String flightNo;
    private Date flightDate ;
    private String messageBody;

    public int getbondMessageId() {
        return bondMessageId;
    }

    public void setbondMessageId(int bondMessageId) {
        this.bondMessageId = bondMessageId;
    }

    public String getflightNo() {
        return flightNo;
    }

    public void setflightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public Date getflightDate() {
        return flightDate;
    }

    public void setflightDate(Date flightDate) {
        this.flightDate= flightDate;
    }

    public String getmessageBody() {
        return messageBody;
    }

    public void setmessageBody(String messageBody) {
        this.messageBody = messageBody;
    }




}
