package com.back.office.ui;

import com.back.office.db.DBConnection;
import com.back.office.entity.AircraftDetails;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonPageDetails extends VerticalLayout implements View {

    protected DBConnection connection;
    protected HorizontalLayout outerLayout;
    protected VerticalLayout userFormLayout;
    protected VerticalLayout mainTableLayout;
    protected HorizontalLayout tableLayout;
    protected VerticalLayout mainUserInputLayout;
    VerticalLayout headerLayout;
    protected Button addButton;
    protected Button resetButton;
    protected String filterFieldStr;
    protected String className;
    protected String pageHeader;
    protected TextField idField;
    protected HorizontalLayout buttonRow;
    protected Object editObj;

    protected boolean isKeyFieldDirty = false;
    protected List<String> keyFieldValues;
    protected TextField keyField;
    protected String keyFieldDBName;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    public CommonPageDetails(){
        connection = DBConnection.getInstance();
        defineStringFields();
        createMainLayout();
    }

    protected void createMainLayout(){
        keyFieldValues = (List<String>) connection.getKeyFieldList(className,keyFieldDBName);
        MarginInfo marginInfo = new MarginInfo(false,false,false,false);
        headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        addComponent(headerLayout);
        Label h1 = new Label(pageHeader);
        h1.addStyleName(ValoTheme.LABEL_H1);
        headerLayout.addComponent(h1);
        headerLayout.setMargin(marginInfo);

        outerLayout = new HorizontalLayout();
        outerLayout.setMargin(marginInfo);
        outerLayout.setSizeFull();
        addComponent(outerLayout);

        userFormLayout = new VerticalLayout();
        outerLayout.addComponent(userFormLayout);
        mainTableLayout = new VerticalLayout();
        addComponent(mainTableLayout);
        tableLayout = new HorizontalLayout();
        tableLayout.setSizeFull();

        mainUserInputLayout = new VerticalLayout();
        userFormLayout.addComponent(mainUserInputLayout);
        mainUserInputLayout.setMargin(marginInfo);

        buttonRow = new HorizontalLayout();
        buttonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        buttonRow.setSpacing(true);
        userFormLayout.addComponent(buttonRow);
        userFormLayout.setMargin(marginInfo);

        addButton = new Button("Add");
        addButton.addClickListener((Button.ClickListener) clickEvent -> insertDetails());
        buttonRow.addComponent(addButton);
        resetButton = new Button("Reset");
        resetButton.addClickListener((Button.ClickListener) clickEvent -> resetFields());
        buttonRow.addComponent(resetButton);
        idField = new TextField("idFld");
        idField.setVisible(false);
        buttonRow.addComponent(idField);

        HorizontalLayout rowFilter = new HorizontalLayout();
        rowFilter.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        rowFilter.setSpacing(true);

        TextField filterFiled = new TextField();
        filterFiled.setCaption("Filter by " + filterFieldStr);
        rowFilter.addComponent(filterFiled);

        mainTableLayout.addComponent(tableLayout);
        mainTableLayout.setMargin(marginInfo);
        setComponentAlignment(mainTableLayout,Alignment.MIDDLE_LEFT);
        setComponentAlignment(outerLayout,Alignment.MIDDLE_LEFT);
        setComponentAlignment(headerLayout,Alignment.MIDDLE_LEFT);

    }

    protected abstract void insertDetails();

    protected abstract void fillEditDetails(Object target);

    protected abstract void defineStringFields();

    protected abstract void updateTable(boolean isEdit , Object details, int newId);

    protected abstract TextField getKeyField();

    protected void resetFields(){
        int componentCount = mainUserInputLayout.getComponentCount();
        for(int i = 0 ; i < componentCount ; i++) {
            if (mainUserInputLayout.getComponent(i) instanceof HorizontalLayout) {
                HorizontalLayout layout = (HorizontalLayout) mainUserInputLayout.getComponent(i);
                for(int j = 0 ; j< layout.getComponentCount() ; j++)
                    if (layout.getComponent(j) instanceof TextField) {
                        ((TextField) layout.getComponent(j)).clear();
                    } else if (layout.getComponent(j) instanceof DateField) {
                        ((DateField) layout.getComponent(j)).clear();
                    }
                    else if(layout.getComponent(j) instanceof  ComboBox){
                        ((ComboBox) layout.getComponent(j)).clear();
                    }
            }
        }
    }

    protected String validateFields(){
        int componentCount = mainUserInputLayout.getComponentCount();
        if(keyFieldValues.contains(getKeyField().getValue()) && isKeyFieldDirty){
            getKeyField().focus();
            return filterFieldStr+" already used. Please use another value";
        }
        for(int i = 0;i<componentCount;i++){
            if (mainUserInputLayout.getComponent(i) instanceof HorizontalLayout) {
                HorizontalLayout layout = (HorizontalLayout) mainUserInputLayout.getComponent(i);
                for(int j = 0;j < layout.getComponentCount();j++){
                    Component component = layout.getComponent(j);
                    if(component instanceof TextField){
                        TextField textField = (TextField) component;
                        if(textField.isRequiredIndicatorVisible() && (textField.getValue() == null || textField.getValue().isEmpty())){
                            textField.focus();
                            return textField.getCaption() + " is mandatory";
                        }
                    }
                    else if(component instanceof DateField){
                        DateField dateField = (DateField) component;
                        if(dateField.isRequiredIndicatorVisible() && dateField.getValue() == null){
                            dateField.focus();
                            return dateField.getCaption() + "is mandatory";
                        }
                    }
                    else if(component instanceof ComboBox){
                        ComboBox comboBox = (ComboBox) component;
                        if(comboBox.isRequiredIndicatorVisible() && comboBox.getValue() == null){
                            comboBox.focus();
                            return comboBox.getCaption() + " is mandatory";
                        }
                    }
                }
            }
        }
        isKeyFieldDirty = false;
        return null;
    }

    protected void addOrUpdateDetails(Object object,int id){
        if(addButton.getCaption().equals("Add")) {
            int newId = connection.insertObjectHBM(object);
            if (newId != 0) {
                BackOfficeUtils.showNotification("Success", pageHeader +" added successfully", VaadinIcons.CHECK_CIRCLE_O);
                updateTable(false,object,newId);
                resetFields();
            } else {
                BackOfficeUtils.showNotification("Error", "Something wrong, please try again", VaadinIcons.CLOSE);
            }
        }
        else{
            connection.insertObjectHBM(object);
            connection.updateRecordStatus(id,className);
            BackOfficeUtils.showNotification("Success", pageHeader +" updated successfully", VaadinIcons.CHECK_CIRCLE_O);
            updateTable(true,object,0);
            addButton.setCaption("Add");
            resetFields();
        }
    }

    protected TextField getColumnFilterField() {
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        return filter;
    }

    protected void updateGridBodyMenu(GridContextMenu.GridContextMenuOpenListener.GridContextMenuOpenEvent<?> event) {
        event.getContextMenu().removeItems();
        if (event.getItem() != null) {
            event.getContextMenu().addItem("Edit row", VaadinIcons.EDIT, selectedItem -> {
                fillEditDetails(event.getItem());
            });
            event.getContextMenu().addItem("Delete row", VaadinIcons.FOLDER_REMOVE, selectedItem -> {
                ConfirmDialog.show(getUI(), "Delete row", "Are you sure you want to delete this row?",
                        "Yes", "No", new ConfirmDialog.Listener() {
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    deleteItem(event.getItem());
                                }
                            }
                });
            });
        }
    }

    protected abstract void deleteItem(Object item);
}
