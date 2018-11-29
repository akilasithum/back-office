package com.back.office.ui;

import com.back.office.db.DBConnection;
import com.back.office.entity.AircraftDetails;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.Action;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;
import java.util.List;

public class AirCraftTypeView extends VerticalLayout implements View {

    TextField aircraftType;
    TextField galleyPosition;
    ComboBox equipmentType;
    TextField noOfFullCarts;
    TextField totalNoOfSeats;
    CheckBox activeCheckBox;
    TextField aircraftId;
    Button addButton;
    Button resetButton;
    DBConnection connection;
    Table airCraftDetailsTable;

    private final String AIRCRAFT_NAME = "Aircraft Name";
    private final String GALLEY_POSITION = "Galley Position";
    private final String EQUIPMENT_TYPE = "Equipment Type";
    private final String NO_OF_EQUIPMENTS = "No of Full Carts";
    private final String TOTLA_NO_OF_SEATS = "Total No of Seats";
    private final String STATUS = "Status";
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    public AirCraftTypeView(){
        connection = DBConnection.getInstance();
        setMargin(true);
        createMainLayout();
    }

    private void createMainLayout(){
        setSpacing(true);
        Label h1 = new Label("Aircraft Type Details");
        h1.addStyleName(ValoTheme.LABEL_H1);
        addComponent(h1);

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        addComponent(firstRow);

        aircraftType = new TextField("Aircraft Name");
        aircraftType.setInputPrompt("Aircraft Name");
        aircraftType.setRequired(true);
        firstRow.addComponent(aircraftType);

        galleyPosition = new TextField("Galley Position");
        galleyPosition.setInputPrompt("Galley Position");
        firstRow.addComponent(galleyPosition);

        equipmentType = new ComboBox("Equipment Type");
        equipmentType.addItem("Full Cart");
        equipmentType.addItem("Half Cart");
        equipmentType.addItem("Container");
        equipmentType.setTextInputAllowed(false);
        equipmentType.setNullSelectionAllowed(false);
        equipmentType.select("Full Cart");
        equipmentType.setRequired(true);
        firstRow.addComponent(equipmentType);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        addComponent(secondRow);

        noOfFullCarts = new TextField("No of Full Carts");
        noOfFullCarts.setInputPrompt("No of Full Carts");
        noOfFullCarts.setRequired(true);
        secondRow.addComponent(noOfFullCarts);

        totalNoOfSeats = new TextField("Total No of Seats");
        totalNoOfSeats.setInputPrompt("Total No of Seats");
        secondRow.addComponent(totalNoOfSeats);

        activeCheckBox = new CheckBox("Active", true);
        secondRow.addComponent(activeCheckBox);

        HorizontalLayout row = new HorizontalLayout();
        row.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        row.setSpacing(true);
        addComponent(row);


        HorizontalLayout rowFilter = new HorizontalLayout();
        rowFilter.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        MarginInfo marginInfo = new MarginInfo(true,false,false,false);
        rowFilter.setMargin(marginInfo);
        rowFilter.setSpacing(true);

        TextField filterFiled = new TextField();
        filterFiled.setInputPrompt("Filter by name");
        rowFilter.addComponent(filterFiled);

        Button filterBtn = new Button("Filter");
        filterBtn.addClickListener((Button.ClickListener) clickEvent -> {
            IndexedContainer container = (IndexedContainer)airCraftDetailsTable.getContainerDataSource();
            if(filterFiled.getValue() == null || filterFiled.getValue().isEmpty()) {
                container.removeContainerFilters(AIRCRAFT_NAME);
            }
            else{
                container.addContainerFilter(AIRCRAFT_NAME, filterFiled.getValue(), true, false);
            }
        });
        rowFilter.addComponent(filterBtn);
        addComponent(rowFilter);

        addButton = new Button("Add");
        addButton.addClickListener((Button.ClickListener) clickEvent -> insertFlightDetails());
        row.addComponent(addButton);
        resetButton = new Button("Reset");
        resetButton.addClickListener((Button.ClickListener) clickEvent -> resetFields());
        row.addComponent(resetButton);
        aircraftId = new TextField("Aircraft Id");
        aircraftId.setVisible(false);
        row.addComponent(aircraftId);

        airCraftDetailsTable = new Table();
        airCraftDetailsTable.setSelectable(true);
        airCraftDetailsTable.setMultiSelect(false);
        airCraftDetailsTable.setSortEnabled(true);
        airCraftDetailsTable.setColumnCollapsingAllowed(true);
        airCraftDetailsTable.setColumnReorderingAllowed(true);
        airCraftDetailsTable.setPageLength(10);

        IndexedContainer normalContainer = generateContainer();
        airCraftDetailsTable.setContainerDataSource(normalContainer);
        airCraftDetailsTable.setSizeFull();
        airCraftDetailsTable.addActionHandler(actionHandler);

        addComponent(airCraftDetailsTable);
    }

