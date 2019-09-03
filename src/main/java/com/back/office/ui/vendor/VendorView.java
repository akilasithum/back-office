package com.back.office.ui.vendor;

import com.back.office.db.DBConnection;
import com.back.office.entity.Vendor;
import com.back.office.framework.UserEntryView;
import com.back.office.utils.Constants;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class VendorView extends UserEntryView implements View {

    DBConnection connection;
    protected TabSheet flightTab;
    Grid<Vendor> issuedGrid;
    Grid<Vendor> redeemedGrid;
    private static final String PAX_NAME = "Pax Name";
    private static final String FLIGHT_NO = "Flight #";
    private static final String PNR = "PNR";
    private static final String ROUTE = "Route";
    private static final String TYPE = "Type";
    private static final String CATEGORY = "Category";
    private static final String VOUCHER_NO = "Voucher No";
    private static final String DESCRIPTION = "Description";
    private static final String STATUS = "Status";
    protected DateField flightDateFromField;
    protected DateField flightDateToField;
    private ComboBox categoryCombo;
    Button searchButton;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public VendorView(){
        super();
        connection = DBConnection.getInstance();
        setMargin(true);
        createMainLayout();
    }

    private void createMainLayout() {

        Label header = new Label("Flight Schedule");
        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        headerLayout.setMargin(Constants.noMargin);
        addComponent(headerLayout);
        header.addStyleName("headerText");
        headerLayout.addComponent(header);

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName("report-filter-panel");
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setWidth("60%");
        firstRow.setMargin(Constants.bottomMarginInfo);

        Date date = new Date();
        LocalDate today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        flightDateFromField = new DateField("Flight Date From");
        flightDateFromField.setValue(today);
        flightDateFromField.setSizeFull();
        firstRow.addComponent(flightDateFromField);

        flightDateToField = new DateField("Flight Date To");
        flightDateToField.setValue(today);
        flightDateToField.setSizeFull();
        firstRow.addComponent(flightDateToField);

        categoryCombo = new ComboBox("Category");
        categoryCombo.setSizeFull();

        searchButton = new Button("Search");
        firstRow.addComponent(categoryCombo);
        //searchButton.addClickListener((Button.ClickListener) clickEvent -> showFilterData());

        HorizontalLayout buttonRow = new HorizontalLayout();
        buttonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        buttonRow.setStyleName("searchButton");
        buttonRow.addComponent(searchButton);
        firstRow.addComponent(buttonRow);
        addComponent(firstRow);

        flightTab = new TabSheet();
        flightTab.setSizeFull();
        flightTab.setStyleName("flightDetailsTab");
        flightTab.setStyleName("tabsheetStyle");
        addComponent(flightTab);
        flightTab.setWidth("90%");
        VerticalLayout arrivalsLayout = new VerticalLayout();
        arrivalsLayout.setSizeFull();
        redeemedGrid = new Grid<>();
        issuedGrid = new Grid<>();
        redeemedGrid.setSizeFull();
        issuedGrid.setSizeFull();
        arrivalsLayout.addComponent(redeemedGrid);
        VerticalLayout departureLayout = new VerticalLayout();
        departureLayout.setSizeFull();
        departureLayout.addComponent(issuedGrid);
        flightTab.addTab(arrivalsLayout,"Issued", FontAwesome.CALENDAR_CHECK_O);
        flightTab.addTab(departureLayout,"Redeemed", FontAwesome.CHECK_CIRCLE_O);
        fillFlightData();
    }

    private void fillFlightData(){
        List<Vendor> issuedVendorList = (List<Vendor>)connection.getAllValuesNoRecordStatus("com.back.office.entity.Vendor");
        List<Vendor> redeemedVendorList = (List<Vendor>)connection.getAllValuesNoRecordStatus("com.back.office.entity.Vendor");

        issuedGrid.addColumn(Vendor::getPaxName).setCaption(PAX_NAME);
        issuedGrid.addColumn(Vendor::getPnr).setCaption(PNR);
        issuedGrid.addColumn(Vendor::getFlightNo).setCaption(FLIGHT_NO);
        issuedGrid.addColumn(Vendor::getRoute).setCaption(ROUTE);
        issuedGrid.addColumn(Vendor::getCategory).setCaption(CATEGORY);
        issuedGrid.addColumn(Vendor::getVoucherNo).setCaption(VOUCHER_NO);
        issuedGrid.addColumn(Vendor::getDescription).setCaption(DESCRIPTION);
        issuedGrid.addColumn(Vendor::getStatus).setCaption(STATUS);

        redeemedGrid.addColumn(Vendor::getPaxName).setCaption(PAX_NAME);
        redeemedGrid.addColumn(Vendor::getPnr).setCaption(PNR);
        redeemedGrid.addColumn(Vendor::getFlightNo).setCaption(FLIGHT_NO);
        redeemedGrid.addColumn(Vendor::getRoute).setCaption(ROUTE);
        redeemedGrid.addColumn(Vendor::getCategory).setCaption(CATEGORY);
        redeemedGrid.addColumn(Vendor::getVoucherNo).setCaption(VOUCHER_NO);
        redeemedGrid.addColumn(Vendor::getDescription).setCaption(DESCRIPTION);
        redeemedGrid.addColumn(Vendor::getStatus).setCaption(STATUS);
        issuedGrid.setItems(issuedVendorList);
        redeemedGrid.setItems(redeemedVendorList);
    }
}
