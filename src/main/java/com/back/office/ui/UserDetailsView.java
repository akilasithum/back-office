package com.back.office.ui;

import com.back.office.entity.KitCodes;
import com.back.office.entity.User;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

public class UserDetailsView extends CommonPageDetails {

    private final String STAFF_ID = "Staff Id";
    private final String STAFF_NAME = "Staff Name";
    private final String DISPLAY_NAME = "Display Name";
    private final String POSITION = "Position";
    private final String DEAPRTMENT = "Department";
    private final String STATUS = "Status";

    protected TextField staffIdFld;
    protected TextField staffNameFld;
    protected TextField displayNameFld;
    protected ComboBox positionComboBox;
    protected TextField departmentFld;
    protected CheckBox statusCheckBox;

    public UserDetailsView(){
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

        staffIdFld = new TextField(STAFF_ID);
        staffIdFld.setInputPrompt(STAFF_ID);
        staffIdFld.setRequired(true);
        firstRow.addComponent(staffIdFld);

        staffNameFld = new TextField(STAFF_NAME);
        staffNameFld.setInputPrompt(STAFF_NAME);
        staffNameFld.setRequired(true);
        firstRow.addComponent(staffNameFld);

        displayNameFld = new TextField(DISPLAY_NAME);
        displayNameFld.setInputPrompt(DISPLAY_NAME);
        displayNameFld.setRequired(true);
        firstRow.addComponent(displayNameFld);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        MarginInfo marginInfo = new MarginInfo(true,false,true,false);
        secondRow.setMargin(marginInfo);
        mainUserInputLayout.addComponent(secondRow);

        positionComboBox = new ComboBox(POSITION);
        positionComboBox.setInputPrompt(POSITION);
        positionComboBox.addItem("SCC");
        positionComboBox.addItem("ICC");
        positionComboBox.addItem("CC");
        positionComboBox.addItem("IT");
        positionComboBox.setNullSelectionAllowed(false);
        positionComboBox.setRequired(true);
        secondRow.addComponent(positionComboBox);

        departmentFld = new TextField(DEAPRTMENT);
        departmentFld.setInputPrompt(DEAPRTMENT);
        departmentFld.setRequired(true);
        secondRow.addComponent(departmentFld);

        statusCheckBox = new CheckBox(STATUS, true);
        secondRow.addComponent(statusCheckBox);

        userFormLayout.setWidth("60%");
        mainTableLayout.setWidth("70%");
        headerLayout.setWidth("70%");

    }

    @Override
    protected IndexedContainer generateContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(STAFF_ID, Integer.class, null);
        container.addContainerProperty(STAFF_NAME, String.class, null);
        container.addContainerProperty(DISPLAY_NAME, String.class, null);
        container.addContainerProperty(POSITION, String.class, null);
        container.addContainerProperty(DEAPRTMENT, String.class, null);
        container.addContainerProperty(STATUS, String.class, null);

        List<User> users = (List<User>)connection.getAllValues(className);
        for(User user : users){
            Item item = container.addItem(user.getUserId());
            item.getItemProperty(STAFF_ID).setValue(user.getStaffId());
            item.getItemProperty(STAFF_NAME).setValue(user.getStaffName());
            item.getItemProperty(DISPLAY_NAME).setValue(user.getDisplayName());
            item.getItemProperty(POSITION).setValue(user.getPosition());
            item.getItemProperty(DEAPRTMENT).setValue(user.getDepartment());
            item.getItemProperty(STATUS).setValue(user.isActive() ? "Active" : "Not Active");
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
            User user = new User();
            user.setUserId(itemIdVal);
            user.setStaffId(Integer.valueOf(staffIdFld.getValue()));
            user.setStaffName(staffNameFld.getValue());
            user.setDisplayName(displayNameFld.getValue());
            user.setPosition(positionComboBox.getValue().toString());
            user.setDepartment(departmentFld.getValue());
            user.setActive(statusCheckBox.getValue());
            addOrUpdateDetails(user);

        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
            Item item = container.getItem(target);
            idField.setValue(target.toString());
            staffIdFld.setValue(item.getItemProperty(STAFF_ID).getValue().toString());
            staffNameFld.setValue(item.getItemProperty(STAFF_NAME).getValue().toString());
            displayNameFld.setValue(item.getItemProperty(DISPLAY_NAME).getValue().toString());
            positionComboBox.setValue(item.getItemProperty(POSITION).getValue().toString());
            departmentFld.setValue(item.getItemProperty(DEAPRTMENT).getValue().toString());
            statusCheckBox.setValue(item.getItemProperty(STATUS).getValue().toString().equals("Active") ? true : false);
            addButton.setCaption("Edit");
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = STAFF_ID;
        this.pageHeader = "Staff Details";
        this.className = "com.back.office.entity.User";
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        User user = (User) details;
        IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
        Item item;
        if(isEdit){
            item  = container.getItem(user.getUserId());
        }
        else{
            item  = container.addItem(newId);
        }
        item.getItemProperty(STAFF_ID).setValue(user.getStaffId());
        item.getItemProperty(STAFF_NAME).setValue(user.getStaffName());
        item.getItemProperty(DISPLAY_NAME).setValue(user.getDisplayName());
        item.getItemProperty(POSITION).setValue(user.getPosition());
        item.getItemProperty(DEAPRTMENT).setValue(user.getDepartment());
        item.getItemProperty(STATUS).setValue(user.isActive() ? "Active" : "Not Active");
    }
}
