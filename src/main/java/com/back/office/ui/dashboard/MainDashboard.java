package com.back.office.ui.dashboard;

import com.back.office.entity.SubMenuItem;
import com.back.office.utils.Constants;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainDashboard extends VerticalLayout implements View {

    private static final String imageWidth = "75%";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public MainDashboard(){
        createMainLayout();
    }

    private void createMainLayout(){
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        HorizontalLayout btnLayout1 = new HorizontalLayout();
        btnLayout1.setSizeFull();
        HorizontalLayout btnLayout2 = new HorizontalLayout();
        btnLayout2.setSizeFull();
        verticalLayout.setStyleName("main-layout");
        btnLayout1.setMargin(Constants.bottomMarginInfo);
        verticalLayout.addComponent(btnLayout1);
        verticalLayout.addComponent(btnLayout2);

        Image flightKitchenImage = new Image(null, new ClassResource("flight_kitchen.png"));
        flightKitchenImage.setWidth(imageWidth);
        flightKitchenImage.setHeight(imageWidth);
        flightKitchenImage.addStyleName("my-img-button");
        flightKitchenImage.addClickListener(clickEvent -> {
            SubMenuItem menuItem = new SubMenuItem();
            menuItem.setMenuImage("crm_sub.png");
            Map<String,String> row1Map = new LinkedHashMap<>();
            row1Map.put("Passenger Purchases","PassengerPurchases");
            row1Map.put("Import pax Manifest","");
            row1Map.put("Loading Recommendations","");
            row1Map.put("SIF","SIFDetails");
            row1Map.put("HHC and Cart Usage","HHCAndCartUsage");
            menuItem.setMenuName("flight_kitchen");
            menuItem.setSubMenuImageMap(row1Map);
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            getUI().getNavigator().navigateTo("CommonView");
        });

        Image preOrderImage = new Image(null, new ClassResource("pre_order.png"));
        preOrderImage.setWidth(imageWidth);
        preOrderImage.setHeight(imageWidth);
        preOrderImage.addStyleName("my-img-button");
        preOrderImage.addClickListener(clickEvent -> {

            SubMenuItem menuItem = new SubMenuItem();
            menuItem.setMenuImage("pre_order_sub.png");
            Map<String,String> row1Map = new LinkedHashMap<>();
            row1Map.put("Pre-order","PreOrders");
            row1Map.put("Pre-order Inventory","");
            row1Map.put("Inflight Requests","MessagingModule");
            row1Map.put("Bond Messages","BondMessages");
            row1Map.put("FA Messages","FAMessages");
            menuItem.setSubMenuImageMap(row1Map);
            menuItem.setMenuName("pre_order");
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            getUI().getNavigator().navigateTo("CommonView");
        });

        Image financeImage = new Image(null, new ClassResource("finance.png"));
        financeImage.setWidth(imageWidth);
        financeImage.setHeight(imageWidth);
        financeImage.addStyleName("my-img-button");
        financeImage.addClickListener(clickEvent -> {

            SubMenuItem menuItem = new SubMenuItem();
            menuItem.setMenuImage("finance_sub.png");
            Map<String,String> row1Map = new LinkedHashMap<>();
            row1Map.put("Currency History","CurrencyHistory");
            row1Map.put("Bank Settlements","");
            row1Map.put("CC Batch Summary","");
            row1Map.put("FA Commissions","");
            row1Map.put("Gross Margins","GrossMargins");
            row1Map.put("Sales Tender Discrepancy","");
            menuItem.setSubMenuImageMap(row1Map);
            menuItem.setMenuName("finance");
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            getUI().getNavigator().navigateTo("CommonView");
        });

        Image bondReportsImage = new Image(null, new ClassResource("reports.png"));
        bondReportsImage.setWidth(imageWidth);
        bondReportsImage.setHeight(imageWidth);
        bondReportsImage.addStyleName("my-img-button");
        bondReportsImage.addClickListener(clickEvent -> {

            SubMenuItem menuItem = new SubMenuItem();
            menuItem.setMenuImage("reports_sub.png");
            Map<String,String> row1Map = new LinkedHashMap<>();
            row1Map.put("Monthly Sales","MonthlySales");
            row1Map.put("Flight Sales","FlightSales");
            row1Map.put("Category/Sector Sales","");
            row1Map.put("Item Sales","ItemSales");
            row1Map.put("FA Performance","");
            row1Map.put("Tender Summary","");
            menuItem.setSubMenuImageMap(row1Map);
            menuItem.setMenuName("report");
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            getUI().getNavigator().navigateTo("CommonView");

        });

        Image inventoryImage = new Image(null, new ClassResource("inventory.png"));
        inventoryImage.setWidth(imageWidth);
        inventoryImage.setHeight(imageWidth);
        inventoryImage.addStyleName("my-img-button");
        inventoryImage.addClickListener(clickEvent -> {

            SubMenuItem menuItem = new SubMenuItem();
            menuItem.setMenuImage("inventory_sub.png");
            Map<String,String> row1Map = new LinkedHashMap<>();
            row1Map.put("Equipment Master","EquipmentMaster");
            row1Map.put("Month End  Inventory","");
            row1Map.put("Unaccounted Carts","");
            row1Map.put("Inventory Valuation","");
            row1Map.put("Sold-out by Flight","SoldOut");
            row1Map.put("Custom Reports","");
            menuItem.setMenuName("inventory");
            menuItem.setSubMenuImageMap(row1Map);
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            getUI().getNavigator().navigateTo("CommonView");

        });

        Image crmImage = new Image(null, new ClassResource("crm.png"));
        crmImage.setWidth(imageWidth);
        crmImage.setHeight(imageWidth);
        crmImage.addStyleName("my-img-button");
        crmImage.addClickListener(clickEvent -> {
            SubMenuItem menuItem = new SubMenuItem();
            menuItem.setMenuImage("crm_sub.png");
            Map<String,String> row1Map = new LinkedHashMap<>();
            row1Map.put("Passenger Purchases","PassengerPurchases");
            row1Map.put("Import pax Manifest","");
            row1Map.put("Loading Recommendations","");
            menuItem.setMenuName("crm");
            menuItem.setSubMenuImageMap(row1Map);
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            getUI().getNavigator().navigateTo("CommonView");
        });

        Image analysisImage = new Image(null, new ClassResource("analyze_upload.png"));
        analysisImage.setWidth(imageWidth);
        analysisImage.setHeight(imageWidth);
        analysisImage.addStyleName("my-img-button");
        analysisImage.addClickListener(clickEvent -> {
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
            getUI().getNavigator().navigateTo("CommonView");
        });

        Image settingsImage = new Image(null, new ClassResource("setup.png"));
        settingsImage.setWidth(imageWidth);
        settingsImage.setHeight(imageWidth);
        settingsImage.addStyleName("my-img-button");

        settingsImage.addClickListener(clickEvent -> {
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
            row1Map.put("FA Commission Table","error");
            row1Map.put("Budget","Budget");
            menuItem.setMenuName("setup");
            menuItem.setSubMenuImageMap(row1Map);
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            getUI().getNavigator().navigateTo("CommonView");
        });


        btnLayout1.addComponents(flightKitchenImage,preOrderImage,financeImage,bondReportsImage);
        btnLayout1.setComponentAlignment(flightKitchenImage, Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(preOrderImage,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(financeImage,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(bondReportsImage,Alignment.MIDDLE_CENTER);

        btnLayout2.addComponents(inventoryImage,crmImage,analysisImage,settingsImage);
        btnLayout2.setComponentAlignment(inventoryImage, Alignment.MIDDLE_CENTER);
        btnLayout2.setComponentAlignment(crmImage,Alignment.MIDDLE_CENTER);
        btnLayout2.setComponentAlignment(analysisImage,Alignment.MIDDLE_CENTER);
        btnLayout2.setComponentAlignment(settingsImage,Alignment.MIDDLE_CENTER);

        MarginInfo info = new MarginInfo(true);
        info.setMargins(true,false,false,false);
        verticalLayout.setMargin(info);
        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }
}
