package com.back.office.ui;

import com.back.office.entity.Flights;
import com.back.office.entity.Sector;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;

import java.util.ArrayList;
import java.util.List;

public class FlightDetailsView extends CommonPageDetails {

    private final String FLIGHT_NAME = "Flight No";
    private final String FLIGHT_FROM = "Flight From";
    private final String FLIGHT_TO = "Flight To";
    private final String NO_OF_SECTORS = "Number of Sectors";
    private final String SECTOR_FROM = "Sector From";
    private final String SECTOR_TO = "Sector To";
    private final String SECTOR_TYPE = "Sector Type";
    private final String FLIGHT_TYPE = "Flight Type";

    protected TextField flightNameFld;
    protected TextField flightFromFld;
    protected TextField flightToFld;
    protected ComboBox noOfSectorsComboBox;
    protected VerticalLayout sectorMainLayout;
    protected Grid<Sector> sectorTable;
    private HorizontalLayout inputMapLayout;
    HorizontalLayout imageLayout;

    FilterGrid<Flights> flightsGrid;
    List<Flights> flightsList;

    public FlightDetailsView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();

        imageLayout = new HorizontalLayout();
        imageLayout.setSizeFull();
        imageLayout.setMargin(Constants.leftMargin);
        outerLayout.addComponent(imageLayout);
        outerLayout.setExpandRatio(userFormLayout,0.55f);
        outerLayout.setExpandRatio(imageLayout,0.45f);

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.setSizeFull();
        firstRow.setMargin(Constants.noMargin);
        firstRow.setWidth(66.67f,Unit.PERCENTAGE);
        mainUserInputLayout.addComponent(firstRow);

        HorizontalLayout mainDetailsSecondRow = new HorizontalLayout();
        mainDetailsSecondRow.setSizeFull();
        mainDetailsSecondRow.setMargin(Constants.noMargin);
        mainDetailsSecondRow.setWidth(66.67f,Unit.PERCENTAGE);
        mainUserInputLayout.addComponent(mainDetailsSecondRow);

        sectorMainLayout = new VerticalLayout();
        sectorMainLayout.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(sectorMainLayout);

        flightNameFld = new TextField(FLIGHT_NAME);
        flightNameFld.setDescription(FLIGHT_NAME);
        flightNameFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(flightNameFld);
        flightNameFld.addValueChangeListener(valueChangeEvent -> {isKeyFieldDirty = true;});

        noOfSectorsComboBox = new ComboBox(NO_OF_SECTORS);
        noOfSectorsComboBox.setDescription(NO_OF_SECTORS);
        noOfSectorsComboBox.setItems("2","3","4","5");
        noOfSectorsComboBox.setEmptySelectionAllowed(false);
        noOfSectorsComboBox.setRequiredIndicatorVisible(true);
        noOfSectorsComboBox.setValue("2");
        noOfSectorsComboBox.addValueChangeListener(valueChangeEvent -> {
            addSectorLayout(Integer.parseInt(noOfSectorsComboBox.getValue().toString()));
        });
        firstRow.addComponent(noOfSectorsComboBox);

        flightFromFld = new TextField(FLIGHT_FROM);
        flightFromFld.setDescription(FLIGHT_FROM);
        flightFromFld.setRequiredIndicatorVisible(true);
        mainDetailsSecondRow.addComponent(flightFromFld);

        flightToFld = new TextField(FLIGHT_TO);
        flightToFld.setDescription(FLIGHT_TO);
        flightToFld.setRequiredIndicatorVisible(true);
        mainDetailsSecondRow.addComponent(flightToFld);

        addSectorLayout(2);

        flightsGrid = new FilterGrid<>();
        flightsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        flightsGrid.setSizeFull();
        tableLayout.addComponent(flightsGrid);
        flightsGrid.addColumn(Flights::getFlightName).setCaption(FLIGHT_NAME).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        flightsGrid.addColumn(Flights::getFlightFrom).setCaption(FLIGHT_FROM).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        flightsGrid.addColumn(Flights::getFlightTo).setCaption(FLIGHT_TO).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        flightsGrid.addColumn(Flights::getNoOfSectors).setCaption(NO_OF_SECTORS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        setDataInGrid();
        GridContextMenu<Flights> gridMenu = new GridContextMenu<>(flightsGrid);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);

