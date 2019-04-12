package com.back.office.ui.dashboard;

import com.back.office.utils.Constants;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;

import java.util.LinkedHashMap;
import java.util.Map;

public class ReportsDashboardView extends SubDashboardCommonView {

    public ReportsDashboardView(){
        createMainLayout();
    }

    protected void createMainLayout(){
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
        row1Map.put("Monthly Sales","");
        row1Map.put("Flight Sales","");
        row1Map.put("Category/Sector Sales","");
        row1Map.put("Item Sales","");
        addMenuItems(btnLayout1,row1Map,"reports_sub.png");

        Map<String,String> row2Map = new LinkedHashMap<>();
        row2Map.put("FA Performance","");
        row2Map.put("Tender Summary","");
        row2Map.put("empty","");
        row2Map.put("empty1","");
        addMenuItems(btnLayout2,row2Map,"reports_sub.png");

        MarginInfo info = new MarginInfo(true);
        info.setMargins(true,false,false,false);
        verticalLayout.setMargin(info);
        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }
}
