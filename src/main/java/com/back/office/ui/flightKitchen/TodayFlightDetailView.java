package com.back.office.ui.flightKitchen;

import com.back.office.db.DBConnection;
import com.back.office.entity.ArrivalFlight;
import com.back.office.entity.DepartureFlight;
import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;

import java.util.List;

public class TodayFlightDetailView extends UserEntryView implements View {

    DBConnection connection;
    protected TabSheet flightTab;
    Grid<DepartureFlight> departureFlightGrid;
    Grid<ArrivalFlight> arrivalFlightGrid;
    private static final String FLIGHT_TIME = "Time";
    private static final String FLIGHT_NO = "Flight #";
    private static final String AIRLINE = "Airline";
    private static final String DESTINATION = "Destination";
    private static final String FROM = "From";
    private static final String CHECKIN = "Check-in";
    private static final String GATE = "Gate";
    private static final String BELT = "Belt";
    private static final String STATUS = "Status";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public TodayFlightDetailView(){
        super();
        connection = DBConnection.getInstance();
        setMargin(true);
        createMainLayout();
    }

    private void createMainLayout() {

        Label header = new Label("Flight Schedule");
        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        headerLayout.setMargin(Constants.bottomMarginInfo);
        addComponent(headerLayout);
        header.addStyleName("headerText");
        headerLayout.addComponent(header);
        flightTab = new TabSheet();
        flightTab.setSizeFull();
        flightTab.setStyleName("flightDetailsTab");
        addComponent(flightTab);
        flightTab.setWidth("90%");
        VerticalLayout arrivalsLayout = new VerticalLayout();
        arrivalsLayout.setSizeFull();
        arrivalFlightGrid = new Grid<>();
        departureFlightGrid = new Grid<>();
        arrivalFlightGrid.setSizeFull();
        departureFlightGrid.setSizeFull();
        arrivalsLayout.addComponent(arrivalFlightGrid);
        VerticalLayout departureLayout = new VerticalLayout();
        departureLayout.setSizeFull();
        departureLayout.addComponent(departureFlightGrid);
        flightTab.addTab(arrivalsLayout,"Arrivals", FontAwesome.PAPER_PLANE_O);
        flightTab.addTab(departureLayout,"Departures", FontAwesome.PLANE);
        fillFlightData();
    }

    private void fillFlightData(){
        List<DepartureFlight> departureFlightList = (List<DepartureFlight>)connection.getAllValuesNoRecrdStatus("com.back.office.entity.DepartureFlight");
        List<ArrivalFlight> arrivalFlightList = (List<ArrivalFlight>)connection.getAllValuesNoRecrdStatus("com.back.office.entity.ArrivalFlight");

        departureFlightGrid.addColumn(bean-> BackOfficeUtils.getTimeStringFromDate(bean.getFlightTime())).setCaption(FLIGHT_TIME);
        departureFlightGrid.addColumn(DepartureFlight::getFlightNo).setCaption(FLIGHT_NO);
        departureFlightGrid.addColumn(DepartureFlight::getAirline).setCaption(AIRLINE);
        departureFlightGrid.addColumn(DepartureFlight::getDestination).setCaption(DESTINATION);
        departureFlightGrid.addColumn(DepartureFlight::getCheckin).setCaption(CHECKIN);
        departureFlightGrid.addColumn(DepartureFlight::getGate).setCaption(GATE);
        departureFlightGrid.addColumn(DepartureFlight::getStatus).setCaption(STATUS).setStyleGenerator(person -> {
            if (person.getStatus().equalsIgnoreCase("On Time")) {
                return "green-style";
            }
            else if (person.getStatus().contains("Delayed") ) {
                return "orange-style";
            } else {
                return "red-style";
            }
        });

        arrivalFlightGrid.addColumn(bean-> BackOfficeUtils.getTimeStringFromDate(bean.getFlightTime())).setCaption(FLIGHT_TIME);
        arrivalFlightGrid.addColumn(ArrivalFlight::getFlightNo).setCaption(FLIGHT_NO);
        arrivalFlightGrid.addColumn(ArrivalFlight::getAirline).setCaption(AIRLINE);
        arrivalFlightGrid.addColumn(ArrivalFlight::getFrom).setCaption(FROM);
        arrivalFlightGrid.addColumn(ArrivalFlight::getGate).setCaption(GATE);
        arrivalFlightGrid.addColumn(ArrivalFlight::getBelt).setCaption(BELT);
        arrivalFlightGrid.addColumn(ArrivalFlight::getStatus).setCaption(STATUS).setStyleGenerator(person -> {
            if (person.getStatus().equalsIgnoreCase("On Time")) {
                return "green-style";
            }
            else if (person.getStatus().contains("Delayed") ) {
                return "orange-style";
            } else {
                return "red-style";
            }
        });
        departureFlightGrid.setItems(departureFlightList);
        arrivalFlightGrid.setItems(arrivalFlightList);
    }
}
