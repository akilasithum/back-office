package com.back.office.ui.message;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;

import com.back.office.db.DBConnection;
import com.back.office.entity.BondMessageDetail;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class BondMessagesView extends UserEntryView implements View{

    protected Button submitButton;
    protected VerticalLayout createLayout;
    protected DBConnection connection;
    protected FilterGrid<BondMessageDetail> flightList;
    protected List<BondMessageDetail> flightDetList;
    protected Button clear;
    protected DateField craftDateText;
    protected ComboBox flightNumberList;
    protected com.vaadin.ui.TextArea bondMessage;
    private DateField flightFromDate;
    private DateField flightToDate;
    private ComboBox flightNumberCombo;

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

    }

    public void createMainLayout() {

        createLayout=new VerticalLayout();
        setMargin(Constants.noTopMargin);
        setSizeFull();
        createLayout.setMargin(Constants.noMargin);

        Label h1=new Label("Messages to HHC");

        h1.addStyleName("headerText");
        createLayout.addComponent(h1);

        submitButton=new Button("Submit");
        createLayout.addComponent(submitButton);
        submitButton.addClickListener((Button.ClickListener) ClickEvent->
                processList());

        clear=new Button("Clear");
        createLayout.addComponent(clear);
        clear.addClickListener((Button.ClickListener) ClickEvent->
                clearText());

        craftDateText=new DateField("Flight Date");

        flightNumberList=new ComboBox("Flight Number");
        flightNumberList.setDescription("Flight Number");
        flightNumberList.setItems(connection.getFlightsNoList());
        flightNumberList.setEmptySelectionAllowed(false);
        flightNumberList.setRequiredIndicatorVisible(true);

        bondMessage=new com.vaadin.ui.TextArea("Message");
        bondMessage.setSizeFull();
        bondMessage.setWidth("30%");


        FormLayout buttonLayotText=new FormLayout();
        HorizontalLayout buttonLayoutSubmit=new HorizontalLayout();
        buttonLayoutSubmit.setMargin(Constants.noMargin);

        addComponent(createLayout);


        flightList=new FilterGrid();
        flightList.setSizeFull();
        flightList.setWidth("50%");
        buttonLayotText.addComponent(flightNumberList);
        buttonLayotText.addComponent(craftDateText);
        buttonLayotText.addComponent(bondMessage);
        buttonLayoutSubmit.addComponent(submitButton);
        buttonLayoutSubmit.addComponent(clear);
        createLayout.addComponent(buttonLayotText);
        createLayout.addComponent(buttonLayoutSubmit);
        createLayout.addComponent(flightList);
        dataInGrid();
    }

    public void dataInGrid() {
        flightDetList=connection.getBondMessageDetail();
        flightList.addColumn(BondMessageDetail::getFlightNo).setCaption("Flight Number").setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        flightList.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getFlightDate())).setCaption("Flight Date").setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        flightList.addColumn(BondMessageDetail::getMessageBody).setCaption("Message").setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        flightList.setItems(flightDetList);
    }

    private void clearText() {
        flightNumberList.clear();
        craftDateText.clear();
        bondMessage.clear();
    }

    private TextField getColumnFilterField() {
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        return filter;

    }


    public void processList() {

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


        }else {
            Notification.show("Error","Pleas Insert All field details",Notification.Type.WARNING_MESSAGE);
        }


    }
}

