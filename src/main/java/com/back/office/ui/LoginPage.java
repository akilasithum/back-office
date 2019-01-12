package com.back.office.ui;

import com.back.office.MyUI;
import com.back.office.utils.Authentication;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ClassResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class LoginPage extends VerticalLayout implements View {
    private static final long serialVersionUID = 1L;
    public static final String NAME = "";
    public static Authentication AUTH;

    public LoginPage(){

        VerticalLayout mainLayout = new VerticalLayout();
        addComponent(mainLayout);
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        setSpacing(true);
        AUTH = new Authentication();
        Panel panel = new Panel("Login");
        panel.setSizeUndefined();
        Image logo = new Image();
        logo.setSource(new ClassResource("logo.png"));
        logo.setWidth(370, Unit.PIXELS);
        logo.setHeight(130, Unit.PIXELS);
        mainLayout.addComponent(logo);
        mainLayout.setSpacing(true);
        mainLayout.addComponent(panel);


        FormLayout content = new FormLayout();
        TextField username = new TextField("Username");
        content.addComponent(username);
        PasswordField password = new PasswordField("Password");
        content.addComponent(password);

        Button send = new Button("Login");
        send.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                if(AUTH.authenticate(username.getValue(), password.getValue())){
                    VaadinSession.getCurrent().setAttribute("user", username.getValue());
                    getSession().setAttribute("userName",username);
                    ((MyUI)getUI()).navigate();
                    getUI().getNavigator().navigateTo("common");
                    Page.getCurrent().reload();
                }else{
                    Notification.show("Invalid credentials", Notification.Type.ERROR_MESSAGE);
                }
            }

        });
        content.addComponent(send);
        content.setSizeUndefined();
        content.setMargin(true);
        panel.setContent(content);
        mainLayout.setComponentAlignment(logo,Alignment.MIDDLE_CENTER);
        mainLayout.setComponentAlignment(panel,Alignment.MIDDLE_CENTER);
        setComponentAlignment(mainLayout, Alignment.MIDDLE_CENTER);

    }

    @Override
    public void enter(ViewChangeEvent event) {

    }

}

