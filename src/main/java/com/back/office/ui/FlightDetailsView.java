package com.back.office.ui;

import com.back.office.entity.Flights;
import com.back.office.entity.Sector;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;

public class FlightDetailsView extends CommonPageDetails {

    private final String FLIGHT_NAME = "Flight Name";
    private final String FLIGHT_FROM = "Flight From";
    private final String FLIGHT_TO = "Flight To";
    private final String NO_OF_SECTORS = "Number of Sectors";
    private final String SECTOR_FROM = "Sector From";
    private final String SECTOR_TO = "Sector To";
    private final String SECTOR_TYPE = "Sector Type";

    protected TextField flightNameFld;
    protected TextField flightFromFld;
    protected TextField flightToFld;
    protected ComboBox noOfSectorsComboBox;
    protected VerticalLayout sectorMainLayout;
    protected Table sectorTable;

    public FlightDetailsView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        mainUserInputLayout.addComponent(firstRow);

        sectorMainLayout = new VerticalLayout();
        mainUserInputLayout.addComponent(sectorMainLayout);

        flightNameFld = new TextField(FLIGHT_NAME);
        flightNameFld.setInputPrompt(FLIGHT_NAME);
        flightNameFld.setRequired(true);
        firstRow.addComponent(flightNameFld);

        flightFromFld = new TextField(FLIGHT_FROM);
        flightFromFld.setInputPrompt(FLIGHT_FROM);
        flightFromFld.setRequired(true);
        firstRow.addComponent(flightFromFld);

        flightToFld = new TextField(FLIGHT_TO);
        flightToFld.setInputPrompt(FLIGHT_TO);
        flightToFld.setRequired(true);
        firstRow.addComponent(flightToFld);

        noOfSectorsComboBox = new ComboBox(NO_OF_SECTORS);
        noOfSectorsComboBox.setInputPrompt(NO_OF_SECTORS);
        noOfSectorsComboBox.addItem("2");
        noOfSectorsComboBox.addItem("3");
        noOfSectorsComboBox.addItem("4");
        noOfSectorsComboBox.addItem("5");
        noOfSectorsComboBox.setNullSelectionAllowed(false);
        noOfSectorsComboBox.setRequired(true);
        noOfSectorsComboBox.setValue("2");
        noOfSectorsComboBox.addValueChangeListener((Property.ValueChangeListener) valueChangeEvent -> {
            addSectorLayout(Integer.parseInt(noOfSectorsComboBox.getValue().toString()));
        });

        firstRow.addComponent(noOfSectorsComboBox);

        addSectorLayout(2);
        buttonRow.setMargin(Constants.topMarginInfo);

        sectorTable = new Table();
        sectorTable.setSelectable(true);
        sectorTable.setMultiSelect(false);
        sectorTable.setSortEnabled(true);
        sectorTable.setPageLength(5);
        IndexedContainer normalContainer = generateSectorContainer();
        sectorTable.setContainerDataSource(normalContainer);
        sectorTable.setSizeFull();
        sectorTable.setStyleName("left-right-margin");
        tableLayout.addComponent(sectorTable);
        tableLayout.setExpandRatio(detailsTable,0.6F);
        tableLayout.setExpandRatio(sectorTable,0.4F);
        detailsTable.addItemClickListener((ItemClickEvent.ItemClickListener) event -> {
            showAvailableSectors(event.getItemId().toString());
        });

