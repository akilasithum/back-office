package com.back.office.ui.dashboard;

import com.back.office.entity.SubMenuItem;
import com.back.office.utils.Constants;
import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommonSelection extends VerticalLayout implements View {

    VerticalLayout mainLayout = new VerticalLayout();

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public CommonSelection()
    {
        setMargin(Constants.noTopMargin);
        setSpacing(false);
        String selected = (String)UI.getCurrent().getSession().getAttribute("selectedLayout");
        createIconLayout(selected);
        createLayout();
    }

    private void createLayout(){
        addComponent(mainLayout);
        mainLayout.setStyleName("main-layout");
        mainLayout.setSizeFull();
        //mainLayout.setWidth("80%");
        MarginInfo info = new MarginInfo(true);
        info.setMargins(true,false,false,false);
        mainLayout.setMargin(info);
        setComponentAlignment(mainLayout, Alignment.MIDDLE_CENTER);
        SubMenuItem menuItem = (SubMenuItem)UI.getCurrent().getSession().getAttribute("subMenu");
        Map<String,String> subMenuMap = menuItem.getSubMenuImageMap();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Map<String,String> subMap = new LinkedHashMap<>();
        int i = 0;
        for(Map.Entry<String,String> map : subMenuMap.entrySet()){
            if(i%4 == 0){
                if(i != 0){
                    horizontalLayout.setSizeFull();
                    addMenuItems(horizontalLayout,subMap,menuItem.getMenuImage());
                    mainLayout.addComponent(horizontalLayout);
                }
                horizontalLayout = new HorizontalLayout();
                subMap = new LinkedHashMap<>();
                subMap.put(map.getKey(),map.getValue());
            }
            else{
                subMap.put(map.getKey(),map.getValue());

            }
            if(subMenuMap.size() == i+1){
                horizontalLayout.setSizeFull();
                if(subMap.size() != 4) {
                    int count = 4-subMap.size();
                    for (int j = 0; j < count; j++) {
                        subMap.put("empty" + j, "");
                    }
                }
                addMenuItems(horizontalLayout,subMap,menuItem.getMenuImage());
                mainLayout.addComponent(horizontalLayout);
            }
            i++;

        }
    }

    private void createIconLayout(String selectedLayout){
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setWidth("80%");
        HorizontalLayout btnLayout1 = new HorizontalLayout();
        btnLayout1.setSizeFull();
        verticalLayout.setStyleName("iconLayoutBackground");
        //btnLayout1.setMargin(Constants.bottomMarginInfo);
        verticalLayout.setMargin(Constants.noMargin);
        verticalLayout.addComponent(btnLayout1);

        CssLayout iconWrapper1 = new CssLayout();
        Button flightKitchenImage = new Button("Flight Kitchen");
        iconWrapper1.addComponents(flightKitchenImage);
        if(selectedLayout.equalsIgnoreCase("Flight Kitchen")) iconWrapper1.setStyleName("selected");
        else iconWrapper1.setStyleName("iconWrapper-11");
        flightKitchenImage.setIcon(FontAwesome.PLANE);
        flightKitchenImage.addClickListener(clickEvent -> {
            UI.getCurrent().getSession().setAttribute("selectedLayout","business intelligence");
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
            getUI().getNavigator().navigateTo("CommonView");
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
            getUI().getNavigator().navigateTo("CommonView");
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
            getUI().getNavigator().navigateTo("CommonView");

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
            getUI().getNavigator().navigateTo("CommonView");

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
            getUI().getNavigator().navigateTo("CommonView");
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
            getUI().getNavigator().navigateTo("CommonView");
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
            getUI().getNavigator().navigateTo("CommonView");
        });


        btnLayout1.addComponents(iconWrapper1,iconWrapper2,iconWrapper3,iconWrapper4);
        btnLayout1.setComponentAlignment(iconWrapper1, Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper2,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper3,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper4,Alignment.MIDDLE_CENTER);
        btnLayout1.addComponents(iconWrapper5,iconWrapper6,iconWrapper7,iconWrapper8);
        btnLayout1.setComponentAlignment(iconWrapper5, Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper6,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper7,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper8,Alignment.MIDDLE_CENTER);

        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }

    protected void addMenuItems(HorizontalLayout horizontalLayout, Map<String,String> iconNameNavigatorMap, String resourceName){
        int i = 1;
        for(Map.Entry<String,String>  map : iconNameNavigatorMap.entrySet()){
            if(map.getKey().contains("empty")){
                VerticalLayout emptyLayout = new VerticalLayout();
                horizontalLayout.addComponents(emptyLayout);
            }
            else {
                resourceName = map.getKey().replace(" ","-")+".svg";
                if(i == 5) {
                    i = 1;
                }
                String styleName = "iconWrapper-" + i;
                i++;
                CssLayout aircraftTypeLayout = new CssLayout();
                Image authorizationImage = new Image(null, new ClassResource(resourceName));
                Button inventoryImage = new Button(map.getKey());
                aircraftTypeLayout.addComponents(inventoryImage, authorizationImage);
                aircraftTypeLayout.setStyleName(styleName);
                inventoryImage.addClickListener(layoutClickEvent ->
                        getUI().getNavigator().navigateTo(map.getValue())
                );
                horizontalLayout.addComponent(aircraftTypeLayout);
                horizontalLayout.setComponentAlignment(aircraftTypeLayout, Alignment.MIDDLE_CENTER);
            }
        }
    }
}
