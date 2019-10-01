package com.back.office.ui.wizard;

import com.back.office.entity.Flight;
import com.back.office.entity.Sector;
import com.back.office.ui.wizard.steps.flight.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CreateFlightView extends WizardCommonView {

    FilterGrid<Flight> flightsGrid;
    List<Flight> flightsList;
    Grid<Sector> sectorTable;

    private final String FLIGHT_NAME = "Flight No";
    private final String FLIGHT_FROM = "Flight From";
    private final String FLIGHT_TO = "Flight To";
    private final String NO_OF_SECTORS = "No of Sectors";
    private final String SECTOR_FROM = "Sector From";
    private final String SECTOR_TO = "Sector To";
    private final String SECTOR_TYPE = "Sector Type";
    private final String COUNTRY = "Country";
    private final String FLIGHT_TYPE = "Flight Type";
    private final String AIRCRAFT_REG_NO = "Aircraft Reg No";

    public CreateFlightView(){
        super();
    }

    @Override
    protected void defineStringFields() {
        headerName = "Flight Details";
        objectName = "flight";
        wizardName = "Create Flight";
        className = "com.back.office.entity.Flight";
    }

    @Override
    protected void createMainLayout(){
        super.createMainLayout();
        flightsGrid = new FilterGrid<>();
        flightsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        flightsGrid.setSizeFull();
        tableLayout.addComponent(flightsGrid);
        setDataInGrid();
        flightsGrid.addItemClickListener(itemClick -> {
            openViewEditWindow(false,itemClick.getItem());
        });
    }

    @Override
    protected void createItemWizard() {
        super.createWizard();
        wizard.addStep(new FlightCommonStep());
        wizard.addStep(new FlightOutboundCommonStep());
        wizard.addStep(new OutboundSectorStep());
        wizard.addStep(new FlightInboundCommonStep());
        wizard.addStep(new InboundSectorsStep());
        getUI().getUI().addWindow(window);
    }

    @Override
    protected void registerWizardBanClickListeners(){
        wizard.getCancelButton().addClickListener((Button.ClickListener) clickEvent ->
        {
            ConfirmDialog.show(getUI(), "Cancel Flight Creation", "Are you sure you want to cancel the wizard?",
                    "Yes", "No", new ConfirmDialog.Listener() {
                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                UI.getCurrent().getSession().setAttribute(objectName,null);
                                window.close();
                            }
                        }
                    });
        });
        wizard.getFinishButton().addClickListener((Button.ClickListener) clickEvent -> {
            ConfirmDialog.show(getUI(), "Add item", "Are you sure you want to add new Item?",
                    "Yes", "No", new ConfirmDialog.Listener() {
                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                Object obj = UI.getCurrent().getSession().getAttribute(objectName);
                                int id = connection.insertObjectHBM(obj);
                                updateTable(false,obj);
                                Map<Integer,Sector> obSectors = (Map<Integer, Sector>)UI.getCurrent().getSession().getAttribute("obSectorMap");
                                Map<Integer,Sector> ibSectors = (Map<Integer, Sector>)UI.getCurrent().getSession().getAttribute("ibSectorMap");
                                for(Sector sector : obSectors.values()){
                                    sector.setFlightId(id);
                                    connection.insertObjectHBM(sector);
                                }
                                for(Sector sector : ibSectors.values()){
                                    sector.setFlightId(id);
                                    connection.insertObjectHBM(sector);
                                }
                                UI.getCurrent().getSession().setAttribute(objectName,null);
                                window.close();
                            }
                        }
                    });
        });
    }

    private void setDataInGrid(){

        flightsGrid.addColumn(Flight::getCountry).setCaption(COUNTRY).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        flightsGrid.addColumn(Flight::getAircraftRegNo).setCaption(AIRCRAFT_REG_NO).
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
        flightsList = (List<Flight>)connection.getAllValues(className);
        flightsGrid.setItems(flightsList);

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

    private void openViewEditWindow(boolean isEdit, Flight item){

    }

    @Override
    protected void updateTable(boolean isEdit, Object details) {
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
}
