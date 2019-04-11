package com.back.office.utils;

import com.back.office.db.DBConnection;
import com.back.office.entity.PermissionCodes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import kaesdingeling.hybridmenu.components.Notification;
import kaesdingeling.hybridmenu.components.NotificationCenter;

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

    public static Map<String, VaadinIcons> getIconMap(){
        Map<String, VaadinIcons> iconMap = new HashMap<>();
        iconMap.put("Dashboard", VaadinIcons.DASHBOARD);
        iconMap.put("Authorization", VaadinIcons.LOCK);
        iconMap.put("Flight Kitchen", VaadinIcons.BOOK);
        iconMap.put("Pre-order / Messenger", VaadinIcons.CART_O);
        iconMap.put("Finance", VaadinIcons.DOLLAR);
        iconMap.put("Reports", VaadinIcons.BOOKMARK);
        iconMap.put("Inventory", VaadinIcons.INVOICE);
        iconMap.put("CRM", VaadinIcons.PALETE);
        iconMap.put("Analysis and Uploads", VaadinIcons.DATE_INPUT);
        iconMap.put("Setup", VaadinIcons.USER);
//        iconMap.put("Analysis", VaadinIcons.BAR_CHART);
//        iconMap.put("Special Reports", VaadinIcons.MONEY);
//        iconMap.put("Pre Order Management", VaadinIcons.TRUCK);
//        iconMap.put("CRM", VaadinIcons.PALETE);
        return iconMap;
    }

    public static List<String> getSubMeuList(String menu){
        List<String> menuItems = new ArrayList<>();
        switch (menu){
            case "Setup" : return getSetupMap();
            case "Authorization" : return getAuthorizationMap();
            case  "Pre-order / Messenger" : return getPreOrder();
            case  "Finance" : return getFinance();
            case  "Reports" : return getSellsReportsMap();
            case  "Inventory" : return getInventory();
            case  "CRM" : return getcrm();
            case  "Flight Kitchen" : return getFlightKitchen();
            case  "Analysis and Uploads" : return getAnalysisUpload();
            default:return menuItems;
        }



    }

    public static List<String> getSetupMap(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Aircraft Type");
        menuItems.add("Flight Details");
        menuItems.add("Currency");
        menuItems.add("Create Items");
        menuItems.add("Equipment Types");
        menuItems.add("Assign Items");
        menuItems.add("Create Kit Codes");
        menuItems.add("CC Black List");
        menuItems.add("CC Number Range");
        menuItems.add("Promotions");
        menuItems.add("Update Inventory");
        menuItems.add("Vouchers");
        return menuItems;
    }

    public static List<String> getPreOrder(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("pre Order");
        menuItems.add("Pre-order Inventory");
        menuItems.add("Inflight Requests");
        menuItems.add("Bond Messages");
        menuItems.add("FA Messages");
        return menuItems;
    }

    public static List<String> getAuthorizationMap(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Manage User Roles");
        menuItems.add("Manage Users");
        menuItems.add("View and Edit User");
        return menuItems;
    }

    public static List<String> getFinance(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Currency History");
        menuItems.add("Bank Settlements");
        menuItems.add("CC Batch Summary");
        menuItems.add("FA Commissions");
        menuItems.add("Gross Margins");
        menuItems.add("Sales Tender Discrepancy");
        return menuItems;
    }

    public static List<String> getInventory(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Equipment Master");
        menuItems.add("Month End  Inventory");
        menuItems.add("Unaccounted Carts");
        menuItems.add("Inventory Valuation");
        menuItems.add("Sold-out by Flight");
        menuItems.add("Custom Reports");
        return menuItems;
    }

    public static List<String> getFlightKitchen(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Flight Schedule");
        menuItems.add("Daily Flights");
        menuItems.add("Request Inventory");
        menuItems.add("Galley Weights");
        menuItems.add("SIF");
        menuItems.add("HHC and Cart Usage");
        return menuItems;
    }

    public static List<String> getcrm(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Passenger Purchases");
        menuItems.add("Import pax Manifest");
        menuItems.add("Loading Recommendations");
        return menuItems;
    }

    public static List<String> getAnalysisUpload(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Wastage");
        menuItems.add("SIF Inquiry");
        menuItems.add("Build Times");
        menuItems.add("Sales vs Weight");
        menuItems.add("Airline Uploads");
        menuItems.add("Custom Reports");
        return menuItems;
    }
    public static List<String> getSellsReportsMap(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Monthly Sales");
        menuItems.add("Flight Sales");
        menuItems.add("Category/Sector Sales");
        menuItems.add("Sale By Category");
        menuItems.add("Item Sales");
        menuItems.add("Tender Summary");
        menuItems.add("FA Performance");
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

    public static String getServicehFromServiceh(String serviceh){
        switch (serviceh){
            case "HHC Order": return "HCC";
            case "Call Center Order" : return "CallCenter";
            case "Web Order" : return "Web";
            case "All":return "All";

            default: return null;
        }
    }

    public static List<String> getMainMenuItems(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Authorization");
        menuItems.add("Flight Kitchen");
        menuItems.add("Pre-order / Messenger");
        menuItems.add("Finance");
        menuItems.add("Reports");
        menuItems.add("Inventory");
        menuItems.add("CRM");
        menuItems.add("Analysis and Uploads");
        menuItems.add("Setup");
//        menuItems.add("Staff");
//        menuItems.add("Special Reports");
//        menuItems.add("Pre Order Management");
//        menuItems.add("CRM");
        return menuItems;
    }

    public static Map<String, Map<Integer, String>> getPermissionCodes(){
        Map<String, Map<Integer, String>> funcAreasCodesMap = new HashMap<>();
        List<PermissionCodes> permissionCodes = (List<PermissionCodes>) connection.getAllPermissionCodes();
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

    public static void showNotification(String caption,String content,VaadinIcons icon){
        NotificationCenter notificationCenter = VaadinSession.getCurrent().getAttribute(NotificationCenter.class);

        Notification notification = Notification.get()
                .withTitle(caption)
                .withContent(content)
                .withIcon(icon)
                .withDisplayTime(5000);
        notification.withCloseable();
        notification.withAutoRemove();
        notification.makeAsReaded();

        notificationCenter.add(notification, true);

    }
}

