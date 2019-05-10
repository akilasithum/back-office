package com.back.office.utils;

import com.back.office.db.DBConnection;
import com.back.office.entity.CurrencyDetails;
import com.back.office.entity.PermissionCodes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
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
        iconMap.put("Setup", VaadinIcons.COG);
        iconMap.put("File Transfer", VaadinIcons.UPLOAD);
        iconMap.put("Generate XML", VaadinIcons.MAP_MARKER);
        iconMap.put("Bond Reports", VaadinIcons.FLAG);
        iconMap.put("Sales Report", VaadinIcons.SCALE);
        iconMap.put("Staff", VaadinIcons.USER);
        iconMap.put("Analysis", VaadinIcons.BAR_CHART);
        iconMap.put("Special Reports", VaadinIcons.MONEY);
        iconMap.put("Pre Order Management", VaadinIcons.TRUCK);
        iconMap.put("CRM", VaadinIcons.PALETE);
        return iconMap;
    }

    public static List<String> getSubMeuList(String menu){
        List<String> menuItems = new ArrayList<>();
        switch (menu){
            case "Setup" : return getSetupMap();
            case "Authorization" : return getAuthorizationMap();
            case  "Sales Report" : return getSellsReportsMap();
            case  "Bond Reports" : return getBondReportsMap();
            case "File Transfer" : return getFileTransferMap();
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
        menuItems.add("View and Edit User");
        return menuItems;
    }

    public static List<String> getBondReportsMap(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Flight-Bond Activity");
        menuItems.add("Cart Usage");
        menuItems.add("HHC Status");
        menuItems.add("Pre Orders");
        return menuItems;
    }

    public static List<String> getFileTransferMap(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Uploads");
        menuItems.add("Downloads");
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

    public static List<String> getMainMenuItems(){
        List<String> menuItems = new ArrayList<>();
        menuItems.add("Authorization");
        menuItems.add("Setup");
        menuItems.add("Uploads");
        menuItems.add("Generate XML");
        menuItems.add("Bond Reports");
        menuItems.add("Sales Report");
        menuItems.add("Analysis");
        menuItems.add("Staff");
        menuItems.add("Special Reports");
        menuItems.add("Pre Order Management");
        menuItems.add("CRM");
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

        com.vaadin.ui.Notification.show(caption,content, com.vaadin.ui.Notification.Type.WARNING_MESSAGE);
            Notification notification = Notification.get()
                    .withTitle(caption)
                    .withContent(content)
                    .withIcon(icon)
                    .withDisplayTime(5000);
                notification.withCloseable();
                notification.withAutoRemove();
                notification.makeAsReaded();

    }

    public static List<String> getCurrencyDropDownValues(boolean allCurrency){
        List<CurrencyDetails> currencyDetails = connection.getAllCurrencies();
        List<String> currencies = new ArrayList<>();
        for(CurrencyDetails currency : currencyDetails){
            if(allCurrency)currencies.add(currency.getCurrencyCode());
            else{
                if(!currency.getCurrencyType().equals("Base")){
                    currencies.add(currency.getCurrencyCode());
                }
            }
        }
        return currencies;
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

    public static FormLayout getFormLayout(){
        FormLayout form = new FormLayout();
        List<String> list = new ArrayList<>();
        list.add("Full Carts");
        list.add("Half Carts");
        list.add("Containers");
        for(String cartType : list){
            ComboBox comboBox = new ComboBox(cartType);
            comboBox.setDescription(cartType);
            comboBox.setRequiredIndicatorVisible(true);
            comboBox.setItems(getNumberList(20));
            comboBox.setValue(0);
            form.addComponent(comboBox);
        }
        return form;
    }

    public static List<Integer> getNumberList(int number){
        List<Integer> list = new ArrayList<>();
        for(int i = 0;i<=number;i++){
            list.add(i);
        }
        return list;
    }

    public static float getFloat(String str){
        try{
            return Float.parseFloat(str);
        }
        catch (Exception e){
            return 0;
        }
    }

    public static int getInt(String str){
        try{
            return Integer.parseInt(str);
        }
        catch (Exception e){
            return 0;
        }
    }

    public static FormLayout getSectorFormLayout(int i){
        FormLayout formLayout = new FormLayout();
        formLayout.setSizeFull();
        formLayout.setMargin(Constants.noMargin);

        Label h1 = new Label("Sector " + i);
        h1.setSizeFull();
        h1.addStyleName(ValoTheme.LABEL_H3);
        formLayout.addComponent(h1);

        TextField sectorFromFld = new TextField("Sector From");
        sectorFromFld.setDescription("Sector From");
        formLayout.addComponent(sectorFromFld);

        TextField sectorToFld = new TextField("Sector To");
        sectorToFld.setDescription("Sector To");
        formLayout.addComponent(sectorToFld);

        ComboBox sectorTypeComboBox = new ComboBox("Sector Type");
        sectorTypeComboBox.setDescription("Sector Type");
        sectorTypeComboBox.setItems("International","Domestic");
        sectorTypeComboBox.setEmptySelectionAllowed(false);
        formLayout.addComponent(sectorTypeComboBox);

        return formLayout;
    }
}

