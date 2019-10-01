package com.back.office.entity;

import java.util.Date;

public class FACommissionSetup {

    private int faCommisionId;
    private float faCommission;
    private Date updatedDateTime;
    private int recordStatus;

    public int getFaCommisionId() {
        return faCommisionId;
    }

    public void setFaCommisionId(int faCommisionId) {
        this.faCommisionId = faCommisionId;
    }

    public float getFaCommission() {
        return faCommission;
    }

    public void setFaCommission(float faCommission) {
        this.faCommission = faCommission;
    }

    public Date getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }
}
