package com.back.office.ui;

import com.back.office.entity.Flight;
import com.back.office.entity.Sector;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.event.dd.acceptcriteria.Not;
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
    private final String NO_OF_SECTORS = "No of Sectors";
    private final String SECTOR_FROM = "Sector From";
    private final String SECTOR_TO = "Sector To";
    private final String SECTOR_TYPE = "Sector Type";
    private final String COUNTRY = "Country";
    private final String FLIGHT_TYPE = "Flight Type";

    protected TextField countryFld;

    protected TextField obFlightNameFld;
    protected TextField obFlightFromFld;
    protected TextField obFlightToFld;
    protected ComboBox obNoOfSectorsComboBox;

    protected TextField ibFlightNameFld;
    protected TextField ibFlightFromFld;
    protected TextField ibFlightToFld;
    protected ComboBox ibNoOfSectorsComboBox;

    protected VerticalLayout obSectorMainLayout;
    protected VerticalLayout ibSectorMainLayout;
    protected Grid<Sector> sectorTable;
    HorizontalLayout imageLayout;

    FilterGrid<Flight> flightsGrid;
    List<Flight> flightsList;

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

        countryFld = new TextField(COUNTRY);
        countryFld.setDescription(COUNTRY);
        countryFld.setRequiredIndicatorVisible(true);
        mainUserInputLayout.addComponent(countryFld);

        HorizontalLayout firstRow = new HorizontalLayout();
        Label fld1 = new Label();
        fld1.setValue("Outbound Flight");
        firstRow.setSizeFull();
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(fld1);
        mainUserInputLayout.addComponent(firstRow);

        obSectorMainLayout = new VerticalLayout();
        obSectorMainLayout.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(obSectorMainLayout);

        obFlightNameFld = new TextField(FLIGHT_NAME);
        obFlightNameFld.setDescription(FLIGHT_NAME);
        obFlightNameFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(obFlightNameFld);
        obFlightNameFld.setSizeFull();
        obFlightNameFld.addValueChangeListener(valueChangeEvent -> {isKeyFieldDirty = true;});

        obNoOfSectorsComboBox = new ComboBox(NO_OF_SECTORS);
        obNoOfSectorsComboBox.setDescription(NO_OF_SECTORS);
        obNoOfSectorsComboBox.setItems("1","2","3");
        obNoOfSectorsComboBox.setEmptySelectionAllowed(false);
        obNoOfSectorsComboBox.setRequiredIndicatorVisible(true);
        obNoOfSectorsComboBox.setValue("1");
        obNoOfSectorsComboBox.setSizeFull();
        obNoOfSectorsComboBox.addValueChangeListener(valueChangeEvent -> {
            addSectorLayout(Integer.parseInt(obNoOfSectorsComboBox.getValue().toString()),obSectorMainLayout);
        });
        firstRow.addComponent(obNoOfSectorsComboBox);

        obFlightFromFld = new TextField(FLIGHT_FROM);
        obFlightFromFld.setDescription(FLIGHT_FROM);
        obFlightFromFld.setSizeFull();
        obFlightFromFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(obFlightFromFld);

        obFlightToFld = new TextField(FLIGHT_TO);
        obFlightToFld.setDescription(FLIGHT_TO);
        obFlightToFld.setSizeFull();
        obFlightToFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(obFlightToFld);

        addSectorLayout(1,obSectorMainLayout);

        HorizontalLayout ibMainLayout = new HorizontalLayout();
        Label ibFld = new Label();
        ibFld.setValue("Inbound Flight");
        ibMainLayout.setSizeFull();
        ibMainLayout.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(ibFld);
        mainUserInputLayout.addComponent(ibMainLayout);

        ibSectorMainLayout = new VerticalLayout();
        ibSectorMainLayout.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(ibSectorMainLayout);

        ibFlightNameFld = new TextField(FLIGHT_NAME);
        ibFlightNameFld.setDescription(FLIGHT_NAME);
        ibFlightNameFld.setRequiredIndicatorVisible(true);
        ibMainLayout.addComponent(ibFlightNameFld);
        ibFlightNameFld.setSizeFull();
        ibFlightNameFld.addValueChangeListener(valueChangeEvent -> {isKeyFieldDirty = true;});

        ibNoOfSectorsComboBox = new ComboBox(NO_OF_SECTORS);
        ibNoOfSectorsComboBox.setDescription(NO_OF_SECTORS);
        ibNoOfSectorsComboBox.setItems("1","2","3");
        ibNoOfSectorsComboBox.setEmptySelectionAllowed(false);
        ibNoOfSectorsComboBox.setRequiredIndicatorVisible(true);
        ibNoOfSectorsComboBox.setValue("1");
        ibNoOfSectorsComboBox.setSizeFull();
        ibNoOfSectorsComboBox.addValueChangeListener(valueChangeEvent -> {
            addSectorLayout(Integer.parseInt(ibNoOfSectorsComboBox.getValue().toString()),ibSectorMainLayout);
        });
        ibMainLayout.addComponent(ibNoOfSectorsComboBox);

        ibFlightFromFld = new TextField(FLIGHT_FROM);
        ibFlightFromFld.setDescription(FLIGHT_FROM);
        ibFlightFromFld.setSizeFull();
        ibFlightFromFld.setRequiredIndicatorVisible(true);
        ibMainLayout.addComponent(ibFlightFromFld);

        ibFlightToFld = new TextField(FLIGHT_TO);
        ibFlightToFld.setDescription(FLIGHT_TO);
        ibFlightToFld.setSizeFull();
        ibFlightToFld.setRequiredIndicatorVisible(true);
        ibMainLayout.addComponent(ibFlightToFld);

        addSectorLayout(1,ibSectorMainLayout);

        flightsGrid = new FilterGrid<>();
        flightsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        flightsGrid.setSizeFull();
        tableLayout.addComponent(flightsGrid);
        flightsGrid.addColumn(Flight::getCountry).setCaption(COUNTRY).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        flightsGrid.addColumn(Flight::getObFlightNo).setCaption("OB " + FLIGHT_NAME).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        flightsGrid.addColumn(Flight::getObFlightFrom).setCaption("OB " +FLIGHT_FROM).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        flightsGrid.addColumn(Flight::getObFlightTo).setCaption("OB " +FLIGHT_TO).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        flightsGrid.addColumn(Flight::getObNoOfSectors).setCaption("OB Sectors").
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());

        flightsGrid.addColumn(Flight::getIbFlightNo).setCaption("IB " + FLIGHT_NAME).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        flightsGrid.addColumn(Flight::getIbFlightFrom).setCaption("IB " +FLIGHT_FROM).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        flightsGrid.addColumn(Flight::getIbFlightTo).setCaption("IB " +FLIGHT_TO).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        flightsGrid.addColumn(Flight::getIbNoOfSectors).setCaption("IB Sectors").
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());

        setDataInGrid();
        GridContextMenu<Flight> gridMenu = new GridContextMenu<>(flightsGrid);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);

        sectorTable = new Grid<>();
        sectorTable.setSizeFull();
        sectorTable.setStyleName("left-right-margin");
        tableLayout.addComponent(sectorTable);
        tableLayout.setExpandRatio(flightsGrid,0.6F);
        tableLayout.setExpandRatio(sectorTable,0.4F);

        sectorTable.addColumn(Sector::getSectorFrom).setCaption(SECTOR_FROM);
        sectorTable.addColumn(Sector::getSectorTo).setCaption(SECTOR_TO);
        sectorTable.addColumn(Sector::getSectorType).setCaption(SECTOR_TYPE);
        sectorTable.addColumn(Sector::getFlightType).setCaption(FLIGHT_TYPE);
        flightsGrid.addItemClickListener(event -> {
            sectorTable.setItems(showAvailableSectors((event.getItem()).getFlightId(),null));
        });

        mainTableLayout.setWidth("80%");
        headerLayout.setWidth("70%");
        googleMap();
    }

    private List<Sector> showAvailableSectors(int itemId,String flightType){
        List<Sector> obSectors =  connection.getSectorsFromFlightType(itemId,"Outbound");
        List<Sector> ibSectors = connection.getSectorsFromFlightType(itemId,"Inbound");
        if("Outbound".equals(flightType)){
            return obSectors;
        }
        else if("Inbound".equals(flightType)){
            return ibSectors;
        }
        else{
            List<Sector> allSectors = new ArrayList<>();
            allSectors.addAll(obSectors);
            allSectors.addAll(ibSectors);
            return allSectors;
        }
    }

    private void addSectorLayout(int index,VerticalLayout layout){
        layout.removeAllComponents();
        for(int i = 0;i<index;i++) {
            HorizontalLayout firstRow = new HorizontalLayout();
            firstRow.setSizeFull();
            firstRow.setMargin(Constants.noMargin);
            layout.addComponent(firstRow, i);

            int sectorId = i+1;
            Label h1 = new Label("Sector " + sectorId);
            h1.setSizeFull();
            h1.addStyleName(ValoTheme.LABEL_H3);
            firstRow.addComponent(h1);

            TextField sectorFromFld = new TextField("Sector From");
            sectorFromFld.setDescription("Sector From");
            sectorFromFld.setSizeFull();
            firstRow.addComponent(sectorFromFld);

            TextField sectorToFld = new TextField("Sector To");
            sectorToFld.setDescription("Sector To");
            sectorToFld.setSizeFull();
            firstRow.addComponent(sectorToFld);

            ComboBox sectorTypeComboBox = new ComboBox("Sector Type");
            sectorTypeComboBox.setDescription("Sector Type");
            sectorTypeComboBox.setItems("International","Domestic");
            sectorTypeComboBox.setEmptySelectionAllowed(false);
            sectorTypeComboBox.setSizeFull();
            firstRow.addComponent(sectorTypeComboBox);

            TextField sectorIdFld = new TextField("Sector Id");
            sectorIdFld.setVisible(false);
            firstRow.addComponent(sectorIdFld);
        }
    }

    protected void deleteItem(Object target){
        if(target != null) {
            Flight flight = (Flight) target;
            boolean success = connection.deleteObjectHBM(flight.getFlightId(), className);
            List<Sector> sectors = showAvailableSectors(flight.getFlightId(),null);
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
        flightsList = (List<Flight>)connection.getAllValues(className);
        flightsGrid.setItems(flightsList);
    }

    @Override
    protected void insertDetails() {
        String isValidated = validateFields();
        List<Sector> sectors = getTotalSectors();
        if(isValidated != null){
            Notification.show(isValidated, Notification.Type.WARNING_MESSAGE);
        }
        else if(sectors == null){
            Notification.show("Fill all the sector fields");
        }
        else if(countryFld.getValue() == null || countryFld.getValue().isEmpty()){
            Notification.show("Country field is mandatory", Notification.Type.WARNING_MESSAGE);
        }
        else{
            int itemIdVal = (idField.getValue() == null || idField.getValue().isEmpty()) ? 0 : Integer.parseInt(idField.getValue());
            Flight flight = new Flight();
            flight.setFlightId(itemIdVal);
            flight.setCountry(countryFld.getValue());
            flight.setObFlightNo(obFlightNameFld.getValue());
            flight.setObFlightFrom(obFlightFromFld.getValue());
            flight.setObFlightTo(obFlightToFld.getValue());
            flight.setObNoOfSectors(Integer.parseInt(obNoOfSectorsComboBox.getValue().toString()));
            flight.setIbFlightNo(ibFlightNameFld.getValue());
            flight.setIbFlightFrom(ibFlightFromFld.getValue());
            flight.setIbFlightTo(ibFlightToFld.getValue());
            flight.setIbNoOfSectors(Integer.parseInt(ibNoOfSectorsComboBox.getValue().toString()));
            flight.setBaseStation(UI.getCurrent().getSession().getAttribute("baseStation").toString());
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

    private List<Sector> getTotalSectors(){
        List<Sector> obSectors = getSectors(obSectorMainLayout,"Outbound");
        List<Sector> ibSectors = getSectors(ibSectorMainLayout,"Inbound");
        if(obSectors != null && ibSectors != null){
            List<Sector> totalSectors = new ArrayList<>();
            totalSectors.addAll(obSectors);
            totalSectors.addAll(ibSectors);
            return totalSectors;
        }
        return null;
    }

    private List<Sector> getSectors(VerticalLayout layout,String flightType){

        int sectorCount = layout.getComponentCount();
        List<Sector> sectors = new ArrayList<>();
        for(int i=0;i<sectorCount;i++){
            HorizontalLayout firstRow = (HorizontalLayout) layout.getComponent(i);
            String sectorFromVal = ((TextField)firstRow.getComponent(1)).getValue();
            String sectorToVal = ((TextField)firstRow.getComponent(2)).getValue();
            Object sectorType = ((ComboBox)firstRow.getComponent(3)).getValue();
            String sectorId = ((TextField)firstRow.getComponent(4)).getValue();
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
            if(flightType != null) sector.setFlightType(flightType);
            else return null;
            sectors.add(sector);
        }
        return sectors;
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            Flight flight = (Flight) target;
            idField.setValue(String.valueOf(flight.getFlightId()));
            countryFld.setValue(flight.getCountry());
            obFlightNameFld.setValue(flight.getObFlightNo());
            obFlightFromFld.setValue(flight.getObFlightFrom());
            obFlightToFld.setValue(flight.getObFlightTo());
            obNoOfSectorsComboBox.setValue(flight.getObNoOfSectors());
            ibFlightNameFld.setValue(flight.getIbFlightNo());
            ibFlightFromFld.setValue(flight.getIbFlightFrom());
            ibFlightToFld.setValue(flight.getIbFlightTo());
            ibNoOfSectorsComboBox.setValue(flight.getIbNoOfSectors());

            fillSectorEditDetails(flight.getFlightId(),"Outbound",obSectorMainLayout);
            fillSectorEditDetails(flight.getFlightId(),"Inbound",ibSectorMainLayout);
            editObj = flight;
            addButton.setCaption("Save");
            isKeyFieldDirty = false;
        }
    }

    private void fillSectorEditDetails(int flightId,String flightType,VerticalLayout layout){
        List<Sector> sectors = showAvailableSectors(flightId,flightType);
        int i = 0;
        for(Sector sector : sectors){

            HorizontalLayout firstRow =  (HorizontalLayout) layout.getComponent(i);
            ((TextField)(firstRow.getComponent(1))).setValue(sector.getSectorFrom());
            ((TextField)firstRow.getComponent(2)).setValue(sector.getSectorTo());
            ((ComboBox)firstRow.getComponent(3)).setValue(sector.getSectorType());
            ((TextField)firstRow.getComponent(4)).setValue(String.valueOf(sector.getSectorId()));
            i++;
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = FLIGHT_NAME;
        this.pageHeader = "Flight Details";
        this.className = "com.back.office.entity.Flight";
        this.keyFieldDBName = "obFlightNo";
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        Flight flights = (Flight) details;
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
        obFlightNameFld.clear();
        obFlightFromFld.clear();
        obFlightToFld.clear();
        obNoOfSectorsComboBox.setValue("1");
        ibFlightNameFld.clear();
        ibFlightFromFld.clear();
        ibFlightToFld.clear();
        ibNoOfSectorsComboBox.setValue("1");
        countryFld.clear();
        clearSectorFields(obSectorMainLayout);
        clearSectorFields(ibSectorMainLayout);
    }

    private void clearSectorFields(VerticalLayout layout){
        int obSectorCount = layout.getComponentCount();
        for(int i=0;i<obSectorCount;i++){
            HorizontalLayout firstRow =  (HorizontalLayout) layout.getComponent(i);
            ((TextField) firstRow.getComponent(1)).clear();
            ((TextField) firstRow.getComponent(2)).clear();
            ((ComboBox) firstRow.getComponent(3)).clear();
        }
    }

    @Override
    protected TextField getKeyField() {
        return obFlightNameFld;
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
