package com.back.office.ui.dashboard;

import com.back.office.utils.Constants;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

public class SetupDashboardView  extends VerticalLayout implements View {

    private static final String imageWidth = "50%";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public SetupDashboardView(){
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

        Image authorizationImage = new Image(null, new ClassResource("sky_image.png"));
        authorizationImage.setWidth(imageWidth);
        authorizationImage.setHeight(imageWidth);
        authorizationImage.addStyleName("my-img-button");

        authorizationImage.addClickListener(clickEvent -> {
            getUI().getNavigator().navigateTo("AircraftType");
        });

        Image setupImage = new Image(null, new ClassResource("ground_image.png"));
        setupImage.setWidth(imageWidth);
        setupImage.setHeight(imageWidth);
        setupImage.addStyleName("my-img-button");

        Image fileTransferImage = new Image(null, new ClassResource("galley_image.png"));
        fileTransferImage.setWidth(imageWidth);
        fileTransferImage.setHeight(imageWidth);
        fileTransferImage.addStyleName("my-img-button");

        Image bondReportsImage = new Image(null, new ClassResource("galley_image.png"));
        bondReportsImage.setWidth(imageWidth);
        bondReportsImage.setHeight(imageWidth);
        bondReportsImage.addStyleName("my-img-button");

        Image salesReportImage = new Image(null, new ClassResource("sky_image.png"));
        salesReportImage.setWidth(imageWidth);
        salesReportImage.setHeight(imageWidth);
        salesReportImage.addStyleName("my-img-button");

        /*authorizationImage.addClickListener(clickEvent -> {

        });*/

        Image analysisImage = new Image(null, new ClassResource("ground_image.png"));
        analysisImage.setWidth(imageWidth);
        analysisImage.setHeight(imageWidth);
        analysisImage.addStyleName("my-img-button");

        Image preOrderMgtImage = new Image(null, new ClassResource("galley_image.png"));
        preOrderMgtImage.setWidth(imageWidth);
        preOrderMgtImage.setHeight(imageWidth);
        preOrderMgtImage.addStyleName("my-img-button");

        Image settingsImage = new Image(null, new ClassResource("galley_image.png"));
        settingsImage.setWidth(imageWidth);
        settingsImage.setHeight(imageWidth);
        settingsImage.addStyleName("my-img-button");


        btnLayout1.addComponents(authorizationImage,setupImage,fileTransferImage,bondReportsImage);
        btnLayout1.setComponentAlignment(authorizationImage, Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(setupImage,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(fileTransferImage,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(bondReportsImage,Alignment.MIDDLE_CENTER);

        btnLayout2.addComponents(salesReportImage,analysisImage,preOrderMgtImage,settingsImage);
        btnLayout2.setComponentAlignment(salesReportImage, Alignment.MIDDLE_CENTER);
        btnLayout2.setComponentAlignment(analysisImage,Alignment.MIDDLE_CENTER);
        btnLayout2.setComponentAlignment(preOrderMgtImage,Alignment.MIDDLE_CENTER);
        btnLayout2.setComponentAlignment(settingsImage,Alignment.MIDDLE_CENTER);

        MarginInfo info = new MarginInfo(true);
        info.setMargins(true,false,false,false);
        verticalLayout.setMargin(info);
        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }
}
