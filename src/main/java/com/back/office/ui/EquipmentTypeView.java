package com.back.office.ui;

import com.back.office.entity.AircraftDetails;
import com.back.office.entity.EquipmentDetails;
import com.back.office.entity.KitCodes;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FileResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EquipmentTypeView extends CommonPageDetails {

    private final String PACK_TYPE = "Pack Type";
    private final String PACK_DESC = "Pack Description";
    private final String EQUIPMENT_TYPE = "Equipment Type";
    private final String NO_OF_DRAWERS = "No of Drawers";
    private final String KIT_CODE = "Kit Code";
    private final String NO_OF_SEALS = "No of Seals";

    protected TextField packTypeFld;
    protected TextField packDescFld;
    protected ComboBox equipmentTypeFld;
    protected TextField noOfDrawersFld;
    protected TextField noOfSealsFld;
    protected HorizontalLayout imageLayout;

    FilterGrid<EquipmentDetails> equipmentDetailsGrid;
    List<EquipmentDetails> equipmentDetailsList;

    public EquipmentTypeView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();

        imageLayout = new HorizontalLayout();
        imageLayout.setMargin(Constants.noMargin);
        imageLayout.setSizeFull();
        outerLayout.addComponent(imageLayout);
        outerLayout.setExpandRatio(userFormLayout,0.6f);
        outerLayout.setExpandRatio(imageLayout,0.4f);

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(firstRow);

        equipmentTypeFld = new ComboBox(EQUIPMENT_TYPE);
        equipmentTypeFld.setDescription(EQUIPMENT_TYPE);
        equipmentTypeFld.setItems("Half Cart","Full Cart","Containers");
        equipmentTypeFld.setEmptySelectionAllowed(false);
        equipmentTypeFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(equipmentTypeFld);
        equipmentTypeFld.addValueChangeListener(valueChangeEvent -> {
            String val = String.valueOf(valueChangeEvent.getValue());
            showCartImage(val);
        });

        packTypeFld = new TextField(PACK_TYPE);
        packTypeFld.setDescription(PACK_TYPE);
        packTypeFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(packTypeFld);
        packTypeFld.addValueChangeListener(valueChangeEvent -> {
            isKeyFieldDirty = true;
        });

        packDescFld = new TextField(PACK_DESC);
        packDescFld.setDescription(PACK_DESC);
        firstRow.addComponent(packDescFld);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        secondRow.setMargin(Constants.noMargin);
        secondRow.setWidth(66.67f,Unit.PERCENTAGE);
        mainUserInputLayout.addComponent(secondRow);

        noOfDrawersFld = new TextField(NO_OF_DRAWERS);
        noOfDrawersFld.setDescription(NO_OF_DRAWERS);
        noOfDrawersFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(noOfDrawersFld);

        /*kitCodeFld = new ComboBox(KIT_CODE);
        kitCodeFld.setItems(getKitCodesList());
        kitCodeFld.setEmptySelectionAllowed(false);
        kitCodeFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(kitCodeFld);*/

        noOfSealsFld = new TextField(NO_OF_SEALS);
        noOfSealsFld.setDescription(NO_OF_SEALS);
        noOfSealsFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(noOfSealsFld);

        equipmentDetailsGrid = new FilterGrid<>();
        equipmentDetailsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        equipmentDetailsGrid.setSizeFull();
        tableLayout.addComponent(equipmentDetailsGrid);
        setDataInGrid();
        GridContextMenu<EquipmentDetails> gridMenu = new GridContextMenu<>(equipmentDetailsGrid);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);

        mainTableLayout.setWidth("70%");
        headerLayout.setWidth("70%");
    }

    protected void deleteItem(Object target) {
        if (target != null) {
            EquipmentDetails aircraftDetail = (EquipmentDetails) target;
            boolean success = connection.deleteObjectHBM(aircraftDetail.getEquipmentId(), className);
            if (success) {
                BackOfficeUtils.showNotification("Success", "Equipment type delete successfully", VaadinIcons.CHECK_CIRCLE_O);
                equipmentDetailsList.remove(target);
                equipmentDetailsGrid.setItems(equipmentDetailsList);
            } else {
                BackOfficeUtils.showNotification("Error", "Something wrong, please try again", VaadinIcons.CLOSE);
            }
        }
    }

    private void setDataInGrid(){
        equipmentDetailsList = (List<EquipmentDetails>)connection.getAllValues(className);
        equipmentDetailsGrid.setItems(equipmentDetailsList);
        equipmentDetailsGrid.addColumn(EquipmentDetails::getEquipmentType).setCaption(EQUIPMENT_TYPE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        equipmentDetailsGrid.addColumn(EquipmentDetails::getPackType).setCaption(PACK_TYPE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        equipmentDetailsGrid.addColumn(EquipmentDetails::getPackDescription).setCaption(PACK_DESC).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        equipmentDetailsGrid.addColumn(EquipmentDetails::getNoOfDrawers).setCaption(NO_OF_DRAWERS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        equipmentDetailsGrid.addColumn(EquipmentDetails::getNoOfSeals).setCaption(NO_OF_SEALS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
    }

    @Override
    protected void insertDetails() {
        String isValidated = validateFields();
        if(isValidated != null){
            Notification.show(isValidated, Notification.Type.WARNING_MESSAGE);
        }
        else{
            int itemIdVal = (idField.getValue() == null || idField.getValue().isEmpty()) ? 0 : Integer.parseInt(idField.getValue());
            EquipmentDetails itemDetails = new EquipmentDetails();
            itemDetails.setEquipmentId(itemIdVal);
            itemDetails.setEquipmentType(equipmentTypeFld.getValue().toString());
            itemDetails.setNoOfDrawers(Integer.parseInt(noOfDrawersFld.getValue()));
            itemDetails.setPackDescription(packDescFld.getValue());
            itemDetails.setPackType(packTypeFld.getValue());
            itemDetails.setNoOfSeals(Integer.parseInt(noOfSealsFld.getValue()));
            addOrUpdateDetails(itemDetails);

        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            EquipmentDetails equipmentDetails = (EquipmentDetails) target;
            idField.setValue(String.valueOf(equipmentDetails.getEquipmentId()));
            packTypeFld.setValue(equipmentDetails.getPackType());
            packDescFld.setValue(equipmentDetails.getPackDescription());
            noOfDrawersFld.setValue(String.valueOf(equipmentDetails.getNoOfDrawers()));
            equipmentTypeFld.setValue(equipmentDetails.getEquipmentType());
            noOfSealsFld.setValue(String.valueOf(equipmentDetails.getNoOfSeals()));
            addButton.setCaption("Save");
            editObj = equipmentDetails;
            isKeyFieldDirty = false;
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = PACK_TYPE;
        this.pageHeader = "Equipment Details";
        this.className = "com.back.office.entity.EquipmentDetails";
        this.keyFieldDBName = "packType";
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        EquipmentDetails equipment = (EquipmentDetails) details;
        if(isEdit){
            int index = equipmentDetailsList.indexOf(editObj);
            equipmentDetailsList.remove(editObj);
            equipmentDetailsList.add(index,equipment);
        }
        else{
            equipmentDetailsList.add(equipment);
        }
        equipmentDetailsGrid.setItems(equipmentDetailsList);
    }

    @Override
    protected TextField getKeyField() {
        return packTypeFld;
    }

    private List<String> getKitCodesList(){
        List<KitCodes> kitCodesList = connection.getAllKitCodes();
        List<String> kitCodeList = new ArrayList<>();
        for(KitCodes kit : kitCodesList){
            kitCodeList.add(kit.getKitCode());
        }
        return kitCodeList;
    }

    private void showCartImage(String type){
        Image logo = new Image();
        logo.setSource(new ClassResource(type+".jpg"));
        imageLayout.removeAllComponents();
        imageLayout.addComponent(logo);
    }
}
