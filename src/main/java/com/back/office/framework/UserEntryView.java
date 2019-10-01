package com.back.office.framework;

import com.back.office.entity.SubMenuItem;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserEntryView extends VerticalLayout {

    public UserEntryView(){
        createMenu();
    }

    public UserEntryView(boolean isRequired){

        if(isRequired){
            UI.getCurrent().getCurrent().getSession().setAttribute("selectedLayout","pre order");
            SubMenuItem menuItem = new SubMenuItem();
            menuItem.setMenuImage("pre_order_sub.png");
            Map<String,String> row1Map = new LinkedHashMap<>();
            row1Map.put("Pre-order Summary","PreOrders");
            row1Map.put("Pre-order Inventory","");
            row1Map.put("Messaging","MessagingModule");
            row1Map.put("Messages to HHC","BondMessages");
            row1Map.put("HHC FA","FAMessages");
            menuItem.setSubMenuImageMap(row1Map);
            menuItem.setSubMenuImageMap(row1Map);
            menuItem.setMenuName("pre_order");
            UI.getCurrent().getSession().setAttribute("subMenu",menuItem);
            createMenu();
        }
    }

    private void createMenu(){
        VerticalLayout header = BackOfficeUtils.getHeaderLayout();
        addComponent(header);
        setComponentAlignment(header, Alignment.MIDDLE_CENTER);
        setSpacing(false);
        setMargin(Constants.noTopMargin);
    }
}
