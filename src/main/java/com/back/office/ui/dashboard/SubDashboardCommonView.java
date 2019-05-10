package com.back.office.ui.dashboard;

import com.back.office.utils.Constants;
import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import java.util.Map;

public class SubDashboardCommonView extends VerticalLayout implements View {

    private static final String imageWidth = "25%";
    VerticalLayout verticalLayout = new VerticalLayout();
    HorizontalLayout mainLayout = new HorizontalLayout();
    VerticalLayout buttonLayout = new VerticalLayout();

    /*public SubDashboardCommonView(){
        addComponent(mainLayout);
        //mainLayout.addComponents(buttonLayout);
       // mainLayout.setExpandRatio(buttonLayout,0.2f);
        //buttonLayout.setMargin(Constants.noMargin);
        mainLayout.setMargin(Constants.noMargin);
        //createButtonLayout();
    }*/

    private void createButtonLayout(){
        Image flightKitchenImage = new Image(null, new ClassResource("flight_kitchen.png"));
        flightKitchenImage.setWidth(imageWidth);
        flightKitchenImage.setHeight(imageWidth);
        flightKitchenImage.addStyleName("icon-image");
        flightKitchenImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("flightKitchen");
        });


        Image preOrderImage = new Image(null, new ClassResource("pre_order.png"));
        preOrderImage.setWidth(imageWidth);
        preOrderImage.setHeight(imageWidth);
        preOrderImage.addStyleName("icon-image");
        preOrderImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("preOrderMessenger");
        });

        Image financeImage = new Image(null, new ClassResource("finance.png"));
        financeImage.setWidth(imageWidth);
        financeImage.setHeight(imageWidth);
        financeImage.addStyleName("icon-image");
        financeImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("finance");
        });

        Image bondReportsImage = new Image(null, new ClassResource("reports.png"));
        bondReportsImage.setWidth(imageWidth);
        bondReportsImage.setHeight(imageWidth);
        bondReportsImage.addStyleName("icon-image");
        bondReportsImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("reports");
        });

        Image inventoryImage = new Image(null, new ClassResource("inventory.png"));
        inventoryImage.setWidth(imageWidth);
        inventoryImage.setHeight(imageWidth);
        inventoryImage.addStyleName("icon-image");
        inventoryImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("inventory");
        });

        Image crmImage = new Image(null, new ClassResource("crm.png"));
        crmImage.setWidth(imageWidth);
        crmImage.setHeight(imageWidth);
        crmImage.addStyleName("icon-image");
        crmImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("crm");
        });

        Image analysisImage = new Image(null, new ClassResource("analyze_upload.png"));
        analysisImage.setWidth(imageWidth);
        analysisImage.setHeight(imageWidth);
        analysisImage.addStyleName("icon-image");
        analysisImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("analyze");
        });

        Image settingsImage = new Image(null, new ClassResource("setup.png"));
        settingsImage.setWidth(imageWidth);
        settingsImage.setHeight(imageWidth);
        settingsImage.addStyleName("icon-image");

        settingsImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("setup");
        });

        buttonLayout.addComponents(flightKitchenImage,preOrderImage,financeImage,bondReportsImage,inventoryImage,crmImage,
                analysisImage,settingsImage);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    protected void addMenuItems(HorizontalLayout horizontalLayout, Map<String,String> iconNameNavigatorMap,String resourceName){
        for(Map.Entry<String,String>  map : iconNameNavigatorMap.entrySet()){

            if(map.getKey().contains("empty")){
                VerticalLayout emptyLayout = new VerticalLayout();
                horizontalLayout.addComponents(emptyLayout);
            }
            else {
                Image authorizationImage = new Image(null, new ClassResource(resourceName));
                authorizationImage.setWidth("50%");
                authorizationImage.setHeight("50%");
                VerticalLayout aircraftTypeLayout = new VerticalLayout();
                aircraftTypeLayout.setMargin(Constants.noMargin);
                aircraftTypeLayout.addComponent(authorizationImage);
                Label label = new Label(map.getKey());
                label.setStyleName("itemLabel");
                aircraftTypeLayout.addComponent(label);
                aircraftTypeLayout.addStyleName("my-img-button");
                aircraftTypeLayout.setComponentAlignment(authorizationImage, Alignment.MIDDLE_CENTER);
                aircraftTypeLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
                aircraftTypeLayout.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent ->
                        getUI().getNavigator().navigateTo(map.getValue())
                );
                horizontalLayout.addComponent(aircraftTypeLayout);
                horizontalLayout.setComponentAlignment(aircraftTypeLayout, Alignment.MIDDLE_CENTER);
            }
        }
    }
}
