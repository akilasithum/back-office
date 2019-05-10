package com.back.office.ui.flightKitchen;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.formula.functions.Today;

import com.back.office.db.DBConnection;
import com.back.office.entity.FlightSheduleDetail;
import com.back.office.entity.ItemDetails;
import com.back.office.entity.ItemGross;
import com.back.office.entity.PreOrderDetails;
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
    protected Button ExportToExcel;
    protected Button print;
    protected Grid<FlightSheduleDetail> flightList;
    protected List<FlightSheduleDetail> flightDetList;



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

        Label h1=new Label("Daily Flight");

        h1.addStyleName(ValoTheme.LABEL_H1);
        createLayout.addComponent(h1);

        flightShedul=new Button("Day Flight");
        createLayout.addComponent(flightShedul);
        flightShedul.addClickListener((Button.ClickListener) ClickEvent->
                processList());

        addComponent(createLayout);



        flightList=new Grid();
        createLayout.addComponent(flightList);
        flightList.setVisible(false);

        flightList.addColumn(FlightSheduleDetail::getflightDateTime).setCaption("Flight Date Time");
        flightList.addColumn(FlightSheduleDetail::getflightTime).setCaption("Flight Time");
        flightList.addColumn(FlightSheduleDetail::getaircraftRegistration).setCaption("Aircraft Registration");
        flightList.addColumn(FlightSheduleDetail::getaircraftType).setCaption("Aircraft Type");
        flightList.addColumn(FlightSheduleDetail::getflightNumber).setCaption("Flight Number");
        flightList.addColumn(FlightSheduleDetail::getroot).setCaption("Root");
        flightList.addColumn(FlightSheduleDetail::getservices).setCaption("Services");




    }

    public void processList() {

        flightList.setVisible(true);

        List<FlightSheduleDetail> flightDetailList=connection.getFlightShedule("datefully",new Date());
        flightList.setItems(flightDetailList);


    }
}

