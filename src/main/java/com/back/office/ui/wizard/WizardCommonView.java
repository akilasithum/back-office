package com.back.office.ui.wizard;

import com.back.office.db.DBConnection;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public abstract class WizardCommonView extends VerticalLayout implements View {

    protected DBConnection connection;
    protected String headerName;
    protected String className;
    protected String keyField;
    protected Object editObj;

    protected VerticalLayout headerLayout;
    protected HorizontalLayout buttonLayout;
    protected HorizontalLayout tableLayout;
    protected Button addNewButton;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public WizardCommonView(){
        connection = DBConnection.getInstance();
        defineStringFields();
        createMainLayout();
        setStyleName("backColorGrey");
    }

    protected void createMainLayout(){
        MarginInfo marginInfo = new MarginInfo(false,false,false,false);
        headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        addComponent(headerLayout);
        Label h1 = new Label(headerName);
        h1.addStyleName(ValoTheme.LABEL_H1);
        headerLayout.addComponent(h1);
        headerLayout.setMargin(marginInfo);

        buttonLayout = new HorizontalLayout();
        buttonLayout.setMargin(marginInfo);
        buttonLayout.setSizeFull();
        addComponent(buttonLayout);

        addNewButton = new Button("Add New");
        addNewButton.setSizeFull();
        addNewButton.addClickListener((Button.ClickListener) clickEvent -> createItemWizard());

        buttonLayout.addComponents(addNewButton);

        tableLayout = new HorizontalLayout();
        tableLayout.setSizeFull();
        tableLayout.setMargin(marginInfo);
        tableLayout.setHeightUndefined();
        addComponent(tableLayout);

        buttonLayout.setWidth("20%");
        setComponentAlignment(tableLayout, Alignment.MIDDLE_LEFT);
        setComponentAlignment(buttonLayout,Alignment.MIDDLE_LEFT);
        setComponentAlignment(headerLayout,Alignment.MIDDLE_LEFT);
    }

    protected TextField getColumnFilterField() {
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        return filter;
    }

    protected abstract void defineStringFields();

    protected abstract void createItemWizard();
}
