package com.back.office.ui.dashboard;

import com.back.office.utils.Constants;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import java.util.LinkedHashMap;
import java.util.Map;

public class FlightKitchenDashboardView extends SubDashboardCommonView {

    public FlightKitchenDashboardView(){
        createMainLayout();
    }

    protected void createMainLayout(){
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        HorizontalLayout btnLayout1 = new HorizontalLayout();
        btnLayout1.setSizeFull();
        HorizontalLayout btnLayout2 = new HorizontalLayout();
        btnLayout2.setSizeFull();
        verticalLayout.setStyleName("main-layout");
        btnLayout1.setMargin(Constants.noMargin);
        btnLayout2.setMargin(Constants.noMargin);
        verticalLayout.addComponent(btnLayout1);
        verticalLayout.addComponent(btnLayout2);

        Map<String,String> row1Map = new LinkedHashMap<>();
        row1Map.put("Flight Schedule","");
        row1Map.put("Daily Flights","");
        row1Map.put("Request Inventory","");
        row1Map.put("Galley Weights","");
        addMenuItems(btnLayout1,row1Map,"flight_kitchen_sub.png");

        Map<String,String> row2Map = new LinkedHashMap<>();
        row2Map.put("SIF","");
        row2Map.put("HHC and Cart Usage","");
        row2Map.put("empty","");
        row2Map.put("empty1","");
        addMenuItems(btnLayout2,row2Map,"flight_kitchen_sub.png");

        MarginInfo info = new MarginInfo(true);
        info.setMargins(true,false,false,false);
        verticalLayout.setMargin(info);
        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }
}
