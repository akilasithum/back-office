package com.back.office.utils;

import com.back.office.db.DBConnection;
import com.back.office.entity.CurrencyDetails;
import com.back.office.entity.PermissionCodes;
import com.back.office.entity.SubMenuItem;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import kaesdingeling.hybridmenu.components.Notification;
import kaesdingeling.hybridmenu.components.NotificationCenter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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

    public static String getDateTimeFromDateTime(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format( date );
    }

    public static String getDateStringFromDate(Date date){
        if(date != null)
        return new SimpleDateFormat(Constants.DATE_FORMAT).format(date);
        else return null;
    }

    public static String getDateStringFromDateTime(Date date){
        if(date != null)
            return new SimpleDateFormat(Constants.DATE_TIME_FORMAT_NO_SECONDS).format(date);
        else return null;
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

    public static String getTimeStringFromDate(Date date){
        if(date != null)
            return new SimpleDateFormat("HH:mm").format(date);
        else return null;
    }

    public static FormLayout getSectorFormLayout(int i){
        FormLayout formLayout = new FormLayout();
        formLayout.setSizeFull();
        formLayout.setMargin(Constants.noMargin);

        Label h1 = new Label("Sector " + i);
        h1.setSizeFull();
        h1.addStyleName("textBlackColor");
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

    public static float formatDecimals(float val){
        NumberFormat formatter = new DecimalFormat("#0.00");
        return Float.parseFloat(formatter.format(val));
    }

    public static Date getDateFromDateTimeStr(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT_NO_SECONDS);

        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static VerticalLayout getHeaderLayout(){
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setWidth("80%");
        HorizontalLayout btnLayout1 = new HorizontalLayout();
        btnLayout1.setSizeFull();
        verticalLayout.setStyleName("iconLayoutBackgroundNoMargin");
        //btnLayout1.setMargin(Constants.bottomMarginInfo);
        verticalLayout.setMargin(Constants.noMargin);
        verticalLayout.addComponent(btnLayout1);
        verticalLayout.setComponentAlignment(btnLayout1,Alignment.MIDDLE_CENTER);
        String selectedLayout = (String)UI.getCurrent().getCurrent().getSession().getAttribute("selectedLayout");

        CssLayout backBtnLayout = new CssLayout();
        Button backBtn = new Button("Back");
        backBtnLayout.addComponents(backBtn);
        backBtn.setIcon(FontAwesome.ARROW_CIRCLE_LEFT);
        backBtnLayout.setStyleName("backBtn");
        backBtn.addClickListener(clickEvent -> {
           UI.getCurrent().getNavigator().navigateTo("CommonView");
        });

        CssLayout iconWrapper1 = new CssLayout();
        Button flightKitchenImage = new Button("Flight Kitchen");
        iconWrapper1.addComponents(flightKitchenImage);
        if(selectedLayout.equalsIgnoreCase("Flight Kitchen")) iconWrapper1.setStyleName("selected");
        else iconWrapper1.setStyleName("iconWrapper-11");
        flightKitchenImage.setIcon(FontAwesome.PLANE);
        flightKitchenImage.addClickListener(clickEvent -> {
            UI.getCurrent().getSession().setAttribute("selectedLayout","Flight Kitchen");
            SubMenuItem menuItem = new SubMenuItem();
            menuItem.setMenuImage("flight_kitchen_sub.png");
            Map<String,String> row1Map = new LinkedHashMap<>();
            row1Map.put("Flight Schedule","FlightSchedule");
            row1Map.put("Daily Flights","DailyFlights");
            row1Map.put("Request Inventory","RequestInventoryView");
            row1Map.put("Galley Weight","GalleyWeight");
            row1Map.put("SIF Details","SIFDetails");
            row1Map.put("HHC and Cart Usage","HHCAndCartUsage");
            menuItem.setMenuName("flight_kitchen");
            menuItem.setSubMenuImageMap(row1Map);
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            UI.getCurrent().getNavigator().navigateTo("CommonView");
        });


        CssLayout iconWrapper2 = new CssLayout();
        Button preOrderImage = new Button("pre order");
        iconWrapper2.addComponents(preOrderImage );
        if(selectedLayout.equalsIgnoreCase("pre order")) iconWrapper2.setStyleName("selected");
        else iconWrapper2.setStyleName("iconWrapper-21");
        preOrderImage.setIcon(FontAwesome.CART_ARROW_DOWN);

        preOrderImage.addClickListener(clickEvent -> {
            UI.getCurrent().getSession().setAttribute("selectedLayout","pre order");
            SubMenuItem menuItem = new SubMenuItem();
            menuItem.setMenuImage("pre_order_sub.png");
            Map<String,String> row1Map = new LinkedHashMap<>();
            row1Map.put("Pre-order Summary","PreOrders");
            row1Map.put("Pre-order Inventory","PreOrderInventory");
            row1Map.put("Messaging","MessagingModule");
            row1Map.put("Messages to HHC","BondMessages");
            row1Map.put("HHC FA","FAMessages");
            menuItem.setSubMenuImageMap(row1Map);
            menuItem.setMenuName("pre_order");
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            UI.getCurrent().getNavigator().navigateTo("CommonView");
        });



        CssLayout iconWrapper3 = new CssLayout();
        Button financeImage = new Button("finance");
        iconWrapper3.addComponents(financeImage);
        if(selectedLayout.equalsIgnoreCase("finance")) iconWrapper3.setStyleName("selected");
        else iconWrapper3.setStyleName("iconWrapper-31");
        financeImage.setIcon(FontAwesome.USD);

        financeImage.addClickListener(clickEvent -> {
            UI.getCurrent().getSession().setAttribute("selectedLayout","finance");
            SubMenuItem menuItem = new SubMenuItem();
            menuItem.setMenuImage("finance_sub.png");
            Map<String,String> row1Map = new LinkedHashMap<>();
            row1Map.put("Currency History","CurrencyHistory");
            row1Map.put("Bank Settlements","");
            row1Map.put("CC Batch Summary","");
            row1Map.put("FA Commissions","FACommissions");
            row1Map.put("Gross Margins","GrossMargins");
            row1Map.put("Sales Tender Discrepancy","");
            menuItem.setSubMenuImageMap(row1Map);
            menuItem.setMenuName("finance");
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            UI.getCurrent().getNavigator().navigateTo("CommonView");
        });


        CssLayout iconWrapper4 = new CssLayout();
        Button bondReportsImage = new Button("reports");
        iconWrapper4.addComponents(bondReportsImage);
        if(selectedLayout.equalsIgnoreCase("reports")) iconWrapper4.setStyleName("selected");
        else iconWrapper4.setStyleName("iconWrapper-41");
        bondReportsImage.setIcon(FontAwesome.FILE_TEXT_O);


        bondReportsImage.addClickListener(clickEvent -> {
            UI.getCurrent().getSession().setAttribute("selectedLayout","reports");
            SubMenuItem menuItem = new SubMenuItem();
            menuItem.setMenuImage("reports_sub.png");
            Map<String,String> row1Map = new LinkedHashMap<>();
            row1Map.put("Item Sales Summary","MonthlySales");
            row1Map.put("Flight Sales Summary","FlightSales");
            row1Map.put("Category Sales","SaleByCategory");
            row1Map.put("Item Sales by Flight","ItemSales");
            row1Map.put("FA Performance","");
            row1Map.put("Tender Summary","TenderSummary");
            menuItem.setSubMenuImageMap(row1Map);
            menuItem.setMenuName("report");
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            UI.getCurrent().getNavigator().navigateTo("CommonView");

        });


        CssLayout iconWrapper5 = new CssLayout();
        Button inventoryImage = new Button("inventory");
        iconWrapper5.addComponents(inventoryImage);
        if(selectedLayout.equalsIgnoreCase("inventory")) iconWrapper5.setStyleName("selected");
        else iconWrapper5.setStyleName("iconWrapper-41");
        inventoryImage.setIcon(FontAwesome.SHOPPING_BASKET);

        inventoryImage.addClickListener(clickEvent -> {
            UI.getCurrent().getSession().setAttribute("selectedLayout","inventory");
            SubMenuItem menuItem = new SubMenuItem();
            menuItem.setMenuImage("inventory_sub.png");
            Map<String,String> row1Map = new LinkedHashMap<>();
            row1Map.put("Equipment Master","EquipmentMaster");
            row1Map.put("Month End Inventory","MonthEndInventoryView");
            row1Map.put("Unaccounted Carts","");
            row1Map.put("Inventory Valuation","");
            row1Map.put("Sold-out by Flight","SoldOut");
            row1Map.put("Custom Reports","");
            menuItem.setMenuName("inventory");
            menuItem.setSubMenuImageMap(row1Map);
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            UI.getCurrent().getNavigator().navigateTo("CommonView");

        });



        CssLayout iconWrapper6 = new CssLayout();
        Button crmImage = new Button("crm");
        iconWrapper6.addComponents(crmImage);
        if(selectedLayout.equalsIgnoreCase("crm")) iconWrapper6.setStyleName("selected");
        else iconWrapper6.setStyleName("iconWrapper-31");
        crmImage.setIcon(FontAwesome.USERS);

        crmImage.addClickListener(clickEvent -> {
            UI.getCurrent().getSession().setAttribute("selectedLayout","crm");
            SubMenuItem menuItem = new SubMenuItem();
            menuItem.setMenuImage("crm_sub.png");
            Map<String,String> row1Map = new LinkedHashMap<>();
            row1Map.put("Passenger Purchases","PassengerPurchases");
            row1Map.put("Import pax Manifest","");
            row1Map.put("Loading Recommendations","");
            menuItem.setMenuName("crm");
            menuItem.setSubMenuImageMap(row1Map);
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            UI.getCurrent().getNavigator().navigateTo("CommonView");
        });


        CssLayout iconWrapper7 = new CssLayout();
        Button analysisImage = new Button("analyze/uploads");
        iconWrapper7.addComponents(analysisImage);
        if(selectedLayout.equalsIgnoreCase("analyze/uploads")) iconWrapper7.setStyleName("selected");
        else iconWrapper7.setStyleName("iconWrapper-21");
        analysisImage.setIcon(FontAwesome.LINE_CHART);

        analysisImage.addClickListener(clickEvent -> {
            UI.getCurrent().getSession().setAttribute("selectedLayout","analyze/uploads");
            SubMenuItem menuItem = new SubMenuItem();
            menuItem.setMenuImage("analyze_upload_sub.png");
            Map<String,String> row1Map = new LinkedHashMap<>();
            row1Map.put("Wastage","Wastage");
            row1Map.put("SIF Inquiry","");
            row1Map.put("Build Times","BuildTimes");
            row1Map.put("Sales vs Weight","");
            row1Map.put("Airline Uploads","");
            row1Map.put("Custom Reports","");
            menuItem.setMenuName("analyze");
            menuItem.setSubMenuImageMap(row1Map);
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            UI.getCurrent().getNavigator().navigateTo("CommonView");
        });
        CssLayout iconWrapper8 = new CssLayout();
        Button settingsImage = new Button("setup");
        iconWrapper8.addComponents(settingsImage);
        if(selectedLayout.equalsIgnoreCase("setup")) iconWrapper8.setStyleName("selected");
        else iconWrapper8.setStyleName("iconWrapper-11");
        settingsImage.setIcon(FontAwesome.COGS);


        settingsImage.addClickListener(clickEvent -> {
            UI.getCurrent().getSession().setAttribute("selectedLayout","setup");
            SubMenuItem menuItem = new SubMenuItem();
            menuItem.setMenuImage("setup_sub.png");
            Map<String,String> row1Map = new LinkedHashMap<>();
            row1Map.put("Aircraft Type","AircraftType");
            row1Map.put("Flight Details","FlightDetails");
            row1Map.put("Currency","Currency");
            row1Map.put("Create Items","CreateItems");
            row1Map.put("Equipment Type","EquipmentTypes");
            row1Map.put("Assign Items","AssignItems");
            row1Map.put("Create KIT Codes","CreateKitCodes");
            row1Map.put("CC Blacklist","CCBlackList");
            row1Map.put("Promotions","Promotions");
            row1Map.put("Vouchers","Vouchers");
            row1Map.put("FA Commission Table","FACommissionSetup");
            row1Map.put("Budget","Budget");
            menuItem.setMenuName("setup");
            menuItem.setSubMenuImageMap(row1Map);
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            UI.getCurrent().getNavigator().navigateTo("CommonView");
        });


        btnLayout1.addComponents(backBtnLayout,iconWrapper1,iconWrapper2,iconWrapper3,iconWrapper4);
        btnLayout1.setComponentAlignment(backBtnLayout, Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper1, Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper2,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper3,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper4,Alignment.MIDDLE_CENTER);
        btnLayout1.addComponents(iconWrapper5,iconWrapper6,iconWrapper7,iconWrapper8);
        btnLayout1.setComponentAlignment(iconWrapper5, Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper6,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper7,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper8,Alignment.MIDDLE_CENTER);

        return verticalLayout;
    }

    public static String showWithDollarSign(Float val){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        String floatVal = df.format(val);
        if(floatVal.contains(".")){
            String[] arr = floatVal.split("\\.");
            if(arr[1].length() == 1) return "$ " + df.format(val)+"0";
            else return "$ " + df.format(val);
        }
        else{
            return "$ " + df.format(val)+".00";
        }
    }
}

