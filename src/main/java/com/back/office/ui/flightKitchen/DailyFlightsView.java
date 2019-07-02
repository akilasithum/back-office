package com.back.office.ui.flightKitchen;

import java.util.Date;
import java.util.List;


import com.back.office.db.DBConnection;
import com.back.office.entity.FlightSheduleDetail;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class DailyFlightsView extends VerticalLayout implements View{

    protected Button flightShedul;
    protected VerticalLayout createLayout;
    protected DBConnection connection;
    protected Grid<FlightSheduleDetail> dailyFlightsGrid;



    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }


    public DailyFlightsView() {
        createMainLayout();
        connection=DBConnection.getInstance();

    }

    public void createMainLayout() {

        createLayout=new VerticalLayout();
        createLayout.setMargin(Constants.leftBottomtMargin);

        Label h1=new Label("Daily Flights");

        h1.addStyleName(ValoTheme.LABEL_H1);
        createLayout.addComponent(h1);

        flightShedul=new Button("Show Daily Flights");
        createLayout.addComponent(flightShedul);
        flightShedul.addClickListener((Button.ClickListener) ClickEvent->
                processList());

        addComponent(createLayout);
        setStyleName("backColorGrey");
        setMargin(Constants.noMargin);


        dailyFlightsGrid =new Grid();
        dailyFlightsGrid.setSizeFull();
        dailyFlightsGrid.setWidth("60%");
        createLayout.addComponent(dailyFlightsGrid);

        dailyFlightsGrid.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getflightDateTime())).setCaption("Date");
        dailyFlightsGrid.addColumn(FlightSheduleDetail::getflightTime).setCaption("Time");
        dailyFlightsGrid.addColumn(FlightSheduleDetail::getaircraftRegistration).setCaption("ACFT Reg");
        dailyFlightsGrid.addColumn(FlightSheduleDetail::getaircraftType).setCaption("Type");
        dailyFlightsGrid.addColumn(FlightSheduleDetail::getflightNumber).setCaption("Flight Number");
        dailyFlightsGrid.addColumn(FlightSheduleDetail::getFrom).setCaption("From");
        dailyFlightsGrid.addColumn(FlightSheduleDetail::getservices).setCaption("Services");




    }

    public void processList() {

        String baseStation = UI.getCurrent().getSession().getAttribute("baseStation").toString();
        List<FlightSheduleDetail> flightDetailList=connection.getFlightShedule(new Date(),new Date(),baseStation);
        dailyFlightsGrid.setItems(flightDetailList);
    }
}
