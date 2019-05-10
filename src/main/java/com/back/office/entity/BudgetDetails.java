package com.back.office.entity;

public class BudgetDetails {

    private int estimateId;
    private int year;
    private String month;
    private float budgetEstimate;
    private int estimatedPax;
    private float estimatedBudgetForPax;


    public int getestimateId() {
        return estimateId;
    }

    public void setestimateId(int estimateId) {
        this.estimateId = estimateId;
    }

    public int getyear() {
        return year;
    }

    public void setyear(int year) {
        this.year = year;
    }

    public String getmonth() {
        return month;
    }

    public void setmonth(String month) {
        this.month = month;
    }

    public float getbudgetEstimate() {
        return budgetEstimate;
    }

    public void setbudgetEstimate(float budgetEstimate) {
        this.budgetEstimate= budgetEstimate;
    }

    public int getestimatedPax() {
        return estimatedPax;
    }

    public void setestimatedPax(int estimatedPax) {
        this.estimatedPax = estimatedPax;
    }

    public float getestimatedBudgetForPax() {
        return estimatedBudgetForPax;
    }

    public void setestimatedBudgetForPax(float estimatedBudgetForPax) {
        this.estimatedBudgetForPax = estimatedBudgetForPax;
    }
}
