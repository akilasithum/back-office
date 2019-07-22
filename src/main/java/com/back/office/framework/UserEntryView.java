package com.back.office.framework;

import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;

public class UserEntryView extends VerticalLayout {

    public UserEntryView(){
        VerticalLayout header = BackOfficeUtils.getHeaderLayout();
        addComponent(header);
        setComponentAlignment(header, Alignment.MIDDLE_CENTER);
        setSpacing(false);
        setMargin(Constants.noTopMargin);
    }


}
