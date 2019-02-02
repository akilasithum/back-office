package com.back.office.ui.uploads;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ErrorView extends VerticalLayout implements View {
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Label label = new Label("Not implemented yet");
        addComponent(label);
    }
}
