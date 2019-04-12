package com.back.office.ui.dashboard;

import com.back.office.utils.Constants;
import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.ui.*;
import java.util.Map;

public class SubDashboardCommonView extends VerticalLayout implements View {

    private static final String imageWidth = "50%";
    VerticalLayout verticalLayout = new VerticalLayout();

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    protected void addMenuItems(HorizontalLayout horizontalLayout, Map<String,String> iconNameNavigatorMap,String resourceName){
        for(Map.Entry<String,String>  map : iconNameNavigatorMap.entrySet()){

            if(map.getKey().contains("empty")){
                VerticalLayout emptyLayout = new VerticalLayout();
                horizontalLayout.addComponents(emptyLayout);
            }
            else {
                Image authorizationImage = new Image(null, new ClassResource(resourceName));
                authorizationImage.setWidth(imageWidth);
                authorizationImage.setHeight(imageWidth);
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
