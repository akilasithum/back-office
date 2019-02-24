package com.back.office.ui;

import com.back.office.entity.AircraftDetails;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

public class AirCraftTypeView extends CommonPageDetails {

    TextField aircraftType;
    ComboBox galleyPosition;
    TextField registrationNumberFld;
    ComboBox equipmentType;
    TextField noOfFullCarts;
    TextField totalNoOfSeats;
    CheckBox activeCheckBox;
    ComboBox economyClassSeatsComboBox;
    ComboBox businessClassSeatsComboBox;
    ComboBox fullCartsComboBox;
    ComboBox halfCartsComboBox;
    ComboBox containersComboBox;
    HorizontalLayout imageLayout;
    VerticalLayout galleyTypeLayout;

    private final String AIRCRAFT_NAME = "Aircraft Type";
    private final String GALLEY_POSITION = "Galley Type";
    private final String REG_NO = "Registration Number";
    private final String STATUS = "Status";
    private final String ECONOMY_CLASS = "Economy";
    private final String BUSINESS_CLASS = "Business";
    private final String FULL_CARTS = "Full Carts";
    private final String HALF_CARTS = "Half Carts";
    private final String CONTAINERS = "Containers";

    FilterGrid<AircraftDetails> aircraftDetailsGrid;
    List<AircraftDetails> aircraftDetailsList;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    public AirCraftTypeView(){
        super();
    }

    @Override
    protected void createMainLayout(){
        super.createMainLayout();

        imageLayout = new HorizontalLayout();
        imageLayout.setSizeFull();
        imageLayout.setMargin(Constants.leftMargin);
        outerLayout.addComponent(imageLayout);
        outerLayout.setExpandRatio(userFormLayout,0.7f);
        outerLayout.setExpandRatio(imageLayout,0.3f);
        showAirCraftImage();

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.setSizeFull();
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(firstRow);

        registrationNumberFld = new TextField(REG_NO);
        registrationNumberFld.setDescription(REG_NO);
        registrationNumberFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(registrationNumberFld);
        registrationNumberFld.addValueChangeListener(valueChangeEvent -> {
            isKeyFieldDirty = true;
        });

        aircraftType = new TextField(AIRCRAFT_NAME);
        aircraftType.setRequiredIndicatorVisible(true);
        aircraftType.setDescription("Name of the aircraft");
        firstRow.addComponent(aircraftType);

        firstRow.addComponent(getSeatNoLayout());

        HorizontalLayout layout = new HorizontalLayout();
        MarginInfo topMarginInfo = new MarginInfo(true,true,true,false);
        layout.setMargin(topMarginInfo);
        layout.setSizeFull();
        activeCheckBox = new CheckBox("Active", true);
        layout.addComponent(activeCheckBox);
        firstRow.addComponent(layout);

        galleyTypeLayout = new VerticalLayout();
        galleyTypeLayout.setMargin(Constants.noMargin);
        galleyTypeLayout.setSizeFull();
        galleyTypeLayout.setCaption("Galley Types");
        mainUserInputLayout.addComponent(galleyTypeLayout);

        galleyPosition = new ComboBox(GALLEY_POSITION);
        galleyPosition.setDescription(GALLEY_POSITION);
        galleyPosition.setItems("Front","Middle","Rear");
        galleyPosition.setRequiredIndicatorVisible(true);
        galleyPosition.setEmptySelectionAllowed(false);

        addGalleyTypes("Front");
        addGalleyTypes("Middle");
        addGalleyTypes("Rear");

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        //mainUserInputLayout.addComponent(secondRow);

        fullCartsComboBox = new ComboBox(FULL_CARTS);
        fullCartsComboBox.setDescription(FULL_CARTS);
        fullCartsComboBox.setRequiredIndicatorVisible(true);
        fullCartsComboBox.setItems(getNumberList(20));

        halfCartsComboBox = new ComboBox(HALF_CARTS);
        halfCartsComboBox.setDescription(HALF_CARTS);
        halfCartsComboBox.setRequiredIndicatorVisible(true);
        halfCartsComboBox.setItems(getNumberList(20));

        containersComboBox = new ComboBox(CONTAINERS);
        containersComboBox.setDescription(CONTAINERS);
        containersComboBox.setRequiredIndicatorVisible(true);
        containersComboBox.setItems(getNumberList(20));


        //secondRow.addComponents(fullCartsComboBox,halfCartsComboBox,containersComboBox);

        HorizontalLayout row = new HorizontalLayout();
        row.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        row.setSpacing(true);
        addComponent(row);

        aircraftDetailsGrid = new FilterGrid<>();
        aircraftDetailsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        aircraftDetailsGrid.setSizeFull();
        tableLayout.addComponent(aircraftDetailsGrid);
        setDataInGrid();

        GridContextMenu<AircraftDetails> gridMenu = new GridContextMenu<>(aircraftDetailsGrid);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);

