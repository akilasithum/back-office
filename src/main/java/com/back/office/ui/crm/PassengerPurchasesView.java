package com.back.office.ui.crm;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.back.office.entity.PassengerPurchases;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;

import com.back.office.db.DBConnection;
import com.back.office.entity.ItemDetails;
import com.back.office.entity.PosItemSaleDetail;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
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

public class PassengerPurchasesView extends VerticalLayout implements View{

    protected Button submitList;
    protected VerticalLayout createLayout;
    protected DBConnection connection;
    protected Grid<PassengerPurchases> passengerPurchasesGrid;
    protected Button clearButton;
    protected DateField fromDateText;
    protected DateField toDateText;
    protected ComboBox flightNumberText;
    protected List<PosItemSaleDetail> posItemList;
    protected Label itemLabel;
    protected Label posItem;
    protected Label posLabel;
    protected float priceAll;
    Window windowdatatable=new Window();

    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }


    public PassengerPurchasesView() {
        connection=DBConnection.getInstance();
        createMainLayout();
    }

    public void createMainLayout() {

        createLayout=new VerticalLayout();
        setStyleName("backColorGrey");
        createLayout.setMargin(Constants.leftBottomtMargin);
        Label h1=new Label("Passenger Purchases");

        h1.addStyleName(ValoTheme.LABEL_H1);
        createLayout.addComponent(h1);

        HorizontalLayout buttonLayoutSubmit=new HorizontalLayout();
        HorizontalLayout dateText=new HorizontalLayout();
        HorizontalLayout dateTextListDetails=new HorizontalLayout();


        submitList=new Button("Submit");
        createLayout.addComponent(submitList);
        submitList.addClickListener((Button.ClickListener) ClickEvent->
                processList());

        clearButton=new Button("Clear");
        buttonLayoutSubmit.addComponent(clearButton);
        clearButton.addClickListener((Button.ClickListener) ClickEvent->
                clearText());

        flightNumberText=new ComboBox("Flight Number");
        flightNumberText.setItems(connection.getFlightsNoList());

        fromDateText=new DateField("Date From");
        fromDateText.setDescription("Date From");
        fromDateText.setRequiredIndicatorVisible(true);

        toDateText=new DateField("Date To");
        toDateText.setDescription("Date To");
        toDateText.setRequiredIndicatorVisible(true);

        dateText.addComponent(fromDateText);
        dateText.addComponent(toDateText);

        dateTextListDetails.addComponent(flightNumberText);

        buttonLayoutSubmit.addComponent(submitList);

        buttonLayoutSubmit.addComponent(clearButton);

        addComponent(createLayout);

        passengerPurchasesGrid =new Grid();

        createLayout.addComponent(dateText);
        createLayout.addComponent(dateTextListDetails);
        createLayout.addComponent(buttonLayoutSubmit);
        createLayout.addComponent(passengerPurchasesGrid);

        passengerPurchasesGrid.setSizeFull();
        passengerPurchasesGrid.setWidth("60%");

        passengerPurchasesGrid.addColumn(PassengerPurchases::getPNR).setCaption("PNR");
        passengerPurchasesGrid.addColumn(PassengerPurchases::getpaxName).setCaption("Pax Name");
        passengerPurchasesGrid.addColumn(PassengerPurchases::getemail).setCaption("Email");
        passengerPurchasesGrid.addColumn(PassengerPurchases::gettelephoneNo).setCaption("Telephone nNo");
        passengerPurchasesGrid.addColumn(PassengerPurchases::getfrequentFlyerNo).setCaption("Frequent flyer No");
        passengerPurchasesGrid.addColumn(bean-> BackOfficeUtils.getDateStringFromDate(bean.getdeparureDate())).setCaption("Departure Date");
        passengerPurchasesGrid.addColumn(PassengerPurchases::getflightNo).setCaption("FlightNo");
        passengerPurchasesGrid.addColumn(PassengerPurchases::getorderId).setCaption("Order Id");
    }

    public void processList() {



        if(flightNumberText.getValue()!=null&&!flightNumberText.getValue().toString().isEmpty()&&fromDateText.getValue()!=null&&!fromDateText.getValue().toString().isEmpty()&&toDateText.getValue()!=null&&!toDateText.getValue().toString().isEmpty()) {

            List<com.back.office.entity.PassengerPurchases> flightDetailListdatelis=connection.getPassengerPurchase("allType",flightNumberText.getValue().toString(),Date.from(fromDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),Date.from(toDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            passengerPurchasesGrid.setItems(flightDetailListdatelis);

        }else if(fromDateText.getValue()!=null&&!fromDateText.getValue().toString().isEmpty()&&toDateText.getValue()!=null&&!toDateText.getValue().toString().isEmpty()) {

            List<com.back.office.entity.PassengerPurchases> flightDetailListdatelis=connection.getPassengerPurchase("dateOnly","flightText",Date.from(fromDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),Date.from(toDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            passengerPurchasesGrid.setItems(flightDetailListdatelis);

        }else if(flightNumberText.getValue()!=null&&!flightNumberText.getValue().toString().isEmpty()) {

            List<com.back.office.entity.PassengerPurchases> flightDetailListdatelis=connection.getPassengerPurchase("typeOnly",flightNumberText.getValue().toString(),new Date(),new Date());
            passengerPurchasesGrid.setItems(flightDetailListdatelis);

        }else  {

            List<com.back.office.entity.PassengerPurchases> flightDetailListdatelis=connection.getPassengerPurchase("paxType","flightText",new Date(),new Date());
            passengerPurchasesGrid.setItems(flightDetailListdatelis);

        }

        passengerPurchasesGrid.addItemClickListener(new ItemClickListener<com.back.office.entity.PassengerPurchases>() {

            public void itemClick(Grid.ItemClick<com.back.office.entity.PassengerPurchases> itemClick) {
                itemClick.getRowIndex();
                int rowItem=itemClick.getRowIndex();


                String orderText=itemClick.getItem().getorderId();

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
                    int itemText=posItemList.get(i).getitemId();
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

            }
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

