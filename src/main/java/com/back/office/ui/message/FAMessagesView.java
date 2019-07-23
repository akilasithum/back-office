package com.back.office.ui.message;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import org.vaadin.addons.filteringgrid.FilterGrid;

import com.back.office.db.DBConnection;
import com.back.office.entity.FaMessage;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class FAMessagesView extends UserEntryView implements View{

    protected Button submitButton;
    protected VerticalLayout createLayout;
    protected DBConnection connection;
    protected FilterGrid<FaMessage> faMessageFilterGrid;
    protected List<FaMessage> flightDetList;
    protected Button clear;
    protected DateField fromDateText;
    protected DateField toDateText;
    protected ComboBox flightNumberList;


    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public FAMessagesView() {
        super();
        connection=DBConnection.getInstance();
        createMainLayout();

    }

    public void createMainLayout() {

        createLayout=new VerticalLayout();
        setMargin(Constants.leftMargin);
        setSizeFull();

        Label h1=new Label("FA Messages");

        h1.addStyleName("headerText");
        createLayout.addComponent(h1);

        submitButton=new Button("Submit");
        submitButton.addClickListener((Button.ClickListener) ClickEvent->
                processList());

        fromDateText=new DateField("From");
        fromDateText.setRequiredIndicatorVisible(true);
        toDateText=new DateField("To");
        toDateText.setRequiredIndicatorVisible(true);


        flightNumberList=new ComboBox("Flight Number");
        flightNumberList.setDescription("Flight Number");
        flightNumberList.setItems(connection.getFlightsNoList());
        flightNumberList.setEmptySelectionAllowed(false);

        HorizontalLayout buttonLayoutSubmit=new HorizontalLayout();
        addComponent(createLayout);

        faMessageFilterGrid =new FilterGrid();
        buttonLayoutSubmit.addComponent(fromDateText);
        buttonLayoutSubmit.addComponents(toDateText,flightNumberList);
        faMessageFilterGrid.setSizeFull();
        faMessageFilterGrid.setWidth("50%");

        createLayout.addComponent(buttonLayoutSubmit);
        //createLayout.addComponent(flightNumberList);
        createLayout.addComponent(submitButton);
        createLayout.addComponent(faMessageFilterGrid);

        faMessageFilterGrid.addColumn(FaMessage::getflightNumber).setCaption("Flight Number");
        faMessageFilterGrid.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getflightDate())).setCaption("Flight Date");
        faMessageFilterGrid.addColumn(FaMessage::getfaName).setCaption("FA Name");
        faMessageFilterGrid.addColumn(FaMessage::getcomment).setCaption("Comment").setWidth(400);
    }

    private TextField getColumnFilterField() {
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        return filter;

    }


    public void processList() {

        if(fromDateText.getValue()!=null&&!fromDateText.getValue().toString().isEmpty()&&toDateText.getValue()!=null&&!toDateText.getValue().toString().isEmpty()) {

            String flightNumberField=flightNumberList.getValue()== null || flightNumberList.getValue().toString().isEmpty() ?
                    null : String.valueOf(flightNumberList.getValue());
            Date craftDateFieldFrom=Date.from(fromDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date craftDateFieldTo=Date.from(toDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            faMessageFilterGrid.setVisible(true);

            flightDetList=connection.getFaMessages(flightNumberField,craftDateFieldFrom,craftDateFieldTo);

            faMessageFilterGrid.setItems(flightDetList);

        }else {
            Notification.show("Error","Pleas enter date range",Notification.Type.WARNING_MESSAGE);

        }


    }
}

