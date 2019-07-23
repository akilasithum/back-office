package com.back.office.ui.authorization;

import com.back.office.db.DBConnection;
import com.back.office.entity.User;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class ViewAndEditCurrentUserDetailsView extends VerticalLayout implements View {

    protected DBConnection connection;
    VerticalLayout mainLayout;
    VerticalLayout headerLayout;
    protected VerticalLayout userFormLayout;
    protected HorizontalLayout buttonRow;
    protected VerticalLayout passwordFormLayout;
    protected HorizontalLayout passwordBtnLayout;

    User user;

    protected Button editBtn;
    protected Button changePasswordBtn;
    protected Button updatePasswordBtn;
    protected Button resetPasswordBtn;

    TextField staffIdFld;
    TextField staffNameFld;
    TextField displayNameFld;
    TextField positionFld;
    TextField departmentFld;
    TextField userRoleFld;
    PasswordField oldPasswordFld;
    PasswordField newPasswordFld;
    PasswordField confirmPasswordFld;


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public ViewAndEditCurrentUserDetailsView(){
        connection = DBConnection.getInstance();
        createMainLayout();
    }

    protected void createMainLayout(){
        String userId = UI.getCurrent().getSession().getAttribute("userName").toString();
        user = connection.getCurrentUser(userId);
        mainLayout = new VerticalLayout();
        mainLayout.setMargin(Constants.noMargin);
        addComponent(mainLayout);
        Label header = new Label("View and Edit User Details");
        header.addStyleName("headerText");
        mainLayout.addComponent(header);
        userFormLayout = new VerticalLayout();
        userFormLayout.setMargin(Constants.noMargin);
        mainLayout.addComponent(userFormLayout);

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.setSizeFull();
        firstRow.setMargin(Constants.noMargin);
        userFormLayout.addComponent(firstRow);

        staffIdFld = new TextField("Staff Id");
        staffIdFld.setValue(user.getStaffId());
        staffIdFld.setEnabled(false);
        firstRow.addComponent(staffIdFld);

        staffNameFld = new TextField("Staff Name");
        staffNameFld.setValue(user.getStaffName());
        firstRow.addComponent(staffNameFld);

        displayNameFld = new TextField("Display Name");
        displayNameFld.setValue(user.getDisplayName());
        firstRow.addComponent(displayNameFld);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.setSizeFull();
        secondRow.setMargin(Constants.noMargin);
        userFormLayout.addComponent(secondRow);

        positionFld = new TextField("Position");
        positionFld.setValue(user.getPosition());
        positionFld.setEnabled(false);
        secondRow.addComponent(positionFld);

        departmentFld = new TextField("Department");
        departmentFld.setValue(user.getDepartment());
        departmentFld.setEnabled(false);
        secondRow.addComponent(departmentFld);

        userRoleFld = new TextField("User Role");
        userRoleFld.setValue(connection.getRoleNameFromRoleId(user.getUserRoleId()));
        userRoleFld.setEnabled(false);
        secondRow.addComponent(userRoleFld);

        buttonRow = new HorizontalLayout();
        buttonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        buttonRow.setSpacing(true);
        mainLayout.addComponent(buttonRow);

        editBtn = new Button("Edit");
        editBtn.addClickListener((Button.ClickListener) clickEvent -> updateUser());
        buttonRow.addComponent(editBtn);

        changePasswordBtn = new Button("Change Password");
        changePasswordBtn.addClickListener((Button.ClickListener) clickEvent -> changePassword());
        buttonRow.addComponent(changePasswordBtn);

        passwordFormLayout = new VerticalLayout();
        passwordFormLayout.setMargin(Constants.noMargin);
        mainLayout.addComponent(passwordFormLayout);
        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(Constants.noMargin);
        passwordFormLayout.addComponent(formLayout);

        oldPasswordFld = new PasswordField("Old Password");
        oldPasswordFld.setRequiredIndicatorVisible(true);
        formLayout.addComponent(oldPasswordFld);

        newPasswordFld = new PasswordField("New Password");
        newPasswordFld.setRequiredIndicatorVisible(true);
        formLayout.addComponent(newPasswordFld);

        confirmPasswordFld = new PasswordField("Confirm Password");
        confirmPasswordFld.setRequiredIndicatorVisible(true);
        formLayout.addComponent(confirmPasswordFld);

        passwordBtnLayout = new HorizontalLayout();
        passwordBtnLayout.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        passwordBtnLayout.setSpacing(true);
        passwordFormLayout.addComponent(passwordBtnLayout);

        updatePasswordBtn = new Button("Update Password");
        passwordBtnLayout.addComponent(updatePasswordBtn);
        updatePasswordBtn.addClickListener((Button.ClickListener) clickEvent -> updatePassword());

        resetPasswordBtn = new Button("Reset Fields");
        passwordBtnLayout.addComponent(resetPasswordBtn);
        resetPasswordBtn.addClickListener((Button.ClickListener) clickEvent -> resetFields());

        passwordFormLayout.setVisible(false);

    }

    private void updatePassword(){
        if(oldPasswordFld.getValue() != null && !oldPasswordFld.getValue().isEmpty() && newPasswordFld.getValue() != null &&
        !newPasswordFld.getValue().isEmpty() && confirmPasswordFld.getValue() != null && !confirmPasswordFld.getValue().isEmpty()){

            if(oldPasswordFld.getValue().equals(user.getPassword())){
                if(newPasswordFld.getValue().equals(confirmPasswordFld.getValue())){
                    user.setPassword(newPasswordFld.getValue());
                    connection.updateObjectHBM(user);
                    BackOfficeUtils.showNotification("Success", "Password Update successfully", VaadinIcons.CHECK_CIRCLE_O);
                    resetFields();
                    passwordFormLayout.setVisible(false);
                }
                else{
                    BackOfficeUtils.showNotification("Error", "Password did not match. Enter same password.", VaadinIcons.POWER_OFF);
                    newPasswordFld.clear();
                    confirmPasswordFld.clear();
                }
            }
            else {
                BackOfficeUtils.showNotification("Error", "Old password is incorrect", VaadinIcons.POWER_OFF);
            }
        }
        else{
            BackOfficeUtils.showNotification("Error", "Fill all required fields", VaadinIcons.POWER_OFF);
        }
    }

    private void resetFields(){
        oldPasswordFld.clear();
        newPasswordFld.clear();
        confirmPasswordFld.clear();
    }

    private void updateUser(){

        user.setDisplayName(displayNameFld.getValue());
        user.setStaffName(staffNameFld.getValue());
        connection.updateObjectHBM(user);
        BackOfficeUtils.showNotification("Success", "User Update successfully", VaadinIcons.CHECK_CIRCLE_O);
    }

    private void changePassword(){
        passwordFormLayout.setVisible(true);
    }
}
