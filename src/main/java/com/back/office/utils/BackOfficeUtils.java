package com.back.office.utils;

import com.back.office.db.DBConnection;
import com.back.office.entity.PermissionCodes;
import com.vaadin.server.FontAwesome;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BackOfficeUtils {

    protected static DBConnection connection = DBConnection.getInstance();

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

    public static String getDateFromDateTime(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format( date );
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
        menuItems.add("Vouchers");
        return menuItems;
    }

    public static List<String> getSellsReportsMap(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Sales Details");
        menuItems.add("Sales Summary by Flight");
        menuItems.add("Sale by Category");
        menuItems.add("Tender Summary");
        menuItems.add("Sales by Sector");
        menuItems.add("Sold out by Flight");
        menuItems.add("Credit Card by Flight");
        menuItems.add( "Credit Card Summary");
        menuItems.add("Sales Tender Discrepancy");
        return menuItems;
    }

    public static List<String> getAuthorizationMap(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Manage User Roles");
        menuItems.add("Manage Users");
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

    public static String getServiceTypeFromServiceType(String serviceType){
        switch (serviceType){
            case "Buy on Board" : return "BOB";
            case "Duty Paid" : return "DTP";
            case "Duty Free" : return "DTF";
            case "Virtual inventory" : return "VRT";
            default: return null;
        }
    }

    public static Map<String, Map<Integer, String>> getPermissionCodes(){
            Map<String, Map<Integer, String>> funcAreasCodesMap = new HashMap<>();
            List<PermissionCodes> permissionCodes = (List<PermissionCodes>) connection.getAllValues("com.back.office.entity.PermissionCodes");
            for (PermissionCodes permissionCode : permissionCodes) {
                if (funcAreasCodesMap.containsKey(permissionCode.getFuncArea())) {
                    funcAreasCodesMap.get(permissionCode.getFuncArea()).put(permissionCode.getPermissionCode(), permissionCode.getDisplayName());
                } else {
                    Map<Integer, String> map = new HashMap<>();
                    map.put(permissionCode.getPermissionCode(), permissionCode.getDisplayName());
                    funcAreasCodesMap.put(permissionCode.getFuncArea(), map);
                }
            }
            return funcAreasCodesMap;
    }
}