        sectorTable = new Grid<>();
        sectorTable.setSizeFull();
        sectorTable.setStyleName("left-right-margin");
        tableLayout.addComponent(sectorTable);
        tableLayout.setExpandRatio(flightsGrid,0.6F);
        tableLayout.setExpandRatio(sectorTable,0.4F);

        sectorTable.addColumn(Sector::getSectorFrom).setCaption(SECTOR_FROM);
        sectorTable.addColumn(Sector::getSectorTo).setCaption(SECTOR_TO);
        sectorTable.addColumn(Sector::getFlightType).setCaption(FLIGHT_TYPE);
        sectorTable.addColumn(Sector::getSectorType).setCaption(SECTOR_TYPE);
        flightsGrid.addItemClickListener(event -> {
            sectorTable.setItems(showAvailableSectors(((Flights)event.getItem()).getFlightId()));
        });

        mainTableLayout.setWidth("80%");
        headerLayout.setWidth("70%");
        googleMap();
    }

    private List<Sector> showAvailableSectors(int itemId){
        List<Sector> sectors = connection.getFilterList("sectorFilter","flightId",itemId,
                "com.back.office.entity.Sector","sectorId");
        return sectors;
    }

    private void addSectorLayout(int index){
        sectorMainLayout.removeAllComponents();
        for(int i = 0;i<index;i++) {
            VerticalLayout sector1Layout = new VerticalLayout();
            sector1Layout.setMargin(Constants.noMargin);
            sector1Layout.setSizeFull();
            sectorMainLayout.addComponent(sector1Layout, i);

            HorizontalLayout firstRow = new HorizontalLayout();
            firstRow.setSizeFull();
            firstRow.setMargin(Constants.noMargin);
            sector1Layout.addComponent(firstRow);

            HorizontalLayout secondRow = new HorizontalLayout();
            secondRow.setSizeFull();
            secondRow.setWidth("66.67%");
            secondRow.setMargin(Constants.noMargin);
            sector1Layout.addComponent(secondRow);

            int sectorId = i+1;
            Label h1 = new Label("Sector " + sectorId);
            h1.addStyleName(ValoTheme.LABEL_H3);
            firstRow.addComponent(h1);

            TextField sectorFromFld = new TextField("Sector From");
            sectorFromFld.setDescription("Sector From");
            firstRow.addComponent(sectorFromFld);

            TextField sectorToFld = new TextField("Sector To");
            sectorToFld.setDescription("Sector To");
            firstRow.addComponent(sectorToFld);

            ComboBox flightType = new ComboBox("Flight Type");
            flightType.setDescription("Flight Type");
            flightType.setItems("Inbound","Outbound");
            flightType.setEmptySelectionAllowed(false);
            secondRow.addComponent(flightType);

            ComboBox sectorTypeComboBox = new ComboBox("Sector Type");
            sectorTypeComboBox.setDescription("Sector Type");
            sectorTypeComboBox.setItems("International","Domestic");
            sectorTypeComboBox.setEmptySelectionAllowed(false);
            secondRow.addComponent(sectorTypeComboBox);

            TextField sectorIdFld = new TextField("Sector Id");
            sectorIdFld.setVisible(false);
            secondRow.addComponent(sectorIdFld);
        }
    }

    protected void deleteItem(Object target){
        if(target != null) {
            Flights flight = (Flights) target;
            boolean success = connection.deleteObjectHBM(flight.getFlightId(), className);
            List<Sector> sectors = showAvailableSectors(flight.getFlightId());
            for(Sector sector : sectors){
                connection.deleteObjectHBM(sector.getSectorId(), "com.back.office.entity.Sector");
            }
            if(success){
                BackOfficeUtils.showNotification("Success", "Detail delete successfully", VaadinIcons.CHECK_CIRCLE_O);
                flightsList.remove(target);
                flightsGrid.setItems(flightsList);
                sectorTable.setItems(new Sector());
            }
            else {
                BackOfficeUtils.showNotification("Error", "Something wrong, please try again", VaadinIcons.CLOSE);
            }
        }
    }

    private void setDataInGrid(){
        flightsList = (List<Flights>)connection.getAllValues(className);
        flightsGrid.setItems(flightsList);
    }

    @Override
    protected void insertDetails() {
        String isValidated = validateFields();
        List<Sector> sectors = getSectors();
        if(isValidated != null){
            Notification.show(isValidated, Notification.Type.WARNING_MESSAGE);
        }
        else if(sectors == null){
            Notification.show("Fill all the sector fields");
        }
        else{
            int itemIdVal = (idField.getValue() == null || idField.getValue().isEmpty()) ? 0 : Integer.parseInt(idField.getValue());
            Flights flight = new Flights();
            flight.setFlightId(itemIdVal);
            flight.setFlightName(flightNameFld.getValue());
            flight.setFlightFrom(flightFromFld.getValue());
            flight.setFlightTo(flightToFld.getValue());
            flight.setNoOfSectors(Integer.parseInt(noOfSectorsComboBox.getValue().toString()));
            addOrUpdateMultipleDetails(flight,sectors);
        }
    }

    protected void addOrUpdateMultipleDetails(Object object, List<Sector> list){
        if(addButton.getCaption().equals("Add")) {
            int newId = connection.insertObjectHBM(object);
            if (newId != 0) {
                updateTable(false,object,newId);
                for(Sector sector : list){
                    sector.setFlightId(newId);
                    connection.insertObjectHBM(sector);
                }
                BackOfficeUtils.showNotification("Success", "Flight added successfully", VaadinIcons.CHECK_CIRCLE_O);
                resetFields();
                sectorTable.setItems(new Sector());
            } else {
                BackOfficeUtils.showNotification("Error","Something wrong, please try again",VaadinIcons.CLOSE);
            }
        }
        else{
            connection.updateObjectHBM(object);
            for(Sector sector : list){
                connection.updateObjectHBM(sector);
            }
            BackOfficeUtils.showNotification("Success", "Flight updated successfully", VaadinIcons.CHECK_CIRCLE_O);
            updateTable(true,object,0);
            addButton.setCaption("Add");
            resetFields();
            sectorTable.setItems(new Sector());
        }
    }

    private List<Sector> getSectors(){

        int sectorCount = sectorMainLayout.getComponentCount();
        List<Sector> sectors = new ArrayList<>();
        for(int i=0;i<sectorCount;i++){
            VerticalLayout layout = (VerticalLayout)sectorMainLayout.getComponent(i);
            HorizontalLayout firstRow = (HorizontalLayout)layout.getComponent(0);
            HorizontalLayout secondRow = (HorizontalLayout)layout.getComponent(1);

            String sectorFromVal = ((TextField)firstRow.getComponent(1)).getValue();
            String sectorToVal = ((TextField)firstRow.getComponent(2)).getValue();
            Object flightType = ((ComboBox)secondRow.getComponent(0)).getValue();
            Object sectorType = ((ComboBox)secondRow.getComponent(1)).getValue();
            String sectorId = ((TextField)secondRow.getComponent(2)).getValue();
            Sector sector = new Sector();
            int itemIdVal = (sectorId== null || sectorId.isEmpty()) ? 0 : Integer.parseInt(sectorId);
            int flightId = (idField.getValue() == null || idField.getValue().isEmpty()) ? 0 : Integer.parseInt(idField.getValue());
            sector.setSectorId(itemIdVal);
            sector.setFlightId(flightId);
            if(sectorFromVal != null && !sectorFromVal.isEmpty()) sector.setSectorFrom(sectorFromVal);
            else  return null;
            if(sectorToVal != null && !sectorToVal.isEmpty()) sector.setSectorTo(sectorToVal);
            else return null;
            if(sectorType != null) sector.setSectorType(sectorType.toString());
            else return null;
            if(flightType != null) sector.setFlightType(flightType.toString());
            else return null;
            sectors.add(sector);
        }
        return sectors;
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            Flights flight = (Flights) target;
            idField.setValue(String.valueOf(flight.getFlightId()));
            flightNameFld.setValue(flight.getFlightName());
            flightFromFld.setValue(flight.getFlightFrom());
            flightToFld.setValue(flight.getFlightTo());
            noOfSectorsComboBox.setValue(flight.getNoOfSectors());

            List<Sector> sectors = showAvailableSectors(flight.getFlightId());
            int i = 0;
            for(Sector sector : sectors){

                VerticalLayout layout =  (VerticalLayout)sectorMainLayout.getComponent(i);
                HorizontalLayout firstRow = (HorizontalLayout)layout.getComponent(0);
                HorizontalLayout secondRow = (HorizontalLayout)layout.getComponent(1);

                ((TextField)(firstRow.getComponent(1))).setValue(sector.getSectorFrom());
                ((TextField)firstRow.getComponent(2)).setValue(sector.getSectorTo());
                ((ComboBox)secondRow.getComponent(0)).setValue(sector.getFlightType());
                ((ComboBox)secondRow.getComponent(1)).setValue(sector.getSectorType());
                ((TextField)secondRow.getComponent(2)).setValue(String.valueOf(sector.getSectorId()));
                i++;
            }
            editObj = flight;
            addButton.setCaption("Save");
            isKeyFieldDirty = false;
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = FLIGHT_NAME;
        this.pageHeader = "Flight Details";
        this.className = "com.back.office.entity.Flights";
        this.keyFieldDBName = "flightName";
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        Flights flights = (Flights) details;
        if(isEdit){
            int index = flightsList.indexOf(editObj);
            flightsList.remove(editObj);
            flightsList.add(index,flights);
        }
        else{
            flightsList.add(flights);
        }
        flightsGrid.setItems(flightsList);
    }

    @Override
    protected void resetFields(){
        flightNameFld.clear();
        flightFromFld.clear();
        flightToFld.clear();
        noOfSectorsComboBox.setValue("2");
        int sectorCount = sectorMainLayout.getComponentCount();
        for(int i=0;i<sectorCount;i++){
            VerticalLayout layout =  (VerticalLayout)sectorMainLayout.getComponent(i);
            HorizontalLayout firstRow = (HorizontalLayout)layout.getComponent(0);
            HorizontalLayout secondRow = (HorizontalLayout)layout.getComponent(1);
            ((TextField) firstRow.getComponent(1)).clear();
            ((TextField) firstRow.getComponent(2)).clear();
            ((ComboBox) secondRow.getComponent(0)).clear();
            ((ComboBox) secondRow.getComponent(1)).clear();
        }
    }

    @Override
    protected TextField getKeyField() {
        return flightNameFld;
    }

    private void googleMap(){
        VerticalLayout layout = new VerticalLayout();
        GoogleMap googleMap = new GoogleMap("AIzaSyAlyIX7vXwAqfZPZ3T4Gcck0jdEQOUk3tM", null, "english");
        googleMap.setSizeFull();
        googleMap.setWidth(90, Unit.PERCENTAGE);
        googleMap.setHeight(500,Unit.PIXELS);
        layout.setMargin(Constants.noMargin);
        layout.setSizeFull();

        googleMap.addMarker("Montreal", new LatLon(
                45.466283, -73.745934), false, null);
        GoogleMapMarker customMarker = new GoogleMapMarker("Torento", new LatLon(43.682496, -79.609814),false, null);
        googleMap.addMarker(customMarker);
        googleMap.setCenter(new LatLon( 43.682496, -79.609814));
        List<LatLon> latLons = new ArrayList<>();
        latLons.add(new LatLon(43.682496, -79.609814));
        latLons.add(new LatLon(45.466283, -73.745934));
        GoogleMapPolyline polyline = new GoogleMapPolyline(latLons);
        polyline.setStrokeWeight(4);
        polyline.setStrokeColor("red");
        googleMap.addPolyline(polyline);
        googleMap.setMinZoom(4);
        googleMap.setMaxZoom(16);
        googleMap.setZoom(6);
        layout.addComponent(googleMap);
        imageLayout.addComponent(layout);
    }
}
