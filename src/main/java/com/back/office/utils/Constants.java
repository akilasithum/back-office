package com.back.office.utils;

import com.vaadin.shared.ui.MarginInfo;

public class Constants {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_NO_SECONDS_FORMAT = "yyyy-MM-dd HH.mm";
    public static final String  DATE_FORMAT = "dd-MM-yyyy";
    public static final MarginInfo topMarginInfo = new MarginInfo(true,false,false,false);
    public static final MarginInfo bottomMarginInfo = new MarginInfo(false,false,true,false);
    public static final MarginInfo topBottomMarginInfo = new MarginInfo(true,false,true,false);
    public static final MarginInfo noMargin = new MarginInfo(false,false,false,false);
    public static final MarginInfo leftMargin = new MarginInfo(false,false,false,true);
    public static final MarginInfo leftBottomtMargin = new MarginInfo(false,false,true,true);
    public static final MarginInfo noTopMargin = new MarginInfo(false,true,true,true);
}
