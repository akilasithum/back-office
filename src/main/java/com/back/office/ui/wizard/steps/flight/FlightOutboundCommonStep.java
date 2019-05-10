package com.back.office.ui.wizard.steps.flight;

import com.back.office.entity.Flight;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

public class FlightOutboundCommonStep implements WizardStep {

    protected TextField obFlightNameFld;
    protected TextField obFlightFromFld;
    protected TextField obFlightToFld;
    protected ComboBox obNoOfSectorsComboBox;

    private final String FLIGHT_NAME = "Flight No";
    private final String FLIGHT_FROM = "Flight From";
    private final String FLIGHT_TO = "Flight To";
    private final String NO_OF_SECTORS = "No of Sectors";

    @Override
    public String getCaption() {
        return "Outbound Common Details";
    }

    @Override
    public Component getContent() {
        FormLayout form = new FormLayout();
        obFlightNameFld = new TextField(FLIGHT_NAME);
        obFlightNameFld.setDescription(FLIGHT_NAME);
        obFlightNameFld.setRequiredIndicatorVisible(true);
        form.addComponent(obFlightNameFld);


        obNoOfSectorsComboBox = new ComboBox(NO_OF_SECTORS);
        obNoOfSectorsComboBox.setDescription(NO_OF_SECTORS);
        obNoOfSectorsComboBox.setItems("1","2","3");
        obNoOfSectorsComboBox.setEmptySelectionAllowed(false);
        obNoOfSectorsComboBox.setRequiredIndicatorVisible(true);
        obNoOfSectorsComboBox.setValue("1");
        form.addComponent(obNoOfSectorsComboBox);

        obFlightFromFld = new TextField(FLIGHT_FROM);
        obFlightFromFld.setDescription(FLIGHT_FROM);
        obFlightFromFld.setRequiredIndicatorVisible(true);
        form.addComponent(obFlightFromFld);

        obFlightToFld = new TextField(FLIGHT_TO);
        obFlightToFld.setDescription(FLIGHT_TO);
        obFlightToFld.setRequiredIndicatorVisible(true);
        form.addComponent(obFlightToFld);
        fillFieldsIfAddedAlready();
        return form;
    }

    private void fillFieldsIfAddedAlready(){
        Object obj = UI.getCurrent().getSession().getAttribute("flight");
        Flight item;
        if(obj != null && obj instanceof Flight){
            item = (Flight)obj;
            if(item.getObFlightNo() != null) obFlightNameFld.setValue(item.getObFlightNo());
            if(item.getObFlightFrom() != null) obFlightFromFld.setValue(item.getObFlightFrom());
            if(item.getObFlightTo() != null) obFlightToFld.setValue(item.getObFlightTo());
            if(item.getObNoOfSectors() != 0) obNoOfSectorsComboBox.setValue(String.valueOf(item.getObNoOfSectors()));
        }
    }

    @Override
    public boolean onAdvance() {
        return saveEntries();
    }

    @Override
    public boolean onBack() {
        return saveEntries();
    }

    private boolean saveEntries(){
        Object obj = UI.getCurrent().getSession().getAttribute("flight");
        Flight item;
        if(obj != null){
            item = (Flight)obj;
        }
        else{
            Notification.show("Something wrong. Please close window and try again", Notification.Type.ERROR_MESSAGE);
            return false;
        }
        int noOfSectors =  Integer.parseInt(obNoOfSectorsComboBox.getValue().toString());
        String flightNo = obFlightNameFld.getValue();
        String flightFrom = obFlightFromFld.getValue();
        String flightTo = obFlightToFld.getValue();
        if(noOfSectors == 0 || flightNo == null || flightNo.isEmpty() || flightFrom == null || flightFrom.isEmpty()
        || flightTo == null || flightTo.isEmpty()){
            Notification.show("Fill all required fields", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        item.setObNoOfSectors(noOfSectors);
        item.setObFlightNo(flightNo);
        item.setObFlightFrom(flightFrom);
        item.setObFlightTo(flightTo);
        UI.getCurrent().getSession().setAttribute("flight",item);
        return true;
    }
}
