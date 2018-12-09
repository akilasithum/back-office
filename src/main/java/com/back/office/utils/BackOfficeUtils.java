package com.back.office.utils;

import com.vaadin.server.FontAwesome;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BackOfficeUtils {


    public static String getCurrentDateAndTime(){
        return new SimpleDateFormat(Constants.DATE_TIME_FORMAT).format(new Date());
    }

    public static Date convertStringToDate(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getDateStringFromDate(Date date){
        return new SimpleDateFormat(Constants.DATE_FORMAT).format(date);
    }

    public static Map<String, FontAwesome> getIconMap(){
        Map<String, FontAwesome> iconMap = new HashMap<>();
        iconMap.put("dashboard", FontAwesome.DASHBOARD);
        iconMap.put("authorization", FontAwesome.LOCK);
        iconMap.put("setup", FontAwesome.COG);
        iconMap.put("uploads", FontAwesome.UPLOAD);
        iconMap.put("generateXML", FontAwesome.MAP);
        iconMap.put("bondReports", FontAwesome.FLAG);
        iconMap.put("salesReport", FontAwesome.BALANCE_SCALE);
        iconMap.put("analysis", FontAwesome.BAR_CHART);
        iconMap.put("specialReports", FontAwesome.MONEY);
        iconMap.put("preOrderManagement", FontAwesome.TRUCK);
        iconMap.put("CRM", FontAwesome.USER);
        return iconMap;
    }

    public static List<String> getSetupMap(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Aircraft Type");
        menuItems.add("Flight Details");
        menuItems.add("Currency");
        menuItems.add("Create Items");
        menuItems.add("Create Kit Codes");
        menuItems.add("Equipment Types");
        menuItems.add("Assign Items");
        menuItems.add( "Staff");
        menuItems.add("CC Black List");
        menuItems.add("CC Number Range");
        menuItems.add("Promotions");
        menuItems.add("Update Inventory");
        return menuItems;
    }

    public static boolean isInteger(String val){
        try{
            int intVal = Integer.parseInt(val);
            if(intVal >= 0){
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
    }

    public static boolean isFloat(String val){
        try{
            float intVal = Float.parseFloat(val);
            if(intVal >= 0){
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
    }

    public static List<String> getCategoryFromServiceType(String serviceType){

        List<String> categoryList = new ArrayList<>();
        if(serviceType.equals("BOB")){
            categoryList.add("Main");
            categoryList.add("Snack");
            categoryList.add("Beverages");
            categoryList.add("Other");
        }
        else if(serviceType.equals("DTP") || serviceType.equals("DTF")){
            categoryList.add("Liquor and Tobacco");
            categoryList.add("Perfumes and Cosmetics");
            categoryList.add("Watches and Jewellery");
            categoryList.add("Gifts and Souvenir");
        }
        else if(serviceType.equals("VRT")){
            categoryList.add("Upgrade");
            categoryList.add("Travel");
            categoryList.add("Executions");
            categoryList.add("Gift Cards");
        }
        return categoryList;
    }
}

