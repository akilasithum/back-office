package com.back.office.ui;

import com.back.office.entity.KitCodes;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;


public class KitCodesView extends CommonPageDetails {

    private final String KIT_CODE = "Kit Code";
    private final String DESCRIPTION = "Description";
    private final String SERVICE_TYPE = "Service Type";
    private final String ACTIVATE_DATE = "Activate Date";
    private final String NO_OF_SEALS = "No of Seals";
    private final String NO_OF_EQUIPMENTS = "No of Equipments";

    protected TextField kitCodeFld;
    protected TextField descFld;
    //protected ComboBoxMultiselect serviceTypeFld;
    protected ComboBox serviceTypeFld;
    protected DateField activateDateFld;
    protected TextField noOfSealsFld;
    protected TextField noOfEqFld;

    public KitCodesView(){
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

        kitCodeFld = new TextField(KIT_CODE);
        kitCodeFld.setInputPrompt(KIT_CODE);
        kitCodeFld.setRequired(true);
        firstRow.addComponent(kitCodeFld);

        descFld = new TextField(DESCRIPTION);
        descFld.setInputPrompt(DESCRIPTION);
        firstRow.addComponent(descFld);

        serviceTypeFld = new ComboBox(SERVICE_TYPE);
        serviceTypeFld.setInputPrompt(SERVICE_TYPE);
        serviceTypeFld.addItem("BOB");
        serviceTypeFld.addItem("DTF");
        serviceTypeFld.addItem("DTP");
        serviceTypeFld.addItem("VRT");
        serviceTypeFld.setNullSelectionAllowed(false);
        serviceTypeFld.setRequired(true);
        firstRow.addComponent(serviceTypeFld);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        MarginInfo marginInfo = new MarginInfo(true,false,true,false);
        secondRow.setMargin(marginInfo);
        mainUserInputLayout.addComponent(secondRow);

        activateDateFld = new DateField(ACTIVATE_DATE);
        activateDateFld.setRequired(true);
        secondRow.addComponent(activateDateFld);

        noOfSealsFld = new TextField(NO_OF_SEALS);
        noOfSealsFld.setInputPrompt(NO_OF_SEALS);
        noOfSealsFld.setRequired(true);
        secondRow.addComponent(noOfSealsFld);

        noOfEqFld = new TextField(NO_OF_EQUIPMENTS);
        noOfEqFld.setInputPrompt(NO_OF_EQUIPMENTS);
        noOfEqFld.setRequired(true);
        secondRow.addComponent(noOfEqFld);
        userFormLayout.setWidth("60%");
        mainTableLayout.setWidth("70%");
        headerLayout.setWidth("70%");
    }

    @Override
    protected IndexedContainer generateContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(KIT_CODE, String.class, null);
        container.addContainerProperty(DESCRIPTION, String.class, null);
        container.addContainerProperty(SERVICE_TYPE, String.class, null);
        container.addContainerProperty(ACTIVATE_DATE, String.class, null);
        container.addContainerProperty(NO_OF_SEALS, Integer.class, null);
        container.addContainerProperty(NO_OF_EQUIPMENTS, Integer.class, null);

        List<KitCodes> kitCodesList = connection.getAllKitCodes();
        for(KitCodes details : kitCodesList){
            Item item = container.addItem(details.getKitCodeId());
            item.getItemProperty(KIT_CODE).setValue(details.getKitCode());
            item.getItemProperty(DESCRIPTION).setValue(details.getDescription());
            item.getItemProperty(SERVICE_TYPE).setValue(details.getServiceType());
            item.getItemProperty(ACTIVATE_DATE).setValue(details.getActivateDate());
            item.getItemProperty(NO_OF_SEALS).setValue(details.getNoOfSeals());
            item.getItemProperty(NO_OF_EQUIPMENTS).setValue(details.getNoOfEquipments());
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
            KitCodes itemDetails = new KitCodes();
            itemDetails.setKitCodeId(itemIdVal);
            itemDetails.setKitCode(kitCodeFld.getValue());
            itemDetails.setServiceType(serviceTypeFld.getValue().toString());
            itemDetails.setDescription(descFld.getValue());
            itemDetails.setNoOfSeals(Integer.parseInt(noOfSealsFld.getValue()));
            itemDetails.setNoOfEquipments(Integer.parseInt(noOfEqFld.getValue()));
            itemDetails.setActivateDate(BackOfficeUtils.getDateStringFromDate(activateDateFld.getValue()));
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
            serviceTypeFld.setValue(item.getItemProperty(SERVICE_TYPE).getValue());
            descFld.setValue(item.getItemProperty(DESCRIPTION).getValue().toString());
            noOfEqFld.setValue(item.getItemProperty(NO_OF_EQUIPMENTS).getValue().toString());
            activateDateFld.setValue(BackOfficeUtils.convertStringToDate(item.getItemProperty(ACTIVATE_DATE).getValue().toString()));
            noOfSealsFld.setValue(item.getItemProperty(NO_OF_SEALS).getValue().toString());
            addButton.setCaption("Edit");
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = KIT_CODE;
        this.pageHeader = "KIT Code Details";
        this.className = "com.back.office.entity.KitCodes";
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        KitCodes itemDetails = (KitCodes) details;
        IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
        Item item;
        if(isEdit){
            item  = container.getItem(itemDetails.getKitCodeId());
        }
        else{
            item  = container.addItem(newId);
        }
        item.getItemProperty(KIT_CODE).setValue(itemDetails.getKitCode());
        item.getItemProperty(SERVICE_TYPE).setValue(itemDetails.getServiceType());
        item.getItemProperty(DESCRIPTION).setValue(itemDetails.getDescription());
        item.getItemProperty(NO_OF_SEALS).setValue(itemDetails.getNoOfSeals());
        item.getItemProperty(NO_OF_EQUIPMENTS).setValue(itemDetails.getNoOfEquipments());
        item.getItemProperty(ACTIVATE_DATE).setValue(itemDetails.getActivateDate());
    }
}
