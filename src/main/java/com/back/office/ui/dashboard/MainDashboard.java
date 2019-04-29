package com.back.office.ui.dashboard;

import com.back.office.utils.Constants;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

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
            getUI().getNavigator().navigateTo("flightKitchen");
        });

        Image preOrderImage = new Image(null, new ClassResource("pre_order.png"));
        preOrderImage.setWidth(imageWidth);
        preOrderImage.setHeight(imageWidth);
        preOrderImage.addStyleName("my-img-button");
        preOrderImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("preOrderMessenger");
        });

        Image financeImage = new Image(null, new ClassResource("finance.png"));
        financeImage.setWidth(imageWidth);
        financeImage.setHeight(imageWidth);
        financeImage.addStyleName("my-img-button");
        financeImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("finance");
        });

        Image bondReportsImage = new Image(null, new ClassResource("reports.png"));
        bondReportsImage.setWidth(imageWidth);
        bondReportsImage.setHeight(imageWidth);
        bondReportsImage.addStyleName("my-img-button");
        bondReportsImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("reports");
        });

        Image inventoryImage = new Image(null, new ClassResource("inventory.png"));
        inventoryImage.setWidth(imageWidth);
        inventoryImage.setHeight(imageWidth);
        inventoryImage.addStyleName("my-img-button");
        inventoryImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("inventory");
        });

        Image crmImage = new Image(null, new ClassResource("crm.png"));
        crmImage.setWidth(imageWidth);
        crmImage.setHeight(imageWidth);
        crmImage.addStyleName("my-img-button");
        crmImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("crm");
        });

        Image analysisImage = new Image(null, new ClassResource("analyze_upload.png"));
        analysisImage.setWidth(imageWidth);
        analysisImage.setHeight(imageWidth);
        analysisImage.addStyleName("my-img-button");
        analysisImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("analyze");
        });

        Image settingsImage = new Image(null, new ClassResource("setup.png"));
        settingsImage.setWidth(imageWidth);
        settingsImage.setHeight(imageWidth);
        settingsImage.addStyleName("my-img-button");

        settingsImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("setup");
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
