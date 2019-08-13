package com.back.office.ui.dashboard;

import com.back.office.entity.SubMenuItem;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.util.*;

public class CommonSelection extends VerticalLayout implements View {

    VerticalLayout mainLayout = new VerticalLayout();

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public CommonSelection()
    {
        setMargin(Constants.noTopMargin);
        setSpacing(false);
        String selected = (String)UI.getCurrent().getSession().getAttribute("selectedLayout");
        createIconLayout(selected);
        createLayout();
    }

    private void createLayout(){
        addComponent(mainLayout);
        mainLayout.setStyleName("main-layout");
        mainLayout.setSizeFull();
        mainLayout.setWidth("80%");
        MarginInfo info = new MarginInfo(true);
        info.setMargins(true,false,false,false);
        mainLayout.setMargin(info);
        setComponentAlignment(mainLayout, Alignment.MIDDLE_CENTER);
        SubMenuItem menuItem = (SubMenuItem)UI.getCurrent().getSession().getAttribute("subMenu");
        Map<String,String> subMenuMap = menuItem.getSubMenuImageMap();
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

    private void createIconLayout(String selectedLayout){
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setWidth("80%");
        HorizontalLayout btnLayout1 = new HorizontalLayout();
        btnLayout1.setSizeFull();
        verticalLayout.setStyleName("iconLayoutBackground");
        //btnLayout1.setMargin(Constants.bottomMarginInfo);
        verticalLayout.setMargin(Constants.noMargin);
        verticalLayout.addComponent(btnLayout1);

        CssLayout iconWrapper1 = BackOfficeUtils.getMainLayoutBtn("flight Schedule");
        if(selectedLayout.equalsIgnoreCase("flight Schedule")) iconWrapper1.setStyleName("selected");
        else iconWrapper1.setStyleName("iconWrapper-11");

        CssLayout iconWrapper2 = BackOfficeUtils.getMainLayoutBtn("pre order");
        if(selectedLayout.equalsIgnoreCase("pre order")) iconWrapper2.setStyleName("selected");
        else iconWrapper2.setStyleName("iconWrapper-21");

        CssLayout iconWrapper3 = BackOfficeUtils.getMainLayoutBtn("Reports and Finance");
        if(selectedLayout.equalsIgnoreCase("Reports and Finance")) iconWrapper3.setStyleName("selected");
        else iconWrapper3.setStyleName("iconWrapper-31");

        CssLayout iconWrapper4 = BackOfficeUtils.getMainLayoutBtn("Airline Specific");
        if(selectedLayout.equalsIgnoreCase("Airline Specific")) iconWrapper4.setStyleName("selected");
        else iconWrapper4.setStyleName("iconWrapper-41");

        CssLayout iconWrapper5 = BackOfficeUtils.getMainLayoutBtn("Vendor");
        if(selectedLayout.equalsIgnoreCase("Vendor")) iconWrapper5.setStyleName("selected");
        else iconWrapper5.setStyleName("iconWrapper-41");

        CssLayout iconWrapper6 = BackOfficeUtils.getMainLayoutBtn("crm");
        if(selectedLayout.equalsIgnoreCase("crm")) iconWrapper6.setStyleName("selected");
        else iconWrapper6.setStyleName("iconWrapper-31");

        CssLayout iconWrapper7 = BackOfficeUtils.getMainLayoutBtn("Bag Trace");
        if(selectedLayout.equalsIgnoreCase("Bag Trace")) iconWrapper7.setStyleName("selected");
        else iconWrapper7.setStyleName("iconWrapper-21");

        CssLayout iconWrapper8 = BackOfficeUtils.getMainLayoutBtn("setup");
        if(selectedLayout.equalsIgnoreCase("setup")) iconWrapper8.setStyleName("selected");
        else iconWrapper8.setStyleName("iconWrapper-11");

        btnLayout1.addComponents(iconWrapper1,iconWrapper2,iconWrapper3,iconWrapper4);
        btnLayout1.setComponentAlignment(iconWrapper1, Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper2,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper3,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper4,Alignment.MIDDLE_CENTER);
        btnLayout1.addComponents(iconWrapper5,iconWrapper6,iconWrapper7,iconWrapper8);
        btnLayout1.setComponentAlignment(iconWrapper5, Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper6,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper7,Alignment.MIDDLE_CENTER);
        btnLayout1.setComponentAlignment(iconWrapper8,Alignment.MIDDLE_CENTER);

        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }

    protected void addMenuItems(HorizontalLayout horizontalLayout, Map<String,String> iconNameNavigatorMap, String resourceName){
        for(Map.Entry<String,String>  map : iconNameNavigatorMap.entrySet()){

            if(map.getKey().contains("empty")){
                VerticalLayout emptyLayout = new VerticalLayout();
                horizontalLayout.addComponents(emptyLayout);
            }
            else {
                Image imageComponent = new Image(null, new ClassResource(resourceName));
                imageComponent.setWidth("50%");
                imageComponent.setHeight("50%");
                VerticalLayout layout = new VerticalLayout();
                layout.setMargin(Constants.noMargin);
                layout.addComponent(imageComponent);
                Label label = new Label(map.getKey());
                label.setStyleName("itemLabel");
                layout.addComponent(label);
                layout.addStyleName("my-img-button");
                layout.setComponentAlignment(imageComponent, Alignment.MIDDLE_CENTER);
                layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
                List<String> setupList = Arrays.asList("Bags","Upgrades","Credit","Transport","Meals","Hotels","Excursions"
                ,"Gif Cards","Order Now");
                if(setupList.contains(map.getKey())){
                    layout.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent ->{
                        UI.getCurrent().getSession().setAttribute("setupSubMenu",map.getKey());
                        getUI().getNavigator().navigateTo(map.getValue());
                    }
                    );
                }
                else{
                    layout.addLayoutClickListener((LayoutEvents.LayoutClickListener) layoutClickEvent ->
                            getUI().getNavigator().navigateTo(map.getValue())
                    );
                }

                horizontalLayout.addComponent(layout);
                horizontalLayout.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
            }
        }
    }
}
