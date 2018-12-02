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
        iconMap.put("common", FontAwesome.DASHBOARD);
        iconMap.put("aircraft-type", FontAwesome.PLANE);
        iconMap.put("flight-number", FontAwesome.PAPER_PLANE);
        iconMap.put("currency", FontAwesome.MONEY);
        iconMap.put("create-items", FontAwesome.SHOPPING_BAG);
        iconMap.put("equipment-types", FontAwesome.SHOPPING_CART);
        iconMap.put("assign-items", FontAwesome.PENCIL);
        iconMap.put("create-kit-codes", FontAwesome.ANCHOR);
        iconMap.put("staff", FontAwesome.SMILE_O);
        iconMap.put("cc-black-list", FontAwesome.CREDIT_CARD);
        iconMap.put("cc-number-range", FontAwesome.CREDIT_CARD_ALT);
        iconMap.put("promotions", FontAwesome.FOLDER);
        iconMap.put("update-inventory", FontAwesome.UPLOAD);
        return iconMap;
    }

    public static List<String> getSetupMap(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Aircraft Type");
        menuItems.add("Flight Number");
        menuItems.add("Currency");
        menuItems.add("Create Items");
        menuItems.add("Equipment Types");
        menuItems.add("Assign Items");
        menuItems.add("Create Kit Codes");
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