    Action.Handler actionHandler = new Action.Handler() {
        private final Action editItem = new Action("Edit Aircraft Type" , FontAwesome.EDIT);
        private final Action deleteItem = new Action("Delete Aircraft Type" , FontAwesome.REMOVE);
        private final Action[] ACTIONS = new Action[] {editItem, deleteItem};

        @Override
        public void handleAction(Action action, Object sender, Object target) {
            if(action.getCaption().equals("Edit Aircraft Type")){
                fillEditDetails(target);
            }
            else if(action.getCaption().equals("Delete Aircraft Type")){
                ConfirmDialog.show(getUI(), "Delete Aircraft", "Are you sure you want to delete Aircraft?",
                        "Yes", "No", new ConfirmDialog.Listener() {

                            public void onClose(ConfirmDialog dialog) {
                                if(dialog.isConfirmed()){
                                    deleteItem(target);
                                }
                            }
                        });
            }
        }

        @Override
        public Action[] getActions(Object target, Object sender) {
            return ACTIONS;
        }
    };

    private void deleteItem(Object target){
        if(target != null){
            boolean success = connection.deleteAircraftDetails(Integer.parseInt(target.toString()));
            if(success){
                Notification.show("Aircraft delete successfully");
                IndexedContainer container = (IndexedContainer) airCraftDetailsTable.getContainerDataSource();
                container.removeItem(target);
            }
            else {
                Notification.show("Something wrong, please try again");
            }
        }
    }

    private void fillEditDetails(Object target){
        if(target != null) {
            IndexedContainer container = (IndexedContainer) airCraftDetailsTable.getContainerDataSource();
            Item item = container.getItem(target);
            aircraftType.setValue(item.getItemProperty(AIRCRAFT_NAME).getValue().toString());
            galleyPosition.setValue(item.getItemProperty(GALLEY_POSITION).getValue() != null ?
                    item.getItemProperty(GALLEY_POSITION).getValue().toString() : "");
            equipmentType.setValue(item.getItemProperty(EQUIPMENT_TYPE).getValue());
            noOfFullCarts.setValue(item.getItemProperty(NO_OF_EQUIPMENTS).getValue().toString());
            totalNoOfSeats.setValue(item.getItemProperty(TOTLA_NO_OF_SEATS).getValue() != null ?
                    item.getItemProperty(TOTLA_NO_OF_SEATS).getValue().toString() : "");
            activeCheckBox.setValue("Active".equals(item.getItemProperty(STATUS).getValue()));
            aircraftId.setValue(target.toString());
            addButton.setCaption("Edit");
        }
    }

