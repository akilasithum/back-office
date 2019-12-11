package com.back.office.ui.dashboard;

import com.back.office.entity.SubMenuItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainDashboard extends VerticalLayout implements View {

    private static final String imageWidth = "35%";

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
        btnLayout2.setHeight("260px");
        verticalLayout.setStyleName("main-layout");
//        btnLayout1.setMargin(Constants.bottomMarginInfo);
        verticalLayout.addComponent(btnLayout1);
        verticalLayout.addComponent(btnLayout2);



        CssLayout iconWrapper1 = new CssLayout();
        Button flightKitchenImage = new Button("Flight Kitchen");
        iconWrapper1.addComponents(flightKitchenImage);
        iconWrapper1.setStyleName("iconWrapper-1");
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
            getUI().getNavigator().navigateTo("CommonView");
        });


        CssLayout iconWrapper2 = new CssLayout();
        Button preOrderImage = new Button("pre order");
        iconWrapper2.addComponents(preOrderImage );
        iconWrapper2.setStyleName("iconWrapper-2");
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
            menuItem.setSubMenuImageMap(row1Map);
            menuItem.setMenuName("pre_order");
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            getUI().getNavigator().navigateTo("CommonView");
        });



        CssLayout iconWrapper3 = new CssLayout();
        Button financeImage = new Button("finance");
        iconWrapper3.addComponents(financeImage);
        iconWrapper3.setStyleName("iconWrapper-3");
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
            getUI().getNavigator().navigateTo("CommonView");
        });


        CssLayout iconWrapper4 = new CssLayout();
        Button bondReportsImage = new Button("reports");
        iconWrapper4.addComponents(bondReportsImage);
        iconWrapper4.setStyleName("iconWrapper-4");
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
            getUI().getNavigator().navigateTo("CommonView");

        });


        CssLayout iconWrapper5 = new CssLayout();
        Button inventoryImage = new Button("inventory");
        iconWrapper5.addComponents(inventoryImage);
        iconWrapper5.setStyleName("iconWrapper-4");
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
            getUI().getNavigator().navigateTo("CommonView");

        });



        CssLayout iconWrapper6 = new CssLayout();
        Button crmImage = new Button("crm");
        iconWrapper6.addComponents(crmImage);
        iconWrapper6.setStyleName("iconWrapper-3");
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
            getUI().getNavigator().navigateTo("CommonView");
        });


        CssLayout iconWrapper7 = new CssLayout();
        Button analysisImage = new Button("analyze upload");
        iconWrapper7.addComponents(analysisImage);
        iconWrapper7.setStyleName("iconWrapper-2");
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
            getUI().getNavigator().navigateTo("CommonView");
        });
        CssLayout iconWrapper8 = new CssLayout();
        Button settingsImage = new Button("setup");
        iconWrapper8.addComponents(settingsImage);

        iconWrapper8.setStyleName("iconWrapper-1");
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
            getUI().getNavigator().navigateTo("CommonView");
        });


        btnLayout1.addComponents(iconWrapper1,iconWrapper2,iconWrapper3,iconWrapper4);
        btnLayout1.setComponentAlignment(iconWrapper1, Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper2,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper3,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper4,Alignment.MIDDLE_CENTER);

        btnLayout2.addComponents(iconWrapper5,iconWrapper6,iconWrapper7,iconWrapper8);
        btnLayout2.setComponentAlignment(iconWrapper5, Alignment.MIDDLE_CENTER);
        btnLayout2.setComponentAlignment(iconWrapper6,Alignment.MIDDLE_CENTER);
        btnLayout2.setComponentAlignment(iconWrapper7,Alignment.MIDDLE_CENTER);
        btnLayout2.setComponentAlignment(iconWrapper8,Alignment.MIDDLE_CENTER);

//        MarginInfo info = new MarginInfo(true);
//        info.setMargins(true,false,false,false);
//        verticalLayout.setMargin(info);
        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }
}
