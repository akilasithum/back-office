package com.back.office.ui.wizard.steps.flight;

import com.back.office.entity.Flight;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

public class FlightInboundCommonStep implements WizardStep {
    private final String FLIGHT_NAME = "Flight No";
    private final String FLIGHT_FROM = "Flight From";
    private final String FLIGHT_TO = "Flight To";
    private final String NO_OF_SECTORS = "No of Sectors";

    protected TextField ibFlightNameFld;
    protected TextField ibFlightFromFld;
    protected TextField ibFlightToFld;
    protected ComboBox ibNoOfSectorsComboBox;

    @Override
    public String getCaption() {
        return "Inbound Common Details";
    }

    @Override
    public Component getContent() {
        FormLayout form = new FormLayout();
        ibFlightNameFld = new TextField(FLIGHT_NAME);
        ibFlightNameFld.setDescription(FLIGHT_NAME);
        ibFlightNameFld.setRequiredIndicatorVisible(true);
        form.addComponent(ibFlightNameFld);


        ibNoOfSectorsComboBox = new ComboBox(NO_OF_SECTORS);
        ibNoOfSectorsComboBox.setDescription(NO_OF_SECTORS);
        ibNoOfSectorsComboBox.setItems("1","2","3");
        ibNoOfSectorsComboBox.setEmptySelectionAllowed(false);
        ibNoOfSectorsComboBox.setRequiredIndicatorVisible(true);
        ibNoOfSectorsComboBox.setValue("1");
        form.addComponent(ibNoOfSectorsComboBox);

        ibFlightFromFld = new TextField(FLIGHT_FROM);
        ibFlightFromFld.setDescription(FLIGHT_FROM);
        ibFlightFromFld.setRequiredIndicatorVisible(true);
        form.addComponent(ibFlightFromFld);

        ibFlightToFld = new TextField(FLIGHT_TO);
        ibFlightToFld.setDescription(FLIGHT_TO);
        ibFlightToFld.setRequiredIndicatorVisible(true);
        form.addComponent(ibFlightToFld);
        fillFieldsIfAddedAlready();
        return form;
    }

    private void fillFieldsIfAddedAlready(){
        Object obj = UI.getCurrent().getSession().getAttribute("flight");
        Flight item;
        if(obj != null && obj instanceof Flight){
            item = (Flight)obj;
            if(item.getIbFlightNo() != null) ibFlightNameFld.setValue(item.getIbFlightNo());
            if(item.getIbFlightFrom() != null) ibFlightFromFld.setValue(item.getIbFlightFrom());
            if(item.getIbFlightTo() != null) ibFlightToFld.setValue(item.getIbFlightTo());
            if(item.getIbNoOfSectors() != 0) ibNoOfSectorsComboBox.setValue(String.valueOf(item.getIbNoOfSectors()));
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
        int noOfSectors =  Integer.parseInt(ibNoOfSectorsComboBox.getValue().toString());
        String flightNo = ibFlightNameFld.getValue();
        String flightFrom = ibFlightFromFld.getValue();
        String flightTo = ibFlightToFld.getValue();
        if(noOfSectors == 0 || flightNo == null || flightNo.isEmpty() || flightFrom == null || flightFrom.isEmpty()
                || flightTo == null || flightTo.isEmpty()){
            Notification.show("Fill all required fields", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        item.setIbNoOfSectors(noOfSectors);
        item.setIbFlightNo(flightNo);
        item.setIbFlightFrom(flightFrom);
        item.setIbFlightTo(flightTo);
        UI.getCurrent().getSession().setAttribute("flight",item);
        return true;
    }
}
