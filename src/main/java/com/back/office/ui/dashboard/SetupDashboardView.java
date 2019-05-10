package com.back.office.ui.dashboard;

import com.back.office.utils.Constants;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class SetupDashboardView  extends SubDashboardCommonView {

    public SetupDashboardView(){
        createMainLayout();
    }

    protected void createMainLayout(){
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        HorizontalLayout btnLayout1 = new HorizontalLayout();
        btnLayout1.setSizeFull();
        HorizontalLayout btnLayout2 = new HorizontalLayout();
        btnLayout2.setSizeFull();
        HorizontalLayout btnLayout3 = new HorizontalLayout();
        btnLayout3.setSizeFull();
        verticalLayout.setStyleName("main-layout");
        btnLayout1.setMargin(Constants.noMargin);
        btnLayout2.setMargin(Constants.noMargin);
        btnLayout3.setMargin(Constants.noMargin);
        verticalLayout.addComponent(btnLayout1);
        verticalLayout.addComponent(btnLayout2);
        verticalLayout.addComponent(btnLayout3);

        Map<String,String> row1Map = new LinkedHashMap<>();
        row1Map.put("Aircraft Type","AircraftType");
        row1Map.put("Flight Details","FlightDetails");
        row1Map.put("Currency","Currency");
        row1Map.put("Create Items","CreateItems");
        addMenuItems(btnLayout1,row1Map,"setup_sub.png");

        Map<String,String> row2Map = new LinkedHashMap<>();
        row2Map.put("Equipment Type","EquipmentTypes");
        row2Map.put("Assign Items","AssignItems");
        row2Map.put("Create KIT Codes","CreateKitCodes");
        row2Map.put("CC Blacklist","CCBlackList");
        addMenuItems(btnLayout2,row2Map,"setup_sub.png");

        Map<String,String> row3Map = new LinkedHashMap<>();
        row3Map.put("Promotions","Promotions");
        row3Map.put("Vouchers","Vouchers");
        row3Map.put("FA Commission Table","error");
        row3Map.put("Budget","Budget");
        addMenuItems(btnLayout3,row3Map,"setup_sub.png");

        MarginInfo info = new MarginInfo(true);
        info.setMargins(true,false,false,false);
        verticalLayout.setMargin(info);
        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }
}
