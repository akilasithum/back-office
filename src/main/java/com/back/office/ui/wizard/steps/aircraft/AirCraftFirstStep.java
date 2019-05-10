package com.back.office.ui.wizard.steps.aircraft;

import com.back.office.entity.AircraftDetails;
import com.back.office.entity.ItemDetails;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.ArrayList;
import java.util.List;

public class AirCraftFirstStep implements WizardStep {

    TextField registrationNumberFld;
    TextField aircraftType;
    ComboBox economyClassSeatsComboBox;
    ComboBox businessClassSeatsComboBox;
    CheckBox activeCheckBox;

    @Override
    public String getCaption() {
        return "Main Details";
    }

    @Override
    public Component getContent() {
        FormLayout form = new FormLayout();

        registrationNumberFld = new TextField("Registration Number");
        registrationNumberFld.setRequiredIndicatorVisible(true);
        form.addComponent(registrationNumberFld);

        aircraftType = new TextField("Aircraft Type");
        aircraftType.setRequiredIndicatorVisible(true);
        form.addComponent(aircraftType);

        businessClassSeatsComboBox = new ComboBox("Business class seats");
        businessClassSeatsComboBox.setItems(getNumberList(20));
        businessClassSeatsComboBox.setRequiredIndicatorVisible(true);
        form.addComponent(businessClassSeatsComboBox);

        economyClassSeatsComboBox = new ComboBox("Economy class seats");
        economyClassSeatsComboBox.setItems(getNumberList(300));
        economyClassSeatsComboBox.setRequiredIndicatorVisible(true);
        form.addComponent(economyClassSeatsComboBox);

        activeCheckBox = new CheckBox("Active", true);
        form.addComponent(activeCheckBox);
        fillFieldsIfAddedAlready();
        return form;
    }

    @Override
    public boolean onAdvance() {
        String regNo = registrationNumberFld.getValue();
        String type = aircraftType.getValue();
        String businessClassPaxCount = String.valueOf(businessClassSeatsComboBox.getValue());
        String econClassPaxCount = String.valueOf(economyClassSeatsComboBox.getValue());
        boolean active = activeCheckBox.getValue();
        if(regNo == null || "null".equals(regNo) || type == null || "null".equals(type) || businessClassPaxCount == null ||
        "null".equals(businessClassPaxCount) || econClassPaxCount == null || "null".equals(econClassPaxCount)){
            Notification.show("Fill all required fields", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        else{
            Object obj = UI.getCurrent().getSession().getAttribute("aircraft");
            AircraftDetails item;
            if(obj != null){
                item = (AircraftDetails)obj;
            }
            else item = new AircraftDetails();

            item.setRegistrationNumber(regNo);
            item.setAircraftName(type);
            item.setBusinessClassSeatCount(Integer.parseInt(businessClassPaxCount));
            item.setEcoClassSeatCount(Integer.parseInt(econClassPaxCount));
            item.setActive(active);
            UI.getCurrent().getSession().setAttribute("aircraft",item);
        }
        return true;
    }

    @Override
    public boolean onBack() {
        return false;
    }

    private List<Integer> getNumberList(int number){
        List<Integer> list = new ArrayList<>();
        for(int i = 0;i<=number;i++){
            list.add(i);
        }
        return list;
    }

    private void fillFieldsIfAddedAlready(){
        Object obj = UI.getCurrent().getSession().getAttribute("aircraft");
        AircraftDetails item;
        if(obj != null && obj instanceof AircraftDetails){
            item = (AircraftDetails)obj;
            registrationNumberFld.setValue(item.getRegistrationNumber());
            aircraftType.setValue(item.getAircraftName());
            businessClassSeatsComboBox.setValue(item.getBusinessClassSeatCount());
            economyClassSeatsComboBox.setValue(item.getEcoClassSeatCount());
            activeCheckBox.setValue(item.isActive());
        }
    }
}
