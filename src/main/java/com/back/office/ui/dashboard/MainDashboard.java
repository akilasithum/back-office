package com.back.office.ui.dashboard;

import com.back.office.entity.SubMenuItem;
import com.back.office.utils.BackOfficeUtils;
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
        verticalLayout.addComponent(btnLayout1);
        verticalLayout.addComponent(btnLayout2);

        CssLayout iconWrapper1 =  BackOfficeUtils.getMainLayoutBtn("flight Schedule");
        iconWrapper1.setStyleName("iconWrapper-1");
        CssLayout iconWrapper2 =  BackOfficeUtils.getMainLayoutBtn("pre order");
        iconWrapper2.setStyleName("iconWrapper-2");
        CssLayout iconWrapper3 =  BackOfficeUtils.getMainLayoutBtn("Reports and Finance");
        iconWrapper3.setStyleName("iconWrapper-3");
        CssLayout iconWrapper4 =  BackOfficeUtils.getMainLayoutBtn("Airline Specific");
        iconWrapper4.setStyleName("iconWrapper-4");
        CssLayout iconWrapper5 = BackOfficeUtils.getMainLayoutBtn("Vendor");
        iconWrapper5.setStyleName("iconWrapper-4");
        CssLayout iconWrapper6 = BackOfficeUtils.getMainLayoutBtn("crm");
        iconWrapper6.setStyleName("iconWrapper-3");
        CssLayout iconWrapper7 = BackOfficeUtils.getMainLayoutBtn("Bag Trace");
        iconWrapper7.setStyleName("iconWrapper-2");
        CssLayout iconWrapper8 = BackOfficeUtils.getMainLayoutBtn("setup");
        iconWrapper8.setStyleName("iconWrapper-1");

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

        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }
}
