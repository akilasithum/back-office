package com.back.office.ui;

import com.back.office.db.DBConnection;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.Action;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.tepi.filtertable.FilterTable;
import org.vaadin.dialogs.ConfirmDialog;

public abstract class CommonPageDetails extends VerticalLayout implements View {

    protected DBConnection connection;
    protected VerticalLayout userFormLayout;
    protected VerticalLayout mainTableLayout;
    protected HorizontalLayout tableLayout;
    protected VerticalLayout mainUserInputLayout;
    VerticalLayout headerLayout;
    protected Button addButton;
    protected Button resetButton;
    protected FilterTable detailsTable;
    protected String filterFieldStr;
    protected String className;
    protected String pageHeader;
    protected TextField idField;
    protected HorizontalLayout buttonRow;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    public CommonPageDetails(){
        connection = DBConnection.getInstance();
        setMargin(true);
        defineStringFields();
        createMainLayout();
    }

    protected void createMainLayout(){

        setSpacing(true);
        headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        addComponent(headerLayout);
        Label h1 = new Label(pageHeader);
        h1.addStyleName(ValoTheme.LABEL_H1);
        headerLayout.addComponent(h1);

        userFormLayout = new VerticalLayout();
        addComponent(userFormLayout);
        userFormLayout.setStyleName("layout-with-border");
        mainTableLayout = new VerticalLayout();
        addComponent(mainTableLayout);
        mainTableLayout.setStyleName("layout-with-border");
        tableLayout = new HorizontalLayout();
        tableLayout.setSizeFull();

        mainUserInputLayout = new VerticalLayout();
        userFormLayout.addComponent(mainUserInputLayout);

        buttonRow = new HorizontalLayout();
        buttonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        buttonRow.setSpacing(true);
        userFormLayout.addComponent(buttonRow);

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
        filterFiled.setInputPrompt("Filter by " + filterFieldStr);
        rowFilter.addComponent(filterFiled);

        Button filterBtn = new Button("Filter");
        filterBtn.addClickListener((Button.ClickListener) clickEvent -> {
            IndexedContainer container = (IndexedContainer)detailsTable.getContainerDataSource();
            if(filterFiled.getValue() == null || filterFiled.getValue().isEmpty()) {
                container.removeContainerFilters(filterFieldStr);
            }
            else{
                container.addContainerFilter(filterFieldStr, filterFiled.getValue(), true, false);
            }
        });
        rowFilter.addComponent(filterBtn);
        mainTableLayout.addComponent(rowFilter);
        detailsTable = new FilterTable();
        detailsTable.setSelectable(true);
        detailsTable.setMultiSelect(false);
        detailsTable.setSortEnabled(true);
        detailsTable.setFilterBarVisible(true);
        detailsTable.setColumnCollapsingAllowed(true);
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setPageLength(10);
        IndexedContainer normalContainer = generateContainer();
        detailsTable.setContainerDataSource(normalContainer);
        detailsTable.setSizeFull();
        detailsTable.addActionHandler(actionHandler);

        mainTableLayout.addComponent(tableLayout);
        tableLayout.addComponent(detailsTable);
        setComponentAlignment(mainTableLayout,Alignment.MIDDLE_CENTER);
        setComponentAlignment(userFormLayout,Alignment.MIDDLE_CENTER);
        setComponentAlignment(headerLayout,Alignment.MIDDLE_CENTER);

    }

    protected abstract IndexedContainer generateContainer();

    protected void deleteItem(Object target){
        if(target != null) {
            boolean success = connection.deleteObjectHBM(Integer.parseInt(target.toString()), className);
            if(success){
                Notification.show("Detail delete successfully");
                IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
                container.removeItem(target);
            }
            else {
                Notification.show("Something wrong, please try again");
            }
        }
    }

    protected abstract void insertDetails();

    protected abstract void fillEditDetails(Object target);

    protected abstract void defineStringFields();

    protected abstract void updateTable(boolean isEdit , Object details, int newId);

    Action.Handler actionHandler = new Action.Handler() {
        private final Action editItem = new Action("Edit row detail" , FontAwesome.EDIT);
        private final Action deleteItem = new Action("Delete row detail" , FontAwesome.REMOVE);
        private final Action[] ACTIONS = new Action[] {editItem, deleteItem};

        @Override
        public void handleAction(Action action, Object sender, Object target) {
            if(action.getCaption().equals("Edit row detail")){
                fillEditDetails(target);
            }
            else if(action.getCaption().equals("Delete row detail")){
                ConfirmDialog.show(getUI(), "Delete", "Are you sure you want to delete this row?",
                        "Yes", "No", new ConfirmDialog.Listener() {

                            public void onClose(ConfirmDialog dialog) {
                                if(dialog.isConfirmed()){
                                    deleteItem(target);
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
        for(int i = 0;i<componentCount;i++){
            if (mainUserInputLayout.getComponent(i) instanceof HorizontalLayout) {
                HorizontalLayout layout = (HorizontalLayout) mainUserInputLayout.getComponent(i);
                for(int j = 0;j < layout.getComponentCount();j++){
                    Component component = layout.getComponent(j);
                    if(component instanceof TextField){
                        TextField textField = (TextField) component;
                        if(textField.isRequired() && (textField.getValue() == null || textField.getValue().isEmpty())){
                            textField.focus();
                            return textField.getCaption() + " is mandatory";
                        }
                    }
                    else if(component instanceof DateField){
                        DateField dateField = (DateField) component;
                        if(dateField.isRequired() && dateField.getValue() == null){
                            dateField.focus();
                            return dateField.getCaption() + "is mandatory";
                        }
                    }
                    else if(component instanceof ComboBox){
                        ComboBox comboBox = (ComboBox) component;
                        if(comboBox.isRequired() && comboBox.getValue() == null){
                            comboBox.focus();
                            return comboBox.getCaption() + " is mandatory";
                        }
                    }
                }
            }
        }
        return null;
    }

    protected void addOrUpdateDetails(Object object){
        if(addButton.getCaption().equals("Add")) {
            int newId = connection.insertObjectHBM(object);
            if (newId != 0) {
                Notification.show(pageHeader +" added successfully");
                updateTable(false,object,newId);
                resetFields();
            } else {
                Notification.show("Something wrong, please try again");
            }
        }
        else{
            connection.updateObjectHBM(object);
            Notification.show(pageHeader + " updated successfully");
            updateTable(true,object,0);
            addButton.setCaption("Add");
            resetFields();
        }
    }
}
