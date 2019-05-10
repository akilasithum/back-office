package com.back.office.ui.wizard;

import com.back.office.entity.AircraftDetails;
import com.back.office.entity.ItemDetails;
import com.back.office.ui.wizard.steps.aircraft.AirCraftFirstStep;
import com.back.office.ui.wizard.steps.aircraft.AircraftFrontGalleyStep;
import com.back.office.ui.wizard.steps.aircraft.AircraftMiddleGalleyStep;
import com.back.office.ui.wizard.steps.aircraft.AircraftRearGalleyStep;
import com.back.office.utils.Constants;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;

import static com.back.office.utils.BackOfficeUtils.getNumberList;

public class AircraftDetailsView extends WizardCommonView {

    FilterGrid<AircraftDetails> aircraftDetailsGrid;
    List<AircraftDetails> aircraftDetailsList;

    private final String AIRCRAFT_NAME = "Aircraft Type";
    private final String GALLEY_POSITION = "Galley Type";
    private final String REG_NO = "Registration Number";
    private final String STATUS = "Status";
    private final String ECONOMY_CLASS = "Economy";
    private final String BUSINESS_CLASS = "Business";
    private final String FULL_CARTS = "Full Carts";
    private final String HALF_CARTS = "Half Carts";
    private final String CONTAINERS = "Containers";

    TextField aircraftType;
    TextField registrationNumberFld;
    ComboBox economyClassSeatsComboBox;
    ComboBox businessClassSeatsComboBox;
    CheckBox activeCheckBox;

    public AircraftDetailsView(){
        super();
    }

    @Override
    protected void createMainLayout(){
        super.createMainLayout();
        aircraftDetailsGrid = new FilterGrid<>();
        aircraftDetailsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        aircraftDetailsGrid.setSizeFull();
        tableLayout.addComponent(aircraftDetailsGrid);
        setDataInGrid();
        aircraftDetailsGrid.addItemClickListener(itemClick -> {
            openViewEditWindow(itemClick.getItem());
        });
    }

    @Override
    protected void defineStringFields() {
        headerName = "Aircraft Details";
        objectName = "aircraft";
        wizardName = "Create Aircraft";
    }

