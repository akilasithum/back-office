package com.back.office.ui.dashboard;

import com.back.office.utils.Constants;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;

import java.util.LinkedHashMap;
import java.util.Map;

public class CRMDashboardView extends SubDashboardCommonView  {

    public CRMDashboardView(){
        createMainLayout();
    }

    protected void createMainLayout(){
        verticalLayout.setSizeFull();
        HorizontalLayout btnLayout1 = new HorizontalLayout();
        btnLayout1.setSizeFull();
        verticalLayout.setStyleName("main-layout");
        btnLayout1.setMargin(Constants.noMargin);
        verticalLayout.addComponent(btnLayout1);

        Map<String,String> row1Map = new LinkedHashMap<>();
        row1Map.put("Passenger Purchases","");
        row1Map.put("Import pax Manifest","");
        row1Map.put("Loading Recommendations","");
        row1Map.put("empty","");
        addMenuItems(btnLayout1,row1Map,"crm_sub.png");

        MarginInfo info = new MarginInfo(true);
        info.setMargins(true,false,false,false);
        verticalLayout.setMargin(info);
        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }
}
