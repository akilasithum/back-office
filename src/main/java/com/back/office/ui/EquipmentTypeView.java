package com.back.office.ui;

import com.back.office.entity.EquipmentDetails;
import com.back.office.entity.KitCodes;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;

public class EquipmentTypeView extends CommonPageDetails {

    private final String PACK_TYPE = "Pack Type";
    private final String PACK_DESC = "Pack Description";
    private final String EQUIPMENT_TYPE = "Equipment Type";
    private final String NO_OF_DRAWERS = "No of Drawers";
    private final String KIT_CODE = "Kit Code";

    protected TextField packTypeFld;
    protected TextField packDescFld;
    protected ComboBox equipmentTypeFld;
    protected TextField noOfDrawersFld;
    private ComboBox kitCodeFld;



    public EquipmentTypeView(){
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

        packTypeFld = new TextField(PACK_TYPE);
        packTypeFld.setInputPrompt(PACK_TYPE);
        packTypeFld.setRequired(true);
        firstRow.addComponent(packTypeFld);

        packDescFld = new TextField(PACK_DESC);
        packDescFld.setInputPrompt(PACK_DESC);
        firstRow.addComponent(packDescFld);

        equipmentTypeFld = new ComboBox(EQUIPMENT_TYPE);
        equipmentTypeFld.setInputPrompt(EQUIPMENT_TYPE);
        equipmentTypeFld.addItem("Half Cart");
        equipmentTypeFld.addItem("Full Cart");
        equipmentTypeFld.addItem("Containers");
        equipmentTypeFld.setNullSelectionAllowed(false);
        equipmentTypeFld.setRequired(true);
        firstRow.addComponent(equipmentTypeFld);


        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        MarginInfo marginInfo = new MarginInfo(true,false,true,false);
        secondRow.setMargin(marginInfo);
        mainUserInputLayout.addComponent(secondRow);

        noOfDrawersFld = new TextField(NO_OF_DRAWERS);
        noOfDrawersFld.setInputPrompt(NO_OF_DRAWERS);
        noOfDrawersFld.setRequired(true);
        secondRow.addComponent(noOfDrawersFld);

        kitCodeFld = new ComboBox(KIT_CODE);
        kitCodeFld.addItems(getKitCodesList());
        kitCodeFld.setNullSelectionAllowed(false);
        kitCodeFld.setRequired(true);
        secondRow.addComponent(kitCodeFld);
        userFormLayout.setWidth("60%");
        mainTableLayout.setWidth("70%");
        headerLayout.setWidth("70%");
    }

    @Override
    protected IndexedContainer generateContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(PACK_TYPE, String.class, null);
        container.addContainerProperty(PACK_DESC, String.class, null);
        container.addContainerProperty(EQUIPMENT_TYPE, String.class, null);
        container.addContainerProperty(NO_OF_DRAWERS, Integer.class, null);
        container.addContainerProperty(KIT_CODE, String.class, null);

        List<EquipmentDetails> equipmentDetails = (List<EquipmentDetails>)connection.getAllValues(className);
        for(EquipmentDetails details : equipmentDetails){
            Item item = container.addItem(details.getEquipmentId());
            item.getItemProperty(PACK_TYPE).setValue(details.getPackType());
            item.getItemProperty(PACK_DESC).setValue(details.getPackDescription());
            item.getItemProperty(EQUIPMENT_TYPE).setValue(details.getEquipmentType());
            item.getItemProperty(NO_OF_DRAWERS).setValue(details.getNoOfDrawers());
            item.getItemProperty(KIT_CODE).setValue(details.getKitCode());
        }
        return container;
    }

    @Override
    protected void insertDetails() {
        String isValidated = validateFields();
        if(isValidated != null){
            Notification.show(isValidated);
        }
        else{
            int itemIdVal = (idField.getValue() == null || idField.getValue().isEmpty()) ? 0 : Integer.parseInt(idField.getValue());
            EquipmentDetails itemDetails = new EquipmentDetails();
            itemDetails.setEquipmentId(itemIdVal);
            itemDetails.setKitCode(kitCodeFld.getValue().toString());
            itemDetails.setEquipmentType(equipmentTypeFld.getValue().toString());
            itemDetails.setNoOfDrawers(Integer.parseInt(noOfDrawersFld.getValue()));
            itemDetails.setPackDescription(packDescFld.getValue());
            itemDetails.setPackType(packTypeFld.getValue());
            addOrUpdateDetails(itemDetails);

        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
            Item item = container.getItem(target);
            idField.setValue(target.toString());
            kitCodeFld.setValue(item.getItemProperty(KIT_CODE).getValue().toString());
            packTypeFld.setValue(item.getItemProperty(PACK_TYPE).getValue().toString());
            packDescFld.setValue(item.getItemProperty(PACK_DESC).getValue().toString());
            noOfDrawersFld.setValue(item.getItemProperty(NO_OF_DRAWERS).getValue().toString());
            equipmentTypeFld.setValue(item.getItemProperty(EQUIPMENT_TYPE).getValue().toString());
            addButton.setCaption("Edit");
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = PACK_TYPE;
        this.pageHeader = "Equipment Details";
        this.className = "com.back.office.entity.EquipmentDetails";
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        EquipmentDetails itemDetails = (EquipmentDetails) details;
        IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
        Item item;
        if(isEdit){
            item  = container.getItem(itemDetails.getEquipmentId());
        }
        else{
            item  = container.addItem(newId);
        }
        item.getItemProperty(KIT_CODE).setValue(itemDetails.getKitCode());
        item.getItemProperty(PACK_TYPE).setValue(itemDetails.getPackType());
        item.getItemProperty(PACK_DESC).setValue(itemDetails.getPackDescription());
        item.getItemProperty(EQUIPMENT_TYPE).setValue(itemDetails.getEquipmentType());
        item.getItemProperty(NO_OF_DRAWERS).setValue(itemDetails.getNoOfDrawers());
    }

    private List<String> getKitCodesList(){
        List<KitCodes> kitCodesList = connection.getAllKitCodes();
        List<String> kitCodeList = new ArrayList<>();
        for(KitCodes kit : kitCodesList){
            kitCodeList.add(kit.getKitCode());
        }
        return kitCodeList;
    }
}
