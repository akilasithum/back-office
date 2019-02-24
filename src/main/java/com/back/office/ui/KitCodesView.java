package com.back.office.ui;

import com.back.office.entity.EquipmentDetails;
import com.back.office.entity.KitCodes;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.data.HasValue;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class KitCodesView extends CommonPageDetails {

    private final String KIT_CODE = "Kit Code";
    private final String DESCRIPTION = "Description";
    private final String SERVICE_TYPE = "Service Type";
    private final String ACTIVATE_DATE = "Activate Date";
    private final String NO_OF_EQUIPMENTS = "No of Equipments";
    private final String PACK_TYPES = "Pack Types";

    protected TextField kitCodeFld;
    protected TextField descFld;
    protected ComboBox serviceTypeFld;
    protected DateField activateDateFld;
    protected ComboBox noOfEqFld;
    protected VerticalLayout packTypesLayout;

    FilterGrid<KitCodes> kitCodesFilterGrid;
    List<KitCodes> kitCodesList;

    public KitCodesView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setMargin(Constants.noMargin);
        firstRow.setSizeFull();
        mainUserInputLayout.addComponent(firstRow);

        packTypesLayout = new VerticalLayout();
        packTypesLayout.setCaption(PACK_TYPES);
        packTypesLayout.setSizeFull();
        packTypesLayout.setMargin(Constants.noMargin);

        kitCodeFld = new TextField(KIT_CODE);
        kitCodeFld.setDescription(KIT_CODE);
        kitCodeFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(kitCodeFld);
        kitCodeFld.addValueChangeListener(valueChangeEvent -> {isKeyFieldDirty = true;});

        serviceTypeFld = new ComboBox(SERVICE_TYPE);
        serviceTypeFld.setDescription(SERVICE_TYPE);
        serviceTypeFld.setItems("BOB","DTF","DTP","VRT");
        serviceTypeFld.setEmptySelectionAllowed(false);
        serviceTypeFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(serviceTypeFld);

        descFld = new TextField(DESCRIPTION);
        descFld.setDescription(DESCRIPTION);
        firstRow.addComponent(descFld);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        secondRow.setWidth(66.67f,Unit.PERCENTAGE);
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(secondRow);

        activateDateFld = new DateField(ACTIVATE_DATE);
        activateDateFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(activateDateFld);

        noOfEqFld = new ComboBox(NO_OF_EQUIPMENTS);
        noOfEqFld.setDescription(NO_OF_EQUIPMENTS);
        noOfEqFld.setRequiredIndicatorVisible(true);
        noOfEqFld.setItems(getEqNoList());
        secondRow.addComponent(noOfEqFld);
        noOfEqFld.addValueChangeListener(new HasValue.ValueChangeListener<Integer>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<Integer> valueChangeEvent) {
                if(valueChangeEvent.getValue() != null){
                        addEqTypesDropDowns(valueChangeEvent.getValue(),"");

                }
                else {
                    addEqTypesDropDowns(0,"");
                }
            }
        });

        kitCodesFilterGrid = new FilterGrid<>();
        kitCodesFilterGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        kitCodesFilterGrid.setSizeFull();
        tableLayout.addComponent(kitCodesFilterGrid);
        setDataInGrid();
        GridContextMenu<KitCodes> gridMenu = new GridContextMenu<>(kitCodesFilterGrid);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);

        mainUserInputLayout.addComponent(packTypesLayout);
        userFormLayout.setWidth("60%");
        mainTableLayout.setWidth("70%");
        headerLayout.setWidth("70%");
    }

    private List<Integer> getEqNoList(){
        List<Integer> integerList = new ArrayList<>();
        for(int i = 0 ;i<=20;i++){
            integerList.add(i);
        }
        return integerList;
    }

    private void addEqTypesDropDowns(int eqCount,String packTypes){
        packTypesLayout.removeAllComponents();
        List<EquipmentDetails> list = (List<EquipmentDetails>)connection.getAllValues("com.back.office.entity.EquipmentDetails");
        List<String> packTypesList = new ArrayList<>();
        for(EquipmentDetails eq : list){
            packTypesList.add(eq.getPackType());
        }
        String[] packTypesArr = packTypes.split(",");
        for(int i = 0 ; i<eqCount ;i++){
            ComboBox comboBox = new ComboBox();
            comboBox.setItems(packTypesList);
            if(!packTypes.isEmpty()){
                comboBox.setValue(packTypesArr[i]);
            }
            packTypesLayout.addComponent(comboBox);
        }
    }

    private String getPackTypesStr(){
        String packTypes = "";
        for(int i = 0;i<packTypesLayout.getComponentCount();i++){
            ComboBox comboBox = (ComboBox) packTypesLayout.getComponent(i);
            if(comboBox.getValue() != null && !String.valueOf(comboBox.getValue()).isEmpty()){
                packTypes += String.valueOf(comboBox.getValue()) + ",";
            }
            else{
                Notification.show("Enter all pack types", Notification.Type.ERROR_MESSAGE);
                return null;
            }
        }
        return packTypes.substring(0,packTypes.length()-1);
    }

    protected void deleteItem(Object target) {
        if (target != null) {
            KitCodes aircraftDetail = (KitCodes) target;
            boolean success = connection.deleteObjectHBM(aircraftDetail.getKitCodeId(), className);
            if (success) {
                BackOfficeUtils.showNotification("Success", "Kit code delete successfully", VaadinIcons.CHECK_CIRCLE_O);
                kitCodesList.remove(target);
                kitCodesFilterGrid.setItems(kitCodesList);
            } else {
                BackOfficeUtils.showNotification("Error", "Something wrong, please try again", VaadinIcons.CLOSE);
            }
        }
    }

    private void setDataInGrid(){
        kitCodesList = connection.getAllKitCodes();
        kitCodesFilterGrid.setItems(kitCodesList);
        kitCodesFilterGrid.addColumn(KitCodes::getKitCode).setCaption(KIT_CODE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        kitCodesFilterGrid.addColumn(KitCodes::getServiceType).setCaption(SERVICE_TYPE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        kitCodesFilterGrid.addColumn(KitCodes::getDescription).setCaption(DESCRIPTION).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        kitCodesFilterGrid.addColumn(KitCodes::getActivateDate).setCaption(ACTIVATE_DATE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        kitCodesFilterGrid.addColumn(KitCodes::getNoOfEquipments).setCaption(NO_OF_EQUIPMENTS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        kitCodesFilterGrid.addColumn(KitCodes::getPackTypes).setCaption(PACK_TYPES).
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
            KitCodes itemDetails = new KitCodes();
            itemDetails.setKitCodeId(itemIdVal);
            itemDetails.setKitCode(kitCodeFld.getValue());
            itemDetails.setServiceType(serviceTypeFld.getValue().toString());
            itemDetails.setDescription(descFld.getValue());
            itemDetails.setNoOfEquipments((Integer) noOfEqFld.getValue());
            Date date = Date.from(activateDateFld.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            String effectiveDateStr = BackOfficeUtils.getDateStringFromDate(date);
            itemDetails.setActivateDate(effectiveDateStr);
            String packTypes = getPackTypesStr();
            if(packTypes != null){
                itemDetails.setPackTypes(packTypes);
                addOrUpdateDetails(itemDetails,itemIdVal);
            }
        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            KitCodes kitCode = (KitCodes) target;
            idField.setValue(String.valueOf(kitCode.getKitCodeId()));
            kitCodeFld.setValue(kitCode.getKitCode());
            serviceTypeFld.setValue(kitCode.getServiceType());
            descFld.setValue(kitCode.getDescription());
            noOfEqFld.setValue(kitCode.getNoOfEquipments());
            Date input = BackOfficeUtils.convertStringToDate(kitCode.getActivateDate());
            LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            activateDateFld.setValue(date);
            addEqTypesDropDowns(kitCode.getNoOfEquipments(),kitCode.getPackTypes());
            addButton.setCaption("Save");
            editObj = kitCode;
            isKeyFieldDirty = false;
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = KIT_CODE;
        this.pageHeader = "KIT Code Details";
        this.className = "com.back.office.entity.KitCodes";
        this.keyFieldDBName = "kitCode";
    }

    @Override
    protected TextField getKeyField() {
        return kitCodeFld;
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        KitCodes kitCode = (KitCodes) details;
        if(isEdit){
            int index = kitCodesList.indexOf(editObj);
            kitCodesList.remove(editObj);
            kitCodesList.add(index,kitCode);
        }
        else{
            kitCodesList.add(kitCode);
        }
        kitCodesFilterGrid.setItems(kitCodesList);
    }
}
