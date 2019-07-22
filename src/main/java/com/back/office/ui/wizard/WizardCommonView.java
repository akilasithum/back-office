package com.back.office.ui.wizard;

import com.back.office.db.DBConnection;
import com.back.office.entity.AircraftDetails;
import com.back.office.framework.UserEntryView;
import com.back.office.ui.wizard.steps.aircraft.AirCraftFirstStep;
import com.back.office.ui.wizard.steps.aircraft.AircraftFrontGalleyStep;
import com.back.office.ui.wizard.steps.aircraft.AircraftMiddleGalleyStep;
import com.back.office.ui.wizard.steps.aircraft.AircraftRearGalleyStep;
import com.back.office.utils.Constants;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.Wizard;

public abstract class WizardCommonView extends UserEntryView implements View {

    protected DBConnection connection;
    protected String headerName;
    protected String className;
    protected String objectName;
    protected String wizardName;
    protected Object editObj;

    protected VerticalLayout headerLayout;
    protected HorizontalLayout buttonLayout;
    protected HorizontalLayout tableLayout;
    protected Button addNewButton;
    protected Wizard wizard;
    protected Window window;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public WizardCommonView(){
        super();
        connection = DBConnection.getInstance();
        defineStringFields();
        createMainLayout();
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
        buttonLayout.setMargin(Constants.bottomMarginInfo);
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

    protected void createWizard(){
        window = new Window(wizardName);
        window.setWidth("50%");
        window.setHeight(500,Unit.PIXELS);
        final FormLayout content = new FormLayout();
        content.setMargin(true);
        window.center();
        wizard = new Wizard();
        wizard.setSizeFull();
        window.setContent(wizard);
        wizard.setStyleName("wizard");
        registerWizardBanClickListeners();
    }

    protected void registerWizardBanClickListeners(){
        wizard.getCancelButton().addClickListener((Button.ClickListener) clickEvent ->
        {
            ConfirmDialog.show(getUI(), "Cancel", "Are you sure you want to cancel the wizard?",
                    "Yes", "No", new ConfirmDialog.Listener() {
                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                UI.getCurrent().getSession().setAttribute(objectName,null);
                                window.close();
                            }
                        }
                    });
        });
        wizard.getFinishButton().addClickListener((Button.ClickListener) clickEvent -> {
            ConfirmDialog.show(getUI(), "Add item", "Are you sure you want to add new Item?",
                    "Yes", "No", new ConfirmDialog.Listener() {
                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                Object obj = UI.getCurrent().getSession().getAttribute(objectName);
                                connection.insertObjectHBM(obj);
                                updateTable(false,obj);
                                window.close();
                            }
                        }
                    });
        });
    }

    protected void enableDisableAllComponents(FormLayout layout,boolean isEnable){
        for(int i = 0 ; i< layout.getComponentCount();i++){
            Component component = layout.getComponent(i);
            if(!(component instanceof HorizontalLayout)){
                component.setEnabled(isEnable);
            }
        }
    }

    protected abstract void updateTable(boolean isEdit , Object object);
}