    @Override
    protected void createItemWizard() {
        super.createWizard();
        wizard.addStep(new AirCraftFirstStep());
        wizard.addStep(new AircraftFrontGalleyStep());
        wizard.addStep(new AircraftMiddleGalleyStep());
        wizard.addStep(new AircraftRearGalleyStep());
        getUI().getUI().addWindow(window);
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

    private void openViewEditWindow(AircraftDetails item){

        Window detailsWindow = new Window("View Item");
        FormLayout formLayout = new FormLayout();
        editObj = item;

        TextField idFld = new TextField();
        idFld.setValue(String.valueOf(item.getAircraftId()));
        formLayout.addComponents(idFld);
        idFld.setVisible(false);

        registrationNumberFld = new TextField(REG_NO);
        registrationNumberFld.setRequiredIndicatorVisible(true);
        registrationNumberFld.setValue(item.getRegistrationNumber());
        formLayout.addComponent(registrationNumberFld);

        aircraftType = new TextField(AIRCRAFT_NAME);
        aircraftType.setRequiredIndicatorVisible(true);
        aircraftType.setValue(item.getAircraftName());
        formLayout.addComponent(aircraftType);

        businessClassSeatsComboBox = new ComboBox(BUSINESS_CLASS);
        businessClassSeatsComboBox.setItems(getNumberList(20));
        businessClassSeatsComboBox.setValue(String.valueOf(item.getBusinessClassSeatCount()));
        formLayout.addComponent(businessClassSeatsComboBox);

        economyClassSeatsComboBox = new ComboBox(ECONOMY_CLASS);
        economyClassSeatsComboBox.setItems(getNumberList(300));
        economyClassSeatsComboBox.setRequiredIndicatorVisible(true);
        economyClassSeatsComboBox.setValue(item.getEcoClassSeatCount());
        formLayout.addComponent(economyClassSeatsComboBox);

        activeCheckBox = new CheckBox("Active");
        activeCheckBox.setValue(item.isActive());
        formLayout.addComponent(activeCheckBox);

        List<Integer> typeList = new ArrayList<>();
        typeList.add(item.getFrontFullCarts());
        typeList.add(item.getFrontHalfCarts());
        typeList.add(item.getFrontContainers());
        addGalleyTypes("Galley Type - Front",formLayout,typeList);
        List<Integer> middleList = new ArrayList<>();
        middleList.add(item.getMiddleFullCarts());
        middleList.add(item.getMiddleHalfCarts());
        middleList.add(item.getMiddleContainers());
        addGalleyTypes("Galley Type - Middle",formLayout,middleList);
        List<Integer> rearList = new ArrayList<>();
        rearList.add(item.getRearFullCarts());
        rearList.add(item.getRearHalfCarts());
        rearList.add(item.getRearContainers());
        addGalleyTypes("Galley Type - Rear",formLayout,rearList);

        detailsWindow.setWidth("40%");
        detailsWindow.setHeight(500,Unit.PIXELS);
        detailsWindow.center();
        formLayout.setMargin(true);

        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.addComponents(editButton,deleteButton);
        formLayout.addComponent(btnLayout);

        detailsWindow.setContent(formLayout);
        enableDisableAllComponents(formLayout,false);
        getUI().addWindow(detailsWindow);

        editButton.addClickListener(clickEvent -> {

                    if (editButton.getCaption().equals("Edit")) {
                        enableDisableAllComponents(formLayout, true);
                        editButton.setCaption("Save");
                    }
                    else{
                        try {
                            AircraftDetails aircraft = new AircraftDetails();
                            aircraft.setRegistrationNumber(registrationNumberFld.getValue());
                            aircraft.setAircraftName(aircraftType.getValue());
                            aircraft.setBusinessClassSeatCount(Integer.parseInt(businessClassSeatsComboBox.getValue().toString()));
                            aircraft.setEcoClassSeatCount(Integer.parseInt(economyClassSeatsComboBox.getValue().toString()));
                            aircraft.setFrontFullCarts(Integer.parseInt(((ComboBox) formLayout.getComponent(7)).getValue().toString()));
                            aircraft.setFrontHalfCarts(Integer.parseInt(((ComboBox) formLayout.getComponent(8)).getValue().toString()));
                            aircraft.setFrontContainers(Integer.parseInt(((ComboBox) formLayout.getComponent(9)).getValue().toString()));

                            aircraft.setMiddleFullCarts(Integer.parseInt(((ComboBox) formLayout.getComponent(11)).getValue().toString()));
                            aircraft.setMiddleHalfCarts(Integer.parseInt(((ComboBox) formLayout.getComponent(12)).getValue().toString()));
                            aircraft.setMiddleContainers(Integer.parseInt(((ComboBox) formLayout.getComponent(13)).getValue().toString()));

                            aircraft.setRearFullCarts(Integer.parseInt(((ComboBox) formLayout.getComponent(15)).getValue().toString()));
                            aircraft.setRearHalfCarts(Integer.parseInt(((ComboBox) formLayout.getComponent(16)).getValue().toString()));
                            aircraft.setRearContainers(Integer.parseInt(((ComboBox) formLayout.getComponent(17)).getValue().toString()));
                            aircraft.setActive(activeCheckBox.getValue());
                            connection.updateRecordStatus(Integer.parseInt(idFld.getValue()),
                                    "com.back.office.entity.AircraftDetails");
                            connection.insertObjectHBM(aircraft);
                            updateTable(true, aircraft);
                            detailsWindow.close();
                        }
                        catch (Exception e){
                            Notification.show("Some fields are not specified or contain wrong values. Please check.", Notification.Type.WARNING_MESSAGE);
                        }
                    }
                });

        deleteButton.addClickListener(clickEvent -> {
            ConfirmDialog.show(getUI(), "Delete", "Are you sure you want to delete this Aircraft?",
                    "Yes", "No", new ConfirmDialog.Listener() {
                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                connection.deleteObjectHBM(item);
                                detailsWindow.close();
                                aircraftDetailsList.remove(editObj);
                                aircraftDetailsGrid.setItems(aircraftDetailsList);
                            }
                        }
                    });
        });
    }

    private void addGalleyTypes(String type,FormLayout layout,List<Integer> values){

        Label label = new Label(type);
        label.addStyleName(ValoTheme.LABEL_H3);
        layout.addComponent(label);
        List<String> list = new ArrayList<>();
        list.add("Full Carts");
        list.add("Half Carts");
        list.add("Containers");
        int i = 0;
        for(String cartType : list){
            ComboBox comboBox = new ComboBox(cartType);
            comboBox.setRequiredIndicatorVisible(true);
            comboBox.setItems(getNumberList(20));
            comboBox.setValue(values.get(i));
            layout.addComponent(comboBox);
            i++;
        }
    }

    @Override
    protected void updateTable(boolean isEdit , Object object){
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
}
