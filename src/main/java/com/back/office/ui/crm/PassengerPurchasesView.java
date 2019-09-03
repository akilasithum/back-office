package com.back.office.ui.crm;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.back.office.entity.PassengerPurchase;
import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;

import com.back.office.db.DBConnection;
import com.back.office.entity.ItemDetails;
import com.back.office.entity.PosItemSaleDetail;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.ItemClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class PassengerPurchasesView extends UserEntryView implements View{

    protected Button submitBtn;
    protected VerticalLayout createLayout;
    protected DBConnection connection;
    protected Grid<PassengerPurchase> PassengerPurchaseGrid;
    protected DateField fromDateText;
    protected DateField toDateText;
    protected ComboBox flightNumberText;
    protected List<PosItemSaleDetail> posItemList;
    protected Label itemLabel;
    protected Label posItem;
    protected Label posLabel;
    protected float priceAll;

    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }


    public PassengerPurchasesView() {
        super();
        connection=DBConnection.getInstance();
        createMainLayout();
        setMargin(false);
    }

    public void createMainLayout() {

        createLayout=new VerticalLayout();
        createLayout.setMargin(Constants.leftBottomtMargin);
        Label h1=new Label("Passenger Purchases");

        h1.addStyleName("headerText");
        createLayout.addComponent(h1);

        HorizontalLayout buttonLayoutSubmit=new HorizontalLayout();
        buttonLayoutSubmit.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        buttonLayoutSubmit.setStyleName("searchButton");
        HorizontalLayout formLayout = new HorizontalLayout();
        formLayout.addStyleName("report-filter-panel");
        formLayout.setSizeFull();
        formLayout.setWidth("60%");

        submitBtn =new Button("Submit");
        submitBtn.addClickListener((Button.ClickListener) ClickEvent->
                processList());

        flightNumberText=new ComboBox("Flight Number");
        flightNumberText.setItems(connection.getFlightsNoList());
        flightNumberText.setSizeFull();

        fromDateText=new DateField("Date From");
        fromDateText.setDescription("Date From");
        fromDateText.setRequiredIndicatorVisible(true);
        fromDateText.setStyleName("datePickerStyle");
        fromDateText.setSizeFull();

        toDateText=new DateField("Date To");
        toDateText.setDescription("Date To");
        toDateText.setRequiredIndicatorVisible(true);
        toDateText.setStyleName("datePickerStyle");
        toDateText.setSizeFull();

        formLayout.addComponent(fromDateText);
        formLayout.addComponent(toDateText);
        formLayout.addComponent(flightNumberText);
        formLayout.addComponent(buttonLayoutSubmit);

        buttonLayoutSubmit.addComponent(submitBtn);

        addComponent(createLayout);

        PassengerPurchaseGrid =new Grid();

        createLayout.addComponent(formLayout);
        Button exportToExcell=new Button();
        exportToExcell.setIcon(FontAwesome.FILE_EXCEL_O);

        Button print=new Button();
        print.setIcon(FontAwesome.PRINT);

        HorizontalLayout optionButtonRow = new HorizontalLayout();
        optionButtonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        optionButtonRow.setSpacing(true);
        optionButtonRow.setMargin(Constants.noMargin);
        optionButtonRow.addComponents(exportToExcell,print);
        createLayout.addComponent(optionButtonRow);
        createLayout.setComponentAlignment(optionButtonRow,Alignment.MIDDLE_RIGHT);
        createLayout.addComponent(PassengerPurchaseGrid);
        createLayout.setMargin(false);

        PassengerPurchaseGrid.setSizeFull();

        PassengerPurchaseGrid.addColumn(PassengerPurchase::getPaxName).setCaption("Pax Name");
        PassengerPurchaseGrid.addColumn(PassengerPurchase::getFrequentFlyerNo).setCaption("Frequent flyer No");
        PassengerPurchaseGrid.addColumn(PassengerPurchase::getEmail).setCaption("Email");
        PassengerPurchaseGrid.addColumn(PassengerPurchase::getTelephoneNumber).setCaption("Tel No");
        PassengerPurchaseGrid.addColumn(PassengerPurchase::getFlightId).setCaption("Flight No");
        PassengerPurchaseGrid.addColumn(bean-> BackOfficeUtils.getDateStringFromDate(bean.getFlightDate())).setCaption("Departure Date");
        PassengerPurchaseGrid.addColumn(PassengerPurchase::getPnr).setCaption("PNR");
        PassengerPurchaseGrid.addColumn(PassengerPurchase::getOrderId).setCaption("Order");
    }

    public void processList() {



        if(flightNumberText.getValue()!=null&&!flightNumberText.getValue().toString().isEmpty()&&fromDateText.getValue()!=null&&!fromDateText.getValue().toString().isEmpty()&&toDateText.getValue()!=null&&!toDateText.getValue().toString().isEmpty()) {

            List<com.back.office.entity.PassengerPurchase> flightDetailListdatelis=connection.getPassengerPurchase("allType",flightNumberText.getValue().toString(),Date.from(fromDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),Date.from(toDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            PassengerPurchaseGrid.setItems(flightDetailListdatelis);

        }else if(fromDateText.getValue()!=null&&!fromDateText.getValue().toString().isEmpty()&&toDateText.getValue()!=null&&!toDateText.getValue().toString().isEmpty()) {

            List<com.back.office.entity.PassengerPurchase> flightDetailListdatelis=connection.getPassengerPurchase("dateOnly","flightText",Date.from(fromDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),Date.from(toDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            PassengerPurchaseGrid.setItems(flightDetailListdatelis);

        }else if(flightNumberText.getValue()!=null&&!flightNumberText.getValue().toString().isEmpty()) {

            List<com.back.office.entity.PassengerPurchase> flightDetailListdatelis=connection.getPassengerPurchase("typeOnly",flightNumberText.getValue().toString(),new Date(),new Date());
            PassengerPurchaseGrid.setItems(flightDetailListdatelis);

        }else  {

            List<com.back.office.entity.PassengerPurchase> flightDetailListdatelis=connection.getPassengerPurchase("paxType","flightText",new Date(),new Date());
            PassengerPurchaseGrid.setItems(flightDetailListdatelis);

        }

        PassengerPurchaseGrid.addItemClickListener((ItemClickListener<PassengerPurchase>) itemClick -> {
            itemClick.getRowIndex();
            String orderText=itemClick.getItem().getOrderId();
            Window windowdatatable=new Window("Passenger Order Details");
            getUI().removeWindow(windowdatatable);

            posItemList=connection.getPosItemSale(orderText);

            Label itemHedderLabel=new Label("Item Name");
            Label labelHedder=new Label("Quantity");
            Label priceLabel=new Label("Price");

            VerticalLayout itemLayoutList=new VerticalLayout();
            VerticalLayout quntityLayoutList=new VerticalLayout();
            VerticalLayout priceLayoutText=new VerticalLayout();

            itemLayoutList.addComponent(itemHedderLabel);
            quntityLayoutList.addComponent(labelHedder);
            priceLayoutText.addComponent(priceLabel);

            priceAll=0;

            for(int i=0;i<posItemList.size();i++) {
                List<ItemDetails> itemGrid=connection.getItemId(posItemList.get(i).getitemId());

                itemLabel=new Label(itemGrid.get(0).getItemName());
                itemLayoutList.addComponent(itemLabel);
                String posItemsec=Integer.toString(posItemList.get(i).getquantity());
                String posItemLabel=Float.toString(posItemList.get(i).getprice());
                posItem=new Label(posItemsec);
                posLabel=new Label(posItemLabel);
                quntityLayoutList.addComponent(posItem);
                priceLayoutText.addComponent(posLabel);
                priceAll=posItemList.get(i).getprice()+priceAll;
            }

            VerticalLayout windowContent = new VerticalLayout();
            HorizontalLayout horizonLayout=new HorizontalLayout();
            windowContent.setMargin(true);
            windowdatatable.setContent(windowContent);
            windowContent.addComponent(horizonLayout);
            Button buttonclose=new Button("Ok");
            buttonclose.addClickListener((Button.ClickListener) clickEvent->closedatawindowh(windowdatatable));
            Label priceLabelText=new Label("Total : $ "+Float.toString(priceAll));
            horizonLayout.addComponent(itemLayoutList);
            horizonLayout.addComponent(quntityLayoutList);
            horizonLayout.addComponent(priceLayoutText);
            windowContent.addComponent(priceLabelText);
            windowContent.setComponentAlignment(priceLabelText,Alignment.BOTTOM_CENTER);
            windowdatatable.center();
            windowContent.addComponent(buttonclose);
            windowContent.setComponentAlignment(buttonclose,Alignment.BOTTOM_CENTER);

            windowdatatable.addStyleName("mywindowstyle");
            getUI().addWindow(windowdatatable);

        });




    }

    public void clearText() {
        fromDateText.clear();
        toDateText.clear();
        flightNumberText.clear();


    }

    public void closedatawindowh(Window windowList) {
        windowList.close();
    }
}

