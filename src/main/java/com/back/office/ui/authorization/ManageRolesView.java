package com.back.office.ui.authorization;

import com.back.office.db.DBConnection;
import com.back.office.entity.*;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.event.Action;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.tools.ant.Project;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ManageRolesView extends VerticalLayout implements View {

    protected DBConnection connection;
    private HorizontalLayout formLayout;
    private VerticalLayout mainLayout;
    private HorizontalLayout outterLayout;
    private VerticalLayout availableRolesLayout;
    TextField roleNameFld;
    CheckBox activeCheckBox;
    TextField roleIdHdnFld;
    TreeGrid<PermissionCodes> permissionTable;
    FilterGrid<UserRole> roleTable;
    Button addButton;
    Button clearButton;
    List<RolePermission> editRolePermissions;
    List<UserRole> userRoleList;
    private Object editObj;

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
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }
    protected void createMainLayout() {
        Label h1 = new Label("Manage User Roles");
        h1.addStyleName(ValoTheme.LABEL_H1);

        outterLayout = new HorizontalLayout();
        outterLayout.setSizeFull();
        outterLayout.setMargin(Constants.noMargin);
        outterLayout.setSpacing(true);

        availableRolesLayout = new VerticalLayout();
        availableRolesLayout.setSizeFull();
        availableRolesLayout.addComponent(getAvailableRolesTable());
        availableRolesLayout.setWidth("70%");
        availableRolesLayout.setMargin(Constants.noMargin);

        mainLayout = new VerticalLayout();
        mainLayout.setCaption("Add User Role");
        mainLayout.setSizeFull();
        mainLayout.setMargin(Constants.noMargin);
        addComponent(mainLayout);
        addComponent(h1);

        outterLayout.addComponent(availableRolesLayout);
        outterLayout.addComponent(mainLayout);
        addComponent(outterLayout);
        outterLayout.setComponentAlignment(availableRolesLayout,Alignment.TOP_CENTER);

        formLayout = new HorizontalLayout();
        formLayout.setSizeFull();
        formLayout.setMargin(Constants.noMargin);
        mainLayout.addComponent(formLayout);

        roleNameFld = new TextField(ROLE_NAME);
        roleNameFld.setDescription(ROLE_NAME);
        roleNameFld.setRequiredIndicatorVisible(true);
        formLayout.addComponent(roleNameFld);

        activeCheckBox = new CheckBox(ACTIVE, true);
        formLayout.addComponent(activeCheckBox);

        roleIdHdnFld = new TextField();
        roleIdHdnFld.setVisible(false);
        formLayout.addComponent(roleIdHdnFld);
        formLayout.setMargin(Constants.noMargin);

        permissionTable = new TreeGrid<>();
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
        buttonLayout.setMargin(Constants.noMargin);
        printInsertStatement();
    }

    private FilterGrid getAvailableRolesTable(){

        roleTable = new FilterGrid();
        roleTable.setSelectionMode(Grid.SelectionMode.SINGLE);
        roleTable.setSizeFull();
        userRoleList = (List<UserRole>)connection.getAllValues("com.back.office.entity.UserRole");
        roleTable.setItems(userRoleList);
        roleTable.addColumn(UserRole::getRoleName).setCaption(ROLE_NAME).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        roleTable.addColumn(bean -> bean.isActive() ? "Active" : "Not Active").setCaption(ACTIVE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        GridContextMenu<UserRole> gridMenu = new GridContextMenu<>(roleTable);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);
        return roleTable;
    }

    protected void updateGridBodyMenu(GridContextMenu.GridContextMenuOpenListener.GridContextMenuOpenEvent<?> event) {
        event.getContextMenu().removeItems();
        if (event.getItem() != null) {
            event.getContextMenu().addItem("Edit row", VaadinIcons.EDIT, selectedItem -> {
                editRole(event.getItem());
            });
            event.getContextMenu().addItem("Delete row", VaadinIcons.FOLDER_REMOVE, selectedItem -> {
                ConfirmDialog.show(getUI(), "Delete row", "Are you sure you want to delete this row?",
                        "Yes", "No", new ConfirmDialog.Listener() {
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    deleteRole(event.getItem());
                                }
                            }
                        });
            });
        }
    }

    protected TextField getColumnFilterField() {
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        return filter;
    }

    private void editRole(Object target){
        UserRole userRole = (UserRole) target;
        TreeDataProvider<PermissionCodes> dataProvider = (TreeDataProvider<PermissionCodes>) permissionTable.getDataProvider();
        TreeData<PermissionCodes> data = dataProvider.getTreeData();
        editRolePermissions = connection.getFilterList("roleIdFilter", "roleId",userRole.getRoleId(),
                "com.back.office.entity.RolePermission","rolePermissionId");
        List<Integer> codeList = connection.getUserRoleIds("roleIdFilter","roleId",userRole.getRoleId(),
                "com.back.office.entity.RolePermission");
        data.clear();
        roleNameFld.setValue(userRole.getRoleName());
        activeCheckBox.setValue(userRole.isActive());
        Map<String,Map<Integer,String>> funcAreasCodesMap = BackOfficeUtils.getPermissionCodes();
        for(Map.Entry<String,Map<Integer,String>> funcAreaMap : funcAreasCodesMap.entrySet()){
            PermissionCodes code = new PermissionCodes();
            code.setDisplayName(funcAreaMap.getKey());
            data.addItem(null, code);
            for(Map.Entry<Integer,String> map : funcAreaMap.getValue().entrySet()){
                PermissionCodes innerVal = new PermissionCodes();
                innerVal.setDisplayName(map.getValue());
                innerVal.setPermissionCode(map.getKey());
                innerVal.setAuthorized(codeList.contains(map.getKey()));
                data.addItem(code, innerVal);
            }
        }
        dataProvider.refreshAll();
        mainLayout.setCaption("Edit User Role");
        addButton.setCaption("Save");
        roleIdHdnFld.setValue(String.valueOf(userRole.getRoleId()));
        editObj = userRole;
    }

    private void deleteRole(Object target) {
        UserRole userRole = (UserRole) target;
        boolean success = connection.deleteObjectHBM(userRole.getRoleId(), "com.back.office.entity.UserRole");
        editRolePermissions = connection.getFilterList("roleIdFilter", "roleId",userRole.getRoleId(),
                "com.back.office.entity.RolePermission","rolePermissionId");
        for (RolePermission rolePermission : editRolePermissions) {
            connection.deleteObjectHBM(rolePermission);
        }
        if (success) {
            Notification.show("User Role delete successfully");
            userRoleList.remove(userRole);
            roleTable.setItems(userRoleList);
        } else {
            Notification.show("Something wrong, please try again");
        }
    }


    private void generateContainer(){

        List<String> menuItems = BackOfficeUtils.getMainMenuItems();
        permissionTable.addColumn(PermissionCodes::getDisplayName).setCaption(FUNC_AREA);
        permissionTable.addComponentColumn(permissionCode -> {
            CheckBox chk=new CheckBox();
            chk.addValueChangeListener(e->
                    permissionCode.setAuthorized(e.getValue())
            );
            chk.setValue(permissionCode.isAuthorized());
            if(menuItems.contains(permissionCode.getDisplayName())){
                return null;
            }
            return chk;
        }).setCaption(AUTHORIZAED);

        TreeDataProvider<PermissionCodes> dataProvider = (TreeDataProvider<PermissionCodes>) permissionTable.getDataProvider();
        TreeData<PermissionCodes> data = dataProvider.getTreeData();
        Map<String,Map<Integer,String>> funcAreasCodesMap = BackOfficeUtils.getPermissionCodes();
        for(Map.Entry<String,Map<Integer,String>> funcAreaMap : funcAreasCodesMap.entrySet()){
            PermissionCodes code = new PermissionCodes();
            code.setDisplayName(funcAreaMap.getKey());
            data.addItem(null, code);
            for(Map.Entry<Integer,String> map : funcAreaMap.getValue().entrySet()){
                PermissionCodes innerVal = new PermissionCodes();
                innerVal.setDisplayName(map.getValue());
                innerVal.setPermissionCode(map.getKey());
                innerVal.setAuthorized(false);
                data.addItem(code, innerVal);
            }
        }
        dataProvider.refreshAll();
    }

    private void addUserRole(){

        TreeDataProvider<PermissionCodes> dataProvider = (TreeDataProvider<PermissionCodes>) permissionTable.getDataProvider();
        TreeData<PermissionCodes> data = dataProvider.getTreeData();

        List<String> menuItems = BackOfficeUtils.getMainMenuItems();

        String roleName = roleNameFld.getValue();
        boolean isActive = activeCheckBox.getValue();
        UserRole role = new UserRole();
        role.setRoleName(roleName);
        role.setActive(isActive);
        int roleId;
        if(addButton.getCaption().equalsIgnoreCase("Add")) {
            roleId = connection.insertObjectHBM(role);
            role.setRoleId(roleId);
            userRoleList.add(role);
        }
        else{
            int index = userRoleList.indexOf(editObj);
            userRoleList.remove(editObj);
            roleId = Integer.parseInt(roleIdHdnFld.getValue());
            role.setRoleId(roleId);
            connection.updateObjectHBM(role);
            for(RolePermission rolePermission : editRolePermissions){
                connection.deleteObjectHBM(rolePermission);
            }
            userRoleList.add(index,role);
        }
        roleTable.setItems(userRoleList);

        for(PermissionCodes menuItem :data.getRootItems()){
            for(PermissionCodes permission : data.getChildren(menuItem)) {
                if (!menuItems.contains(permission.getDisplayName())) {
                    boolean isSelected = permission.isAuthorized();
                    if (isSelected) {
                        RolePermission rolePermission = new RolePermission();
                        rolePermission.setRoleId(roleId);
                        rolePermission.setPermissionCode(permission.getPermissionCode());
                        connection.insertObjectHBM(rolePermission);
                    }
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
        TreeDataProvider<PermissionCodes> dataProvider = (TreeDataProvider<PermissionCodes>) permissionTable.getDataProvider();
        TreeData<PermissionCodes> data = dataProvider.getTreeData();

        for(PermissionCodes menuItem :data.getRootItems()){
            for(PermissionCodes permission : data.getChildren(menuItem)){
                permission.setAuthorized(false);
            }
        }
        dataProvider.refreshAll();
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