        userFormLayout.setWidth("70%");
        mainTableLayout.setWidth("80%");
        headerLayout.setWidth("70%");
    }

    private void showAvailableSectors(String itemId){
        List<Sector> sectors = connection.getFilterList("sectorFilter","flightId",Integer.parseInt(itemId),
                "com.back.office.entity.Sector");
        IndexedContainer container = (IndexedContainer)sectorTable.getContainerDataSource();
        container.removeAllItems();

        for(Sector sector : sectors){
            Item item = container.addItem(sector.getSectorId());
            item.getItemProperty(SECTOR_FROM).setValue(sector.getSectorFrom());
            item.getItemProperty(SECTOR_TO).setValue(sector.getSectorTo());
            item.getItemProperty(SECTOR_TYPE).setValue(sector.getSectorType());
        }
    }

    private void addSectorLayout(int index){
        sectorMainLayout.removeAllComponents();
        for(int i = 0;i<index;i++) {
            HorizontalLayout sector1Layout = new HorizontalLayout();
            sector1Layout.setSizeFull();
            sector1Layout.setMargin(Constants.topMarginInfo);
            sectorMainLayout.addComponent(sector1Layout, i);
            int sectorId = i+1;
            Label h1 = new Label("Sector " + sectorId);
            h1.addStyleName(ValoTheme.LABEL_H3);
            sector1Layout.addComponent(h1);

            TextField sectorFromFld = new TextField("Sector From");
            sectorFromFld.setInputPrompt("Sector From");
            sector1Layout.addComponent(sectorFromFld);

            TextField sectorToFld = new TextField("Sector To");
            sectorToFld.setInputPrompt("Sector To");
            sector1Layout.addComponent(sectorToFld);

            ComboBox sectorTypeComboBox = new ComboBox("Sector Type");
            sectorTypeComboBox.setInputPrompt("Sector Type");
            sectorTypeComboBox.addItem("International");
            sectorTypeComboBox.addItem("Domestic");
            sectorTypeComboBox.setNullSelectionAllowed(false);
            sector1Layout.addComponent(sectorTypeComboBox);

            TextField sectorIdFld = new TextField("Sector Id");
            sectorIdFld.setVisible(false);
            sector1Layout.addComponent(sectorIdFld);
        }
    }

    @Override
    protected IndexedContainer generateContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(FLIGHT_NAME, String.class, null);
        container.addContainerProperty(FLIGHT_FROM, String.class, null);
        container.addContainerProperty(FLIGHT_TO, String.class, null);
        container.addContainerProperty(NO_OF_SECTORS, Integer.class, null);

        List<Flights> flights = (List<Flights>)connection.getAllValues(className);
        for(Flights flight : flights){
            Item item = container.addItem(flight.getFlightId());
            item.getItemProperty(FLIGHT_NAME).setValue(flight.getFlightName());
            item.getItemProperty(FLIGHT_FROM).setValue(flight.getFlightFrom());
            item.getItemProperty(FLIGHT_TO).setValue(flight.getFlightTo());
            item.getItemProperty(NO_OF_SECTORS).setValue(flight.getNoOfSectors());
        }
        return container;
    }

    private IndexedContainer generateSectorContainer(){
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(SECTOR_FROM, String.class, null);
        container.addContainerProperty(SECTOR_TO, String.class, null);
        container.addContainerProperty(SECTOR_TYPE, String.class, null);

        return container;
    }

    @Override
    protected void insertDetails() {
        String isValidated = validateFields();
        List<Sector> sectors = getSectors();
        if(isValidated != null){
            Notification.show(isValidated);
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
                Notification.show(pageHeader +" added successfully");
                resetFields();
            } else {
                Notification.show("Something wrong, please try again");
            }
        }
        else{
            connection.updateObjectHBM(object);
            for(Sector sector : list){
                connection.updateObjectHBM(sector);
            }
            Notification.show(pageHeader + " updated successfully");
            updateTable(true,object,0);
            addButton.setCaption("Add");
            resetFields();
        }
    }

    private List<Sector> getSectors(){

        int sectorCount = sectorMainLayout.getComponentCount();
        List<Sector> sectors = new ArrayList<>();
        for(int i=0;i<sectorCount;i++){
            HorizontalLayout layout = (HorizontalLayout)sectorMainLayout.getComponent(i);
            String sectorFromVal = ((TextField)layout.getComponent(1)).getValue();
            String sectorToVal = ((TextField)layout.getComponent(2)).getValue();
            Object flightTypeVal = ((ComboBox)layout.getComponent(3)).getValue();
            String sectorId = ((TextField)layout.getComponent(4)).getValue();
            Sector sector = new Sector();
            int itemIdVal = (sectorId== null || sectorId.isEmpty()) ? 0 : Integer.parseInt(sectorId);
            int flightId = (idField.getValue() == null || idField.getValue().isEmpty()) ? 0 : Integer.parseInt(idField.getValue());
            sector.setSectorId(itemIdVal);
            sector.setFlightId(flightId);
            if(sectorFromVal != null && !sectorFromVal.isEmpty()) sector.setSectorFrom(sectorFromVal);
            else  return null;
            if(sectorToVal != null && !sectorToVal.isEmpty()) sector.setSectorTo(sectorToVal);
            else return null;
            if(flightTypeVal != null) sector.setSectorType(flightTypeVal.toString());
            else return null;
            sectors.add(sector);
        }
        return sectors;
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
            Item item = container.getItem(target);
            idField.setValue(target.toString());
            flightNameFld.setValue(item.getItemProperty(FLIGHT_NAME).getValue().toString());
            flightFromFld.setValue(item.getItemProperty(FLIGHT_TO).getValue().toString());
            flightToFld.setValue(item.getItemProperty(FLIGHT_TO).getValue().toString());
            noOfSectorsComboBox.setValue(item.getItemProperty(NO_OF_SECTORS).getValue().toString());

            IndexedContainer sectorContainer = (IndexedContainer) sectorTable.getContainerDataSource();
            int i = 0;
            for(Object obj : sectorContainer.getItemIds()){
                Item item1 = sectorContainer.getItem(obj);
                ((TextField)((HorizontalLayout)sectorMainLayout.getComponent(i)).getComponent(1)).setValue(item1.getItemProperty(SECTOR_FROM).getValue().toString());
                ((TextField)((HorizontalLayout)sectorMainLayout.getComponent(i)).getComponent(2)).setValue(item1.getItemProperty(SECTOR_TO).getValue().toString());
                ((ComboBox)((HorizontalLayout)sectorMainLayout.getComponent(i)).getComponent(3)).setValue(item1.getItemProperty(SECTOR_TYPE).getValue().toString());
                ((TextField)((HorizontalLayout)sectorMainLayout.getComponent(i)).getComponent(4)).setValue(obj.toString());
                i++;
            }

            addButton.setCaption("Edit");
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = FLIGHT_NAME;
        this.pageHeader = "Flight Details";
        this.className = "com.back.office.entity.Flights";
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        Flights flights = (Flights) details;
        IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
        Item item;
        if(isEdit){
            item  = container.getItem(flights.getFlightId());
        }
        else{
            item  = container.addItem(newId);
        }
        item.getItemProperty(FLIGHT_NAME).setValue(flights.getFlightName());
        item.getItemProperty(FLIGHT_FROM).setValue(flights.getFlightFrom());
        item.getItemProperty(FLIGHT_TO).setValue(flights.getFlightTo());
        item.getItemProperty(NO_OF_SECTORS).setValue(flights.getNoOfSectors());
        detailsTable.select(details);
    }

    @Override
    protected void resetFields(){
        flightNameFld.clear();
        flightFromFld.clear();
        flightToFld.clear();
        noOfSectorsComboBox.setValue("2");
        int sectorCount = sectorMainLayout.getComponentCount();
        for(int i=0;i<sectorCount;i++){
            HorizontalLayout layout =  (HorizontalLayout)sectorMainLayout.getComponent(i);
            ((TextField) layout.getComponent(1)).clear();
            ((TextField) layout.getComponent(2)).clear();
            ((ComboBox) layout.getComponent(3)).clear();
        }
    }
}
