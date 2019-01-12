package com.back.office.ui.authorization;

import com.back.office.db.DBConnection;
import com.back.office.entity.PermissionCodes;
import com.back.office.entity.RolePermission;
import com.back.office.entity.UserRole;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.Action;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageRolesView extends VerticalLayout implements View {

    protected DBConnection connection;
    private HorizontalLayout formLayout;
    private VerticalLayout mainLayout;
    private HorizontalLayout outterLayout;
    private VerticalLayout availableRolesLayout;
    TextField roleNameFld;
    CheckBox activeCheckBox;
    TextField roleIdHdnFld;
    TreeTable permissionTable;
    Table roleTable;
    Button addButton;
    Button clearButton;
    List<RolePermission> editRolePermissions;

    private static final String FUNC_AREA = "Functional Area";
    private static final String AUTHORIZAED = "Authorized";
    private static final String ROLE_NAME = "Role Name";
    private static final String ACTIVE = "Status";

    public ManageRolesView(){
        connection = DBConnection.getInstance();
        setMargin(true);
        createMainLayout();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
    protected void createMainLayout() {
        Label h1 = new Label("Manage User Roles");
        h1.addStyleName(ValoTheme.LABEL_H1);

        outterLayout = new HorizontalLayout();
        outterLayout.setSizeFull();
        outterLayout.setSpacing(true);

        availableRolesLayout = new VerticalLayout();
        availableRolesLayout.setSizeFull();
        availableRolesLayout.addComponent(getAvailableRolesTable());
        availableRolesLayout.setWidth("70%");

        mainLayout = new VerticalLayout();
        mainLayout.setCaption("Add User Role");
        mainLayout.setSizeFull();
        mainLayout.setStyleName("layout-with-border");
        //addComponent(mainLayout);
        addComponent(h1);

        outterLayout.addComponent(availableRolesLayout);
        outterLayout.addComponent(mainLayout);
        addComponent(outterLayout);
        outterLayout.setComponentAlignment(availableRolesLayout,Alignment.TOP_CENTER);

        formLayout = new HorizontalLayout();
        formLayout.setSizeFull();
        mainLayout.addComponent(formLayout);

        roleNameFld = new TextField(ROLE_NAME);
        roleNameFld.setInputPrompt(ROLE_NAME);
        roleNameFld.setRequired(true);
        formLayout.addComponent(roleNameFld);

        activeCheckBox = new CheckBox(ACTIVE, true);
        formLayout.addComponent(activeCheckBox);

        roleIdHdnFld = new TextField();
        roleIdHdnFld.setVisible(false);
        formLayout.addComponent(roleIdHdnFld);
        formLayout.setMargin(Constants.bottomMarginInfo);

        permissionTable = new TreeTable();
        permissionTable.setSelectable(true);
        permissionTable.setMultiSelect(false);
        permissionTable.setSortEnabled(true);
        permissionTable.setColumnCollapsingAllowed(false);
        permissionTable.setColumnReorderingAllowed(false);
        permissionTable.setPageLength(10);
        permissionTable.setSizeFull();
        generateContainer();

        mainLayout.addComponent(permissionTable);
        addButton = new Button("Add");
        clearButton = new Button("Clear");
        addButton.addClickListener((Button.ClickListener) clickEvent -> addUserRole());
        clearButton.addClickListener((Button.ClickListener) clickEvent -> clear());
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        mainLayout.addComponent(buttonLayout);
        buttonLayout.addComponent(addButton);
        buttonLayout.addComponent(clearButton);
        buttonLayout.setMargin(Constants.topMarginInfo);
        buttonLayout.setMargin(Constants.topBottomMarginInfo);
        printInsertStatement();
    }

    private Table getAvailableRolesTable(){

        roleTable = new Table();
        roleTable.setSelectable(true);
        roleTable.setMultiSelect(false);
        roleTable.setSortEnabled(true);
        roleTable.setColumnCollapsingAllowed(false);
        roleTable.setColumnReorderingAllowed(false);
        roleTable.setPageLength(5);
        roleTable.setContainerDataSource(getAvailableTableContainer());
        roleTable.setSizeFull();
        roleTable.addActionHandler(actionHandler);
        return roleTable;
    }

    Action.Handler actionHandler = new Action.Handler() {
        private final Action editItem = new Action("Edit user role" , FontAwesome.EDIT);
        private final Action deleteItem = new Action("Delete user role" , FontAwesome.REMOVE);
        private final Action[] ACTIONS = new Action[] {editItem, deleteItem};

        @Override
        public void handleAction(Action action, Object sender, Object target) {
            if(action.getCaption().equals("Edit user role")){
                editRole(target);
            }
            else if(action.getCaption().equals("Delete user role")){
                ConfirmDialog.show(getUI(), "Delete", "Are you sure you want to delete this user role?",
                        "Yes", "No", new ConfirmDialog.Listener() {

                            public void onClose(ConfirmDialog dialog) {
                                if(dialog.isConfirmed()){
                                    deleteRole(target);
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

    private void editRole(Object target){

        editRolePermissions = connection.getFilterList("roleIdFilter","roleId",Integer.parseInt(target.toString()),
                "com.back.office.entity.RolePermission");
        Item roleRow = roleTable.getItem(target);
        roleNameFld.setValue(roleRow.getItemProperty(ROLE_NAME).getValue().toString());
        activeCheckBox.setValue(roleRow.getItemProperty(ACTIVE).getValue().toString().equalsIgnoreCase("Active"));
        for(RolePermission rolePermission : editRolePermissions){
            Item item = permissionTable.getItem(rolePermission.getPermissionCode());
            ((CheckBox)item.getItemProperty(AUTHORIZAED).getValue()).setValue(true);
        }
        mainLayout.setCaption("Edit User Role");
        addButton.setCaption("Edit");
        roleIdHdnFld.setValue(target.toString());
    }

    private void deleteRole(Object target) {
        boolean success = connection.deleteObjectHBM(Integer.parseInt(target.toString()), "com.back.office.entity.UserRole");
        editRolePermissions = connection.getFilterList("roleIdFilter", "roleId", Integer.parseInt(target.toString()),
                "com.back.office.entity.RolePermission");
        for (RolePermission rolePermission : editRolePermissions) {
            connection.deleteObjectHBM(rolePermission);
        }
        if (success) {
            Notification.show("User Role delete successfully");
            IndexedContainer container = (IndexedContainer) roleTable.getContainerDataSource();
            container.removeItem(target);
        } else {
            Notification.show("Something wrong, please try again");
        }
    }

    private IndexedContainer getAvailableTableContainer(){
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(ROLE_NAME, String.class, null);
        container.addContainerProperty(ACTIVE, String.class, null);
        List<UserRole> userRoles = (List<UserRole>)connection.getAllValues("com.back.office.entity.UserRole");
        for(UserRole userRole : userRoles){
            Item item = container.addItem(userRole.getRoleId());
            item.getItemProperty(ROLE_NAME).setValue(userRole.getRoleName());
            item.getItemProperty(ACTIVE).setValue(userRole.isActive() == true ? "Active" : "Not Active");
        }
        return container;
    }

    private IndexedContainer generateContainer(){
        IndexedContainer container = new IndexedContainer();
        permissionTable.addContainerProperty(FUNC_AREA, String.class, null);
        permissionTable.addContainerProperty(AUTHORIZAED, CheckBox.class, null);
        int i = 1;
        Map<String,Map<Integer,String>> funcAreasCodesMap = BackOfficeUtils.getPermissionCodes();
        for(Map.Entry<String,Map<Integer,String>> funcAreaMap : funcAreasCodesMap.entrySet()){
            permissionTable.addItem(new Object[] {funcAreaMap.getKey(), null}, i);
            int j = 100*i;
            for(Map.Entry<Integer,String> map : funcAreaMap.getValue().entrySet()){
                CheckBox checkBox = new CheckBox();
                permissionTable.addItem(new Object[] {map.getValue(), checkBox}, map.getKey());
                permissionTable.setParent(map.getKey(),i);
                permissionTable.setChildrenAllowed(map.getKey(),false);
                j++;
            }
            i++;
        }
        return container;
    }

    private void addUserRole(){
        IndexedContainer container = (IndexedContainer) permissionTable.getContainerDataSource();
        IndexedContainer roleContainer = (IndexedContainer) roleTable.getContainerDataSource();
        List<Integer> menuItems = (List<Integer>)container.getItemIds();
        String roleName = roleNameFld.getValue();
        boolean isActive = activeCheckBox.getValue();
        UserRole role = new UserRole();
        role.setRoleName(roleName);
        role.setActive(isActive);
        int roleId;
        Item roleItem;
        if(addButton.getCaption().equalsIgnoreCase("Add")) {
            roleId = connection.insertObjectHBM(role);
            roleItem = roleContainer.addItem(roleId);
        }
        else{
            roleId = Integer.parseInt(roleIdHdnFld.getValue());
            role.setRoleId(roleId);
            connection.updateObjectHBM(role);
            for(RolePermission rolePermission : editRolePermissions){
                connection.deleteObjectHBM(rolePermission);
            }
            roleItem = roleContainer.getItem(roleId);
        }
        roleItem.getItemProperty(ROLE_NAME).setValue(roleNameFld.getValue());
        roleItem.getItemProperty(ACTIVE).setValue(activeCheckBox.getValue() == true ? "Active" : "Not Active");
        for(Integer menuItem :menuItems){
            Item item = container.getItem(menuItem);
            if(menuItem >= 100) {
                boolean isSelected = ((CheckBox) item.getItemProperty(AUTHORIZAED).getValue()).getValue();
                if(isSelected) {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setRoleId(roleId);
                    rolePermission.setPermissionCode(menuItem);
                    connection.insertObjectHBM(rolePermission);
                }
            }
        }
        Notification.show("Role Added Successfully");
        mainLayout.setCaption("Add User Role");
        addButton.setCaption("Add");
        clear();
    }

    private void clear(){
        roleNameFld.setValue("");
        IndexedContainer container = (IndexedContainer)permissionTable.getContainerDataSource();
        List<Integer> menuItems = (List<Integer>)container.getItemIds();
        for(Integer menuItem :menuItems){
            Item item = permissionTable.getItem(menuItem);
            if(menuItem >= 100) {
                ((CheckBox) item.getItemProperty(AUTHORIZAED).getValue()).setValue(false);
            }
        }
    }

    private void printInsertStatement(){
        String s = "";
        int i = 100;
        for(String menuItem : BackOfficeUtils.getSellsReportsMap()){
            s += "insert into permission_codes (permissionCode,displayName,funcArea) values ("+i+",'"+menuItem+"','Sales Report');";
            i++;
        }
        int j =0;
    }
}
