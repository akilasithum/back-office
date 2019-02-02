package com.back.office.entity;

public class CCNumericRange {

    private int rangeId;
    private String numaricRange;
    private String cardType;
    private String cardDesc;
    private String startRange;
    private String endRange;
    private float genralLimit;
    private float authorizedLimit;

    public int getRangeId() {
        return rangeId;
    }

    public void setRangeId(int rangeId) {
        this.rangeId = rangeId;
    }

    public String getNumaricRange() {
        return numaricRange;
    }

    public void setNumaricRange(String numaricRange) {
        this.numaricRange = numaricRange;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardDesc() {
        return cardDesc;
    }

    public void setCardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
    }

    public String getStartRange() {
        return startRange;
    }

    public void setStartRange(String startRange) {
        this.startRange = startRange;
    }

    public String getEndRange() {
        return endRange;
    }

    public void setEndRange(String endRange) {
        this.endRange = endRange;
    }

    public float getGenralLimit() {
        return genralLimit;
    }

    public void setGenralLimit(float genralLimit) {
        this.genralLimit = genralLimit;
    }

    public float getAuthorizedLimit() {
        return authorizedLimit;
    }

    public void setAuthorizedLimit(float authorizedLimit) {
        this.authorizedLimit = authorizedLimit;
    }
}