        mainTableLayout.setWidth("85%");
        headerLayout.setWidth("85%");
    }

    private void addGalleyTypes(String type){
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(Constants.noMargin);
        layout.setSizeFull();

        Label label = new Label(type);
        label.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(label);
        List<String> list = new ArrayList<>();
        list.add("Full Carts");
        list.add("Half Carts");
        list.add("Containers");
        for(String cartType : list){
            ComboBox comboBox = new ComboBox(cartType);
            comboBox.setDescription(cartType);
            comboBox.setRequiredIndicatorVisible(true);
            comboBox.setItems(getNumberList(20));
            comboBox.setValue(0);
            layout.addComponent(comboBox);
        }
        galleyTypeLayout.addComponent(layout);


    }

    private HorizontalLayout getSeatNoLayout(){
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        layout.setMargin(Constants.noMargin);
        businessClassSeatsComboBox = new ComboBox(BUSINESS_CLASS);
        businessClassSeatsComboBox.setItems(getNumberList(20));
        businessClassSeatsComboBox.setSizeFull();
        economyClassSeatsComboBox = new ComboBox(ECONOMY_CLASS);
        economyClassSeatsComboBox.setItems(getNumberList(300));
        economyClassSeatsComboBox.setSizeFull();
        economyClassSeatsComboBox.setRequiredIndicatorVisible(true);
        layout.addComponents(businessClassSeatsComboBox,economyClassSeatsComboBox);
        layout.setExpandRatio(businessClassSeatsComboBox,0.5f);
        layout.setExpandRatio(economyClassSeatsComboBox,0.5f);
        layout.setStyleName("layout-with-border");
        return layout;
    }

    private List<Integer> getNumberList(int number){
        List<Integer> list = new ArrayList<>();
        for(int i = 0;i<=number;i++){
            list.add(i);
        }
        return list;
    }

    private void showAirCraftImage(){
        Image logo = new Image();
        logo.setHeight(200,Unit.PIXELS);
        logo.setWidth(200,Unit.PIXELS);
        logo.setSource(new ClassResource("aircraft.png"));
        imageLayout.addComponent(logo);
    }

    protected void deleteItem(Object target) {
        if (target != null) {
            AircraftDetails aircraftDetail = (AircraftDetails) target;
            boolean success = connection.deleteObjectHBM(aircraftDetail.getAircraftId(), className);
            if (success) {
                BackOfficeUtils.showNotification("Success", "Detail delete successfully", VaadinIcons.CHECK_CIRCLE_O);
                aircraftDetailsList.remove(target);
                aircraftDetailsGrid.setItems(aircraftDetailsList);
            } else {
                BackOfficeUtils.showNotification("Error", "Something wrong, please try again", VaadinIcons.CLOSE);
            }
        }
    }

    private void setDataInGrid(){
        aircraftDetailsList = connection.getAllFlights();
        aircraftDetailsGrid.setItems(aircraftDetailsList);
        aircraftDetailsGrid.addColumn(AircraftDetails::getRegistrationNumber).setCaption(REG_NO).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        aircraftDetailsGrid.addColumn(AircraftDetails::getAircraftName).setCaption(AIRCRAFT_NAME).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        aircraftDetailsGrid.addColumn(AircraftDetails::getBusinessClassSeatCount).setCaption(BUSINESS_CLASS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        aircraftDetailsGrid.addColumn(AircraftDetails::getEcoClassSeatCount).setCaption(ECONOMY_CLASS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        /*aircraftDetailsGrid.addColumn(AircraftDetails::getGalleyPosition).setCaption(GALLEY_POSITION).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());*/
        aircraftDetailsGrid.addColumn(AircraftDetails::getFrontFullCarts).setCaption("Front " + FULL_CARTS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        aircraftDetailsGrid.addColumn(AircraftDetails::getFrontHalfCarts).setCaption("Front "+HALF_CARTS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        aircraftDetailsGrid.addColumn(AircraftDetails::getFrontContainers).setCaption("Front "+CONTAINERS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        aircraftDetailsGrid.addColumn(AircraftDetails::getMiddleFullCarts).setCaption("Half " + FULL_CARTS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        aircraftDetailsGrid.addColumn(AircraftDetails::getMiddleHalfCarts).setCaption("Half "+HALF_CARTS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        aircraftDetailsGrid.addColumn(AircraftDetails::getMiddleContainers).setCaption("Half "+CONTAINERS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        aircraftDetailsGrid.addColumn(AircraftDetails::getRearFullCarts).setCaption("Rear " + FULL_CARTS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        aircraftDetailsGrid.addColumn(AircraftDetails::getRearHalfCarts).setCaption("Rear "+HALF_CARTS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        aircraftDetailsGrid.addColumn(AircraftDetails::getRearContainers).setCaption("Rear "+CONTAINERS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());

        aircraftDetailsGrid.addColumn(bean->bean.isActive() ? "Active" : "Not Active").setCaption(STATUS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
    }

    @Override
    protected void fillEditDetails(Object target){
        if(target != null) {
            AircraftDetails aircraftDetail = (AircraftDetails) target;
            editObj = aircraftDetail;
            aircraftType.setValue(aircraftDetail.getAircraftName());
            galleyPosition.setValue(aircraftDetail.getGalleyPosition() != null ?
                    aircraftDetail.getGalleyPosition() : "");
            registrationNumberFld.setValue(aircraftDetail.getRegistrationNumber());
            economyClassSeatsComboBox.setValue(aircraftDetail.getEcoClassSeatCount());
            businessClassSeatsComboBox.setValue(aircraftDetail.getBusinessClassSeatCount());

            /*fullCartsComboBox.setValue(aircraftDetail.getFullCartsCount());
            halfCartsComboBox.setValue(aircraftDetail.getHalfCartsCount());
            containersComboBox.setValue(aircraftDetail.getContainerCount());*/
            HorizontalLayout frontGalleyLayout = (HorizontalLayout)galleyTypeLayout.getComponent(0);
            ((ComboBox)frontGalleyLayout.getComponent(1)).setValue(aircraftDetail.getFrontFullCarts());
            ((ComboBox)frontGalleyLayout.getComponent(2)).setValue(aircraftDetail.getFrontHalfCarts());
            ((ComboBox)frontGalleyLayout.getComponent(3)).setValue(aircraftDetail.getFrontContainers());

            HorizontalLayout middleGalleyLayout = (HorizontalLayout)galleyTypeLayout.getComponent(1);
            ((ComboBox)middleGalleyLayout.getComponent(1)).setValue(aircraftDetail.getMiddleFullCarts());
            ((ComboBox)middleGalleyLayout.getComponent(2)).setValue(aircraftDetail.getMiddleHalfCarts());
            ((ComboBox)middleGalleyLayout.getComponent(3)).setValue(aircraftDetail.getMiddleContainers());

            HorizontalLayout rearGalleyLayout = (HorizontalLayout)galleyTypeLayout.getComponent(2);
            ((ComboBox)rearGalleyLayout.getComponent(1)).setValue(aircraftDetail.getRearFullCarts());
            ((ComboBox)rearGalleyLayout.getComponent(2)).setValue(aircraftDetail.getRearHalfCarts());
            ((ComboBox)rearGalleyLayout.getComponent(3)).setValue(aircraftDetail.getRearContainers());

            activeCheckBox.setValue(aircraftDetail.isActive());
            idField.setValue(String.valueOf(aircraftDetail.getAircraftId()));
            isKeyFieldDirty = false;
            addButton.setCaption("Save");
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = AIRCRAFT_NAME;
        this.pageHeader = "Aircraft Details";
        this.className = "com.back.office.entity.AircraftDetails";
        this.keyFieldDBName = "registrationNumber";
    }

    @Override
    protected void insertDetails(){
        String isValidated = validateFields();
        if(isValidated != null){
            Notification.show(isValidated, Notification.Type.WARNING_MESSAGE);
            return;
        }
        String aircraftName = aircraftType.getValue();
        if(aircraftName == null || aircraftName.isEmpty()){
            Notification.show("Enter Aircraft Type", Notification.Type.WARNING_MESSAGE);
            aircraftType.focus();
            return;
        }
        String galleyPositionVal = String.valueOf(galleyPosition.getValue());
        String registrationNumber = registrationNumberFld.getValue();

        int numOfSeats = 0;
        String ecoClassCount = String.valueOf(economyClassSeatsComboBox.getValue());
        if(ecoClassCount != null && !ecoClassCount.isEmpty()){
                numOfSeats = Integer.parseInt(ecoClassCount);
        }
        int aircraftIdVal = (idField.getValue() == null || idField.getValue().isEmpty()) ? 0 : Integer.parseInt(idField.getValue());
        Boolean isActive = activeCheckBox.getValue();
        AircraftDetails flightDetails = new AircraftDetails();
        flightDetails.setAircraftId(aircraftIdVal);
        flightDetails.setActive(isActive);
        flightDetails.setAircraftName(aircraftName);
        flightDetails.setRegistrationNumber(registrationNumber);
        flightDetails.setGalleyPosition(galleyPositionVal);

        flightDetails.setEcoClassSeatCount(businessClassSeatsComboBox.getValue() != null ? (Integer) businessClassSeatsComboBox.getValue() : 0);
        flightDetails.setBusinessClassSeatCount(numOfSeats);

        HorizontalLayout frontGalleyLayout = (HorizontalLayout)galleyTypeLayout.getComponent(0);
        flightDetails.setFrontFullCarts(((Integer)((ComboBox)frontGalleyLayout.getComponent(1)).getValue()));
        flightDetails.setFrontHalfCarts(((Integer)((ComboBox)frontGalleyLayout.getComponent(2)).getValue()));
        flightDetails.setFrontContainers(((Integer)((ComboBox)frontGalleyLayout.getComponent(3)).getValue()));

        HorizontalLayout middleGalleyLayout = (HorizontalLayout)galleyTypeLayout.getComponent(1);
        flightDetails.setMiddleFullCarts(((Integer)((ComboBox)middleGalleyLayout.getComponent(1)).getValue()));
        flightDetails.setMiddleHalfCarts(((Integer)((ComboBox)middleGalleyLayout.getComponent(2)).getValue()));
        flightDetails.setMiddleContainers(((Integer)((ComboBox)middleGalleyLayout.getComponent(3)).getValue()));

        HorizontalLayout rearGalleyLayout = (HorizontalLayout)galleyTypeLayout.getComponent(2);
        flightDetails.setRearFullCarts(((Integer)((ComboBox)rearGalleyLayout.getComponent(1)).getValue()));
        flightDetails.setRearHalfCarts(((Integer)((ComboBox)rearGalleyLayout.getComponent(2)).getValue()));
        flightDetails.setRearContainers(((Integer)((ComboBox)rearGalleyLayout.getComponent(3)).getValue()));

        flightDetails.setUpdateDateAndtime(BackOfficeUtils.getCurrentDateAndTime());
        if(addButton.getCaption().equals("Add")) {
            int newId = connection.insertObjectHBM(flightDetails);
            if (newId != 0) {
                BackOfficeUtils.showNotification("Success","Aircraft added successfully",VaadinIcons.CHECK_CIRCLE_O);
                updateTable(false,flightDetails,newId);
                resetFields();
            } else {
                BackOfficeUtils.showNotification("Error","Something wrong, please try again",VaadinIcons.CLOSE);
            }
        }
        else{
            connection.updateObjectHBM(flightDetails);
            connection.updateRecordStatus(aircraftIdVal,className);
            Notification.show("Aircraft updated successfully");
            updateTable(true,flightDetails,0);
            addButton.setCaption("Add");
            resetFields();
        }
    }
    @Override
    protected void updateTable(boolean isEdit , Object object, int newId){
        AircraftDetails flightDetails = (AircraftDetails) object;
        if(isEdit){
            int index = aircraftDetailsList.indexOf(editObj);
            aircraftDetailsList.remove(editObj);
            aircraftDetailsList.add(index,flightDetails);
        }
        else{
            aircraftDetailsList.add(flightDetails);
        }
        aircraftDetailsGrid.setItems(aircraftDetailsList);
    }

    @Override
    protected void resetFields(){
        aircraftType.clear();
        galleyPosition.clear();
        registrationNumberFld.clear();
        economyClassSeatsComboBox.clear();
        businessClassSeatsComboBox.clear();
        for(int i = 0;i<3;i++){
            HorizontalLayout layout = (HorizontalLayout)galleyTypeLayout.getComponent(i);
            for(int j = 1;j<4;j++) {
                ((ComboBox) layout.getComponent(j)).setValue(0);
            }
        }
    }

    @Override
    protected TextField getKeyField() {
        return registrationNumberFld;
    }

    @Override
    protected String validateFields(){
        if(keyFieldValues.contains(getKeyField().getValue()) && isKeyFieldDirty){
            getKeyField().focus();
            return filterFieldStr+" already used. Please use another value";
        }
        if(aircraftType.getValue() == null || aircraftType.getValue().isEmpty()) return "Aircraft Type is mandatory";
        if(economyClassSeatsComboBox.getValue() == null || String.valueOf(economyClassSeatsComboBox.getValue()).isEmpty())
            return "Economy class seat count is mandatory";
        return null;
    }
}
