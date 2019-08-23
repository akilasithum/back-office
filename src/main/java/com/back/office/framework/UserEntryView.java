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
            UI.getCurrent().getSession().setAttribute("subMenu",new SubMenuItem());
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
