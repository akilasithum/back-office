package com.back.office.entity;

public class PermissionCodes {

    private int permissionCode;
    private String displayName;
    private String funcArea;

    public int getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(int permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFuncArea() {
        return funcArea;
    }

    public void setFuncArea(String funcArea) {
        this.funcArea = funcArea;
    }
}
