package com.back.office.ui.message;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.back.office.utils.UserNotification;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;

import com.back.office.db.DBConnection;
import com.back.office.entity.BondMessageDetail;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.themes.ValoTheme;

public class BondMessagesView extends UserEntryView implements View{

    protected Button submitButton;
    protected VerticalLayout mainLayout;
    protected DBConnection connection;
    protected FilterGrid<BondMessageDetail> flightList;
    protected List<BondMessageDetail> flightDetList;
    protected Button clear;
    protected DateField craftDateText;
    protected ComboBox flightNumberList;
    protected com.vaadin.ui.TextArea bondMessage;
    private Window createMsgWindow;

    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public BondMessagesView() {
        super();
        connection=DBConnection.getInstance();
        createMainLayout();
        createMsgWindow = new Window("Create New Message");
    }

    public void createMainLayout() {

        mainLayout = new VerticalLayout();
        setMargin(Constants.noTopMargin);
        setSizeFull();
        mainLayout.setMargin(Constants.noMargin);

        Label h1=new Label("Messages to HHC");
        h1.addStyleName("headerText");
        mainLayout.addComponent(h1);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        MarginInfo marginInfo = new MarginInfo(false,false,false,false);
        buttonLayout.setMargin(marginInfo);
        buttonLayout.setSizeFull();
        mainLayout.addComponent(buttonLayout);

        Button addNewButton = new Button();
        addNewButton.setIcon(FontAwesome.PLUS);
        addNewButton.setStyleName("add_button");
        addNewButton.setSizeFull();
        addNewButton.addClickListener((Button.ClickListener) clickEvent -> openCreateMsgWindow());
        buttonLayout.addComponents(addNewButton);

        addComponent(mainLayout);
        flightList=new FilterGrid();
        flightList.setSizeFull();
        flightList.setWidth("70%");

        mainLayout.addComponent(flightList);
        dataInGrid();
    }

    public void openCreateMsgWindow() {

        if (!createMsgWindow.isAttached()) {
            createMsgWindow.setWidth("40%");
            createMsgWindow.setHeight(400, Unit.PIXELS);

            craftDateText=new DateField("Flight Date");
            craftDateText.setStyleName("datePickerStyle");

            flightNumberList=new ComboBox("Flight No");
            flightNumberList.setDescription("Flight No");
            flightNumberList.setItems(connection.getFlightsNoList());
            flightNumberList.setEmptySelectionAllowed(false);
            flightNumberList.setRequiredIndicatorVisible(true);

            bondMessage=new com.vaadin.ui.TextArea("Message");
            bondMessage.setSizeFull();
            bondMessage.setWidth("70%");

            submitButton=new Button("Submit");
            mainLayout.addComponent(submitButton);
            submitButton.addClickListener((Button.ClickListener) ClickEvent->
                    sendBondMessage());

            FormLayout formLayout =new FormLayout();
            formLayout.setSizeFull();
            formLayout.setMargin(true);

            formLayout.addComponent(flightNumberList);
            formLayout.addComponent(craftDateText);
            formLayout.addComponent(bondMessage);
            formLayout.addComponent(submitButton);

            createMsgWindow.center();
            createMsgWindow.setContent(formLayout);
            createMsgWindow.addStyleName("mywindowstyle");
            createMsgWindow.setModal(true);
            getUI().addWindow(createMsgWindow);
        }
    }

    public void dataInGrid() {
        flightDetList=connection.getBondMessageDetail();
        flightList.addColumn(BondMessageDetail::getFlightNo).setCaption("Flight No").setFilter(getColumnFilterField(),
                InMemoryFilter.StringComparator.containsIgnoreCase()).setExpandRatio(1);
        flightList.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getFlightDate())).setCaption("Dep Date").setFilter(getColumnFilterField(),
                InMemoryFilter.StringComparator.containsIgnoreCase()).setExpandRatio(1);
        flightList.addColumn(BondMessageDetail::getMessageBody).setCaption("Message").setFilter(getColumnFilterField(),
                InMemoryFilter.StringComparator.containsIgnoreCase()).setExpandRatio(4);
        flightList.setItems(flightDetList);
    }

    private TextField getColumnFilterField() {
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        return filter;

    }


    public void sendBondMessage() {

        if(flightNumberList.getValue()!=null&&!flightNumberList.getValue().toString().isEmpty()&&craftDateText.getValue()!=null&&!craftDateText.getValue().toString().isEmpty()&&bondMessage.getValue()!=null&&!bondMessage.getValue().toString().isEmpty()) {
            BondMessageDetail bondMessageText=new BondMessageDetail();
            bondMessageText.setFlightNo(flightNumberList.getValue().toString());
            bondMessageText.setFlightDate(Date.from(craftDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            bondMessageText.setMessageBody(bondMessage.getValue());
            int messageDetails;
            messageDetails = connection.insertObjectHBM(bondMessageText);
            bondMessageText.setBondMessageId(messageDetails);
            flightDetList=connection.getBondMessageDetail();
            flightList.setItems(flightDetList);
            UserNotification.show("Success","Message saved successfully","success",UI.getCurrent());
            createMsgWindow.close();

        }else {
            UserNotification.show("Error","Pleas Insert All field details","warning",UI.getCurrent());
        }
    }
}

