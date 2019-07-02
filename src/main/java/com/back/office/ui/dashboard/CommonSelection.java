package com.back.office.ui.dashboard;

import com.back.office.entity.SubMenuItem;
import com.back.office.utils.Constants;
import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommonSelection extends VerticalLayout implements View {

    VerticalLayout mainLayout = new VerticalLayout();

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public CommonSelection(){
        createLayout();
    }

    private void createLayout(){
        addComponent(mainLayout);
        mainLayout.setStyleName("main-layout");
        mainLayout.setSizeFull();
        MarginInfo info = new MarginInfo(true);
        info.setMargins(true,false,false,false);
        mainLayout.setMargin(info);
        setComponentAlignment(mainLayout, Alignment.MIDDLE_CENTER);
        SubMenuItem menuItem = (SubMenuItem)UI.getCurrent().getSession().getAttribute("subMenu");
        Map<String,String> subMenuMap = menuItem.getSubMenuImageMap();
        int subMenuCount = subMenuMap.size();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Map<String,String> subMap = new LinkedHashMap<>();
        int i = 0;
        for(Map.Entry<String,String> map : subMenuMap.entrySet()){
            if(i%4 == 0){
                if(i != 0){
                    horizontalLayout.setSizeFull();
                    addMenuItems(horizontalLayout,subMap,menuItem.getMenuImage());
                    mainLayout.addComponent(horizontalLayout);
                }
                horizontalLayout = new HorizontalLayout();
                subMap = new LinkedHashMap<>();
                subMap.put(map.getKey(),map.getValue());
            }
            else{
                subMap.put(map.getKey(),map.getValue());

            }
            if(subMenuMap.size() == i+1){
                horizontalLayout.setSizeFull();
                if(subMap.size() != 4) {
                    int count = 4-subMap.size();
                    for (int j = 0; j < count; j++) {
                        subMap.put("empty" + j, "");
                    }
                }
                addMenuItems(horizontalLayout,subMap,menuItem.getMenuImage());
                mainLayout.addComponent(horizontalLayout);
            }
            i++;

        }
    }

    protected void addMenuItems(HorizontalLayout horizontalLayout, Map<String,String> iconNameNavigatorMap, String resourceName){
        for(Map.Entry<String,String>  map : iconNameNavigatorMap.entrySet()){

            if(map.getKey().contains("empty")){
                VerticalLayout emptyLayout = new VerticalLayout();
                horizontalLayout.addComponents(emptyLayout);
            }
            else {
                Image authorizationImage = new Image(null, new ClassResource(resourceName));
                authorizationImage.setWidth("50%");
                authorizationImage.setHeight("50%");
                VerticalLayout aircraftTypeLayout = new VerticalLayout();
                aircraftTypeLayout.setMargin(Constants.noMargin);
                aircraftTypeLayout.addComponent(authorizationImage);
                Label label = new Label(map.getKey());
                label.setStyleName("itemLabel");
                aircraftTypeLayout.addComponent(label);
                aircraftTypeLayout.addStyleName("my-img-button");
                aircraftTypeLayout.setComponentAlignment(authorizationImage, Alignment.MIDDLE_CENTER);
                aircraftTypeLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
                aircraftTypeLayout.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent ->
                        getUI().getNavigator().navigateTo(map.getValue())
                );
                horizontalLayout.addComponent(aircraftTypeLayout);
                horizontalLayout.setComponentAlignment(aircraftTypeLayout, Alignment.MIDDLE_CENTER);
            }
        }
    }
}
