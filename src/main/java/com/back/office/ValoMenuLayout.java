package com.back.office;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @since
 * @author Vaadin Ltd
 */
public class ValoMenuLayout extends HorizontalLayout {

    CssLayout contentArea = new CssLayout();
    CssLayout menuArea = new CssLayout();
    VerticalLayout separator = new VerticalLayout();

    public ValoMenuLayout() {
        setSizeFull();

        menuArea.setPrimaryStyleName(ValoTheme.MENU_ROOT);

        contentArea.setPrimaryStyleName("valo-content");
        contentArea.addStyleName("v-scrollable");
        contentArea.setSizeFull();
        separator.setWidth(100,Unit.PIXELS);
        separator.addStyleName("backgroundColor");
        separator.setSizeFull();
        Button btn = new Button("a");
        separator.addComponent(btn);
        addComponents(menuArea, contentArea);
        setExpandRatio(contentArea, 1);
        contentArea.setWidth("95%");
        setComponentAlignment(contentArea, Alignment.MIDDLE_CENTER);
    }

    public ComponentContainer getContentContainer() {
        return contentArea;
    }

    public void addMenu(Component menu) {
        menu.addStyleName(ValoTheme.MENU_PART);
        menuArea.addComponent(menu);
    }

}
