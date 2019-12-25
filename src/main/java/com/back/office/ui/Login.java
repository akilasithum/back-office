package com.back.office.ui;

import com.back.office.HybridUI;
import com.back.office.db.DBConnection;
import com.back.office.utils.UserNotification;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.util.Arrays;

public class Login extends VerticalLayout implements View {
    private static final long serialVersionUID = 1L;
    public static final String NAME = "";
    DBConnection connection;
    private VerticalLayout logoLayout = new VerticalLayout();
    MarginInfo leftMargin = new MarginInfo(false,false,false,true);
    public static MarginInfo noMargin = new MarginInfo(false,false,false,false);

    public Login(){
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(leftMargin);
        addComponent(layout);
        logoLayout.setMargin(noMargin);
        Label loginImage = new Label();
        Image logo = new Image();
        logo.setSource(new ClassResource("logo.png"));
        logo.setWidth(140, Unit.PIXELS);
        logoLayout.addComponent(logo);
        logoLayout.setComponentAlignment(logo,Alignment.MIDDLE_CENTER);
        logoLayout.setStyleName("porter-logo");

        VerticalLayout mainLayout = new VerticalLayout();
        addComponent(mainLayout);
        mainLayout.setSizeFull();
        //mainLayout.setMargin(true);
        setSpacing(true);
        setMargin(noMargin);
        VerticalLayout panel = new VerticalLayout();
        panel.setStyleName("login-form");
        panel.setSizeUndefined();
        mainLayout.setSpacing(true);
        mainLayout.addComponent(panel);

        connection = DBConnection.getInstance();


        VerticalLayout content = new VerticalLayout();
        TextField username = new TextField("Username");
        username.setIcon(FontAwesome.USER);
        username.setStyleName("login-textbox");
        PasswordField password = new PasswordField("Password");
        password.setIcon(FontAwesome.LOCK);
        password.setStyleName("login-textbox");
        ComboBox baseStationCombo = new ComboBox("Base Station");
        baseStationCombo.setIcon(FontAwesome.PLANE);
        baseStationCombo.setStyleName("dropdownStyle");
        baseStationCombo.setItems(Arrays.asList("YYZ","YUL"));



        Button send = new Button("Login");
        send.setStyleName("loginButton");
        send.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(connection.isLoginSuccessful(username.getValue(), password.getValue())){
                    VaadinSession.getCurrent().setAttribute("user", username.getValue());
                    getSession().setAttribute("userName",username.getValue());
                    getSession().setAttribute("baseStation",baseStationCombo.getValue().toString());
                    ((HybridUI)getUI()).navigate();
                }else{
                    UserNotification.show("Error","Invalid credentials", "error",UI.getCurrent());
                }
            }

        });
        content.setSizeFull();
        content.setStyleName("login-wrapper");
        loginImage.setStyleName("login-image");
        content.addComponent(loginImage);
        content.addComponent(logoLayout);
        content.addComponent(username);
        content.addComponent(password);
        content.addComponent(baseStationCombo);
        content.addComponent(send);
        content.setSizeUndefined();
        content.setMargin(true);
        panel.addComponent(content);
        panel.setComponentAlignment(content,Alignment.TOP_CENTER);
        mainLayout.setComponentAlignment(panel,Alignment.MIDDLE_CENTER);
        setComponentAlignment(mainLayout, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}


