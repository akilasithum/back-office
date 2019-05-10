package com.back.office.ui.wizard.steps.flight;

import com.back.office.entity.Flight;
import com.back.office.entity.Sector;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.HashMap;
import java.util.Map;

public class InboundSectorsStep implements WizardStep {

    VerticalLayout mainLayout;
    Flight flight;
    int noOfSectors;

    @Override
    public String getCaption() {
        return "Inbound Sectors";
    }

    @Override
    public Component getContent() {
        mainLayout = new VerticalLayout();
        int sectorCount = getObSectors();
        for(int i=1;i<=sectorCount;i++) {
            mainLayout.addComponents(BackOfficeUtils.getSectorFormLayout(i));
        }
        fillFieldsIfAddedAlready();
        return mainLayout;
    }

    @Override
    public boolean onAdvance() {
        return saveSectors();
    }

    @Override
    public boolean onBack() {
        return saveSectors();
    }

    private boolean saveSectors(){
        Map<Integer,Sector> sectorMap = new HashMap<>();
        Object obj = UI.getCurrent().getSession().getAttribute("ibSectorMap");
        if(obj != null && obj instanceof Map){
            sectorMap = (Map<Integer, Sector>) obj;
        }

        for(int i = 1 ;i<= noOfSectors;i++){
            FormLayout formLayout = (FormLayout) mainLayout.getComponent(i-1);
            String sectorFrom = ((TextField)formLayout.getComponent(1)).getValue();
            String sectorTo = ((TextField)formLayout.getComponent(2)).getValue();
            String flightType = String.valueOf(((ComboBox)formLayout.getComponent(3)).getValue());
            if(sectorFrom == null || sectorFrom.isEmpty() || sectorTo == null || sectorTo.isEmpty() || flightType == null || flightType.isEmpty()){
                Notification.show("Fill all the fields", Notification.Type.ERROR_MESSAGE);
                return false;
            }
            else{
                Sector sector = new Sector();
                sector.setSectorFrom(sectorFrom);
                sector.setSectorTo(sectorTo);
                sector.setFlightType("Inbound");
                sector.setSectorType(flightType);
                sectorMap.put(i,sector);
            }
        }
        if(noOfSectors < sectorMap.size()){
            Map<Integer,Sector> newMap = new HashMap<>();
            for(int i = 1;i<=noOfSectors;i++){
                newMap.put(i,sectorMap.get(i));
            }
            UI.getCurrent().getSession().setAttribute("ibSectorMap",newMap);
        }
        else {
            UI.getCurrent().getSession().setAttribute("ibSectorMap", sectorMap);
        }
        return true;
    }

    private void fillFieldsIfAddedAlready(){
        Object obj = UI.getCurrent().getSession().getAttribute("IbSectorMap");
        Map<Integer,Sector> item;
        int i = 1;
        if(obj != null){
            item = (Map<Integer,Sector>)obj;
            for(Map.Entry<Integer,Sector> map : item.entrySet()){
                Sector sector = map.getValue();
                if(sector.getSectorType() != null && sector.getSectorType().equals("Inbound") && i <= noOfSectors){
                    FormLayout formLayout = (FormLayout) mainLayout.getComponent(i-1);
                    ((TextField)formLayout.getComponent(1)).setValue(sector.getSectorFrom());
                    ((TextField)formLayout.getComponent(2)).setValue(sector.getSectorTo());
                    ((ComboBox)formLayout.getComponent(3)).setValue(sector.getSectorType());
                    i++;
                }
            }
        }
    }

    private int getObSectors(){
        Object obj = UI.getCurrent().getSession().getAttribute("flight");
        if(obj != null && obj instanceof Flight){
            flight = (Flight)obj;
            noOfSectors = flight.getIbNoOfSectors();
            return noOfSectors;
        }
        return 0;
    }
}