    private void insertFlightDetails(){
        String aircraftName = aircraftType.getValue();
        if(aircraftName == null || aircraftName.isEmpty()){
            Notification.show("Enter Aircraft Name");
            aircraftType.focus();
            return;
        }
        String galleyPositionVal = galleyPosition.getValue();
        String equipmentTypeVal = equipmentType.getValue().toString();
        String noOfCartsVal = noOfFullCarts.getValue();
        if(noOfCartsVal == null || noOfCartsVal.isEmpty() || !BackOfficeUtils.isInteger(noOfCartsVal)){
            Notification.show("No of full carts value is incorrect");
            noOfFullCarts.focus();
            return;
        }
        int numOfSeats = 0;
        String totalNoOfSeatsVal = totalNoOfSeats.getValue();
        if(totalNoOfSeats != null && !totalNoOfSeatsVal.isEmpty()){
            if(!BackOfficeUtils.isInteger(totalNoOfSeatsVal)) {
                Notification.show("total number of seats value is incorrect");
                totalNoOfSeats.focus();
                return;
            }
            else{
                numOfSeats = Integer.parseInt(totalNoOfSeatsVal);
            }
        }
        String val = aircraftId.getValue();
        int aircraftIdVal = (val != null && !val.isEmpty())? Integer.parseInt(val) : 0;
        Boolean isActive = activeCheckBox.getValue();
        AircraftDetails flightDetails = new AircraftDetails();
        flightDetails.setAircraftId(aircraftIdVal);
        flightDetails.setActive(isActive);
        flightDetails.setAircraftName(aircraftName);
        flightDetails.setEquipmentType(equipmentTypeVal);
        flightDetails.setGalleyPosition(galleyPositionVal);
        flightDetails.setNoOfFullCrts(Integer.parseInt(noOfCartsVal));
        flightDetails.setTotalNoOfSeats(numOfSeats);
        flightDetails.setUpdateDateAndtime(BackOfficeUtils.getCurrentDateAndTime());
        if(addButton.getCaption().equals("Add")) {
            int newId = connection.insertObjectHBM(flightDetails);
            if (newId != 0) {
                Notification.show("Aircraft added successfully");
                updateFlightDetailsTable(false,flightDetails,newId);
                resetFields();
            } else {
                Notification.show("Something wrong, please try again");
            }
        }
        else{
            connection.updateObjectHBM(flightDetails);
            Notification.show("Aircraft updated successfully");
            updateFlightDetailsTable(true,flightDetails,0);
            addButton.setCaption("Add");
            resetFields();
        }
    }

    private void updateFlightDetailsTable(boolean isEdit , AircraftDetails flightDetails, int newId){
        IndexedContainer container = (IndexedContainer) airCraftDetailsTable.getContainerDataSource();
        Item item;
        if(isEdit){
            item  = container.getItem(flightDetails.getAircraftId());
        }
        else{
            item  = container.addItem(newId);
        }
        item.getItemProperty(AIRCRAFT_NAME).setValue(flightDetails.getAircraftName());
        item.getItemProperty(GALLEY_POSITION).setValue(flightDetails.getGalleyPosition());
        item.getItemProperty(EQUIPMENT_TYPE).setValue(flightDetails.getEquipmentType());
        item.getItemProperty(NO_OF_EQUIPMENTS).setValue(flightDetails.getNoOfFullCrts());
        if (flightDetails.getTotalNoOfSeats() != 0) {
            item.getItemProperty(TOTLA_NO_OF_SEATS).setValue(flightDetails.getTotalNoOfSeats());
        }
        String status = flightDetails.isActive() ? "Active" : "Not Active";
        item.getItemProperty(STATUS).setValue(status);
    }

    private IndexedContainer generateContainer(){
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(AIRCRAFT_NAME, String.class, null);
        container.addContainerProperty(GALLEY_POSITION, String.class, null);
        container.addContainerProperty(EQUIPMENT_TYPE, String.class, null);
        container.addContainerProperty(NO_OF_EQUIPMENTS, Integer.class, null);
        container.addContainerProperty(TOTLA_NO_OF_SEATS, Integer.class, null);
        container.addContainerProperty(STATUS, String.class, null);
        List<AircraftDetails> flightDetailsList = connection.getAllFlights();
        for(AircraftDetails details : flightDetailsList){
            Item item = container.addItem(details.getAircraftId());
            item.getItemProperty(AIRCRAFT_NAME).setValue(details.getAircraftName());
            item.getItemProperty(GALLEY_POSITION).setValue(details.getGalleyPosition());
            item.getItemProperty(EQUIPMENT_TYPE).setValue(details.getEquipmentType());
            item.getItemProperty(NO_OF_EQUIPMENTS).setValue(details.getNoOfFullCrts());
            int seatCount = details.getTotalNoOfSeats();
            if(seatCount != 0) {
                item.getItemProperty(TOTLA_NO_OF_SEATS).setValue(details.getTotalNoOfSeats());
            }
            String status = details.isActive() ? "Active" : "Not Active";
            item.getItemProperty(STATUS).setValue(status);
        }
        return container;
    }

    private void resetFields(){
        aircraftType.clear();
        galleyPosition.clear();
        totalNoOfSeats.clear();
        noOfFullCarts.clear();
    }
}
