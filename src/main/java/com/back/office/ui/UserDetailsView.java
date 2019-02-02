package com.back.office.ui;

import com.back.office.entity.User;
import com.back.office.entity.UserRole;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDetailsView extends CommonPageDetails {

    private final String STAFF_ID = "Staff Id";
    private final String STAFF_NAME = "Staff Name";
    private final String DISPLAY_NAME = "Display Name";
    private final String POSITION = "Position";
    private final String DEAPRTMENT = "Department";
    private final String STATUS = "Status";
    private final String USER_ROLE = "User Role";
    private Map<Integer,String> roleIdRoleNameMap;
    private Map<String,Integer> roleNameRoleIdMap;

    protected TextField staffIdFld;
    protected TextField staffNameFld;
    protected TextField displayNameFld;
    protected ComboBox positionComboBox;
    protected TextField departmentFld;
    protected CheckBox statusCheckBox;
    protected ComboBox userRoleComboBox;

    FilterGrid<User> userGrid;
    List<User> userList;

    public UserDetailsView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        roleIdRoleNameMap = new HashMap<>();
        roleNameRoleIdMap = new HashMap<>();
        List<UserRole> userRoles = (List<UserRole>)connection.getAllValues("com.back.office.entity.UserRole");
        for(UserRole userRole : userRoles){
            if(userRole.isActive()){
                roleIdRoleNameMap.put(userRole.getRoleId(),userRole.getRoleName());
                roleNameRoleIdMap.put(userRole.getRoleName(),userRole.getRoleId());
            }
        }
        super.createMainLayout();
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(firstRow);

        staffIdFld = new TextField(STAFF_ID);
        staffIdFld.setDescription(STAFF_ID);
        staffIdFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(staffIdFld);
        staffIdFld.addValueChangeListener(valueChangeEvent -> {isKeyFieldDirty = true;});

        staffNameFld = new TextField(STAFF_NAME);
        staffNameFld.setDescription(STAFF_NAME);
        staffNameFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(staffNameFld);

        displayNameFld = new TextField(DISPLAY_NAME);
        displayNameFld.setDescription(DISPLAY_NAME);
        displayNameFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(displayNameFld);

        userRoleComboBox = new ComboBox(USER_ROLE);
        userRoleComboBox.setDescription(USER_ROLE);
        userRoleComboBox.setRequiredIndicatorVisible(true);
        userRoleComboBox.setEmptySelectionAllowed(false);
        userRoleComboBox.setItems(roleIdRoleNameMap.values());
        firstRow.addComponent(userRoleComboBox);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        secondRow.setWidth(75,Unit.PERCENTAGE);
        secondRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(secondRow);

        positionComboBox = new ComboBox(POSITION);
        positionComboBox.setDescription(POSITION);
        positionComboBox.setItems("SCC","ICC","CC","IT");
        positionComboBox.setEmptySelectionAllowed(false);
        positionComboBox.setRequiredIndicatorVisible(true);
        secondRow.addComponent(positionComboBox);

        departmentFld = new TextField(DEAPRTMENT);
        departmentFld.setDescription(DEAPRTMENT);
        departmentFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(departmentFld);

        statusCheckBox = new CheckBox(STATUS, true);
        secondRow.addComponent(statusCheckBox);

        userGrid = new FilterGrid<>();
        userGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        userGrid.setSizeFull();
        tableLayout.addComponent(userGrid);
        setDataInGrid();
        GridContextMenu<User> gridMenu = new GridContextMenu<>(userGrid);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);

        userFormLayout.setWidth("70%");
        mainTableLayout.setWidth("80%");
        headerLayout.setWidth("70%");

    }

    protected void deleteItem(Object target) {
        if (target != null) {
            User user = (User) target;
            boolean success = connection.deleteObjectHBM(user.getUserId(), className);
            if (success) {
                BackOfficeUtils.showNotification("Success", "User delete successfully", VaadinIcons.CHECK_CIRCLE_O);
                userList.remove(target);
                userGrid.setItems(userList);
            } else {
                BackOfficeUtils.showNotification("Error", "Something wrong, please try again", VaadinIcons.CLOSE);
            }
        }
    }

    private void setDataInGrid(){
        userList = (List<User>)connection.getAllValues(className);
        userGrid.setItems(userList);
        userGrid.addColumn(User::getStaffId).setCaption(STAFF_ID).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        userGrid.addColumn(User::getStaffName).setCaption(STAFF_NAME).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        userGrid.addColumn(User::getDisplayName).setCaption(DISPLAY_NAME).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        userGrid.addColumn(User::getUserRoleId).setCaption(USER_ROLE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        userGrid.addColumn(User::getPosition).setCaption(POSITION).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        userGrid.addColumn(User::getDepartment).setCaption(DEAPRTMENT).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        userGrid.addColumn(bean->bean.isActive() ? "Active" : "Not Active").setCaption(STATUS).
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
            User user = new User();
            user.setUserId(itemIdVal);
            user.setStaffId(staffIdFld.getValue());
            user.setStaffName(staffNameFld.getValue());
            user.setDisplayName(displayNameFld.getValue());
            user.setPosition(positionComboBox.getValue().toString());
            user.setDepartment(departmentFld.getValue());
            user.setActive(statusCheckBox.getValue());
            user.setUserRoleId(roleNameRoleIdMap.get(userRoleComboBox.getValue().toString()));
            addOrUpdateDetails(user);
        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            User user = (User) target;
            idField.setValue(String.valueOf(user.getUserId()));
            staffIdFld.setValue(String.valueOf(user.getStaffId()));
            staffNameFld.setValue(user.getStaffName());
            displayNameFld.setValue(user.getDisplayName());
            positionComboBox.setValue(user.getPosition());
            departmentFld.setValue(user.getDepartment());
            statusCheckBox.setValue(user.isActive());
            userRoleComboBox.setValue(roleIdRoleNameMap.get(user.getUserRoleId()));
            addButton.setCaption("Save");
            editObj = user;
            isKeyFieldDirty = false;
        }
    }

    @Override
    protected TextField getKeyField() {
        return staffIdFld;
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
        if(isEdit){
            int index = userList.indexOf(editObj);
            userList.remove(editObj);
            userList.add(index,user);
        }
        else{
            userList.add(user);
        }
        userGrid.setItems(userList);
    }
}
