package com.back.office.ui;

import com.back.office.HybridUI;
import com.back.office.db.DBConnection;
import com.back.office.utils.Authentication;
import com.back.office.utils.Constants;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextField;
import org.vaadin.alump.fancylayouts.FancyNotifications;

import java.awt.*;

public class LoginPage extends VerticalLayout implements View {
    private static final long serialVersionUID = 1L;
    public static final String NAME = "";
    public static Authentication AUTH;
    DBConnection connection;
    FancyNotifications notifications;
    private VerticalLayout logoLayout = new VerticalLayout();

    public LoginPage(){

        VerticalLayout mainLayout = new VerticalLayout();
        notifications = new FancyNotifications();
        addComponent(notifications);
        notifications.setClickClose(true);
        mainLayout.setSizeUndefined();
        mainLayout.setMargin(true);
        setSpacing(true);
        AUTH = new Authentication();
        VerticalLayout panel = new VerticalLayout();
        panel.setSizeUndefined();

        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(Constants.leftMargin);
        addComponent(layout);
        addComponent(mainLayout);
        layout.addComponent(logoLayout);
        logoLayout.setMargin(Constants.noMargin);
        setMargin(Constants.noMargin);

        Image logo = new Image();
        logo.setSource(new ClassResource("logo.png"));
        logo.setWidth(170, Unit.PIXELS);
        logo.setHeight(50, Unit.PIXELS);
        logoLayout.addComponent(logo);


        mainLayout.setSpacing(true);
        panel.setSizeUndefined();
        mainLayout.addComponent(panel);
        connection = DBConnection.getInstance();

        FormLayout content = new FormLayout();
        TextField username = new TextField("Username");
        content.addComponent(username);
        PasswordField password = new PasswordField("Password");
        content.addComponent(password);
        ComboBox baseStation = new ComboBox("Base Station");
        baseStation.setItems("YYZ","YUL");
        content.addComponent(baseStation);

        Button send = new Button("Login");
        send.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                if(baseStation.getValue() != null && !baseStation.getValue().toString().isEmpty()){
                    if(connection.isLoginSuccessful(username.getValue(), password.getValue())){
                        VaadinSession.getCurrent().setAttribute("user", username.getValue());
                        getSession().setAttribute("userName",username.getValue());
                        getSession().setAttribute("baseStation",baseStation.getValue().toString());
                        ((HybridUI)getUI()).navigate();
                    }else{
                        notifications.showNotification(null, "Error",
                                "Invalid credentials!", null,
                                "notification-error");
                    }
                }
                else{
                    notifications.showNotification(null, "Missing",
                            "Select base station", null,
                            "notification-warning");
                }
            }

        });
        content.addComponent(send);
        content.setSizeUndefined();
        content.setMargin(true);
        panel.addComponent(content);
        //mainLayout.setComponentAlignment(logo,Alignment.MIDDLE_CENTER);
        mainLayout.setComponentAlignment(panel,Alignment.MIDDLE_CENTER);
        setComponentAlignment(mainLayout, Alignment.MIDDLE_CENTER);

    }

   /* public void init() {
        notifications
                .addListener((FancyNotifications.NotificationsListener) id -> {

                    String msg;
                    if (id != null && id instanceof Button) {
                        Button button = (Button) id;
                        msg = button.getCaption() + " clicked";
                    } else {
                        return;
                    }

                    LoginPage.this.notifications.showNotification(
                            null, "click!", msg);
                });

    }*/

    @Override
    public void enter(ViewChangeEvent event) {

    }

}

