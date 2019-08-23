package com.back.office.ui.preOrder;

import com.back.office.db.DBConnection;
import com.back.office.entity.*;
import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;

public class OrderNowView extends UserEntryView implements View {

    ComboBox flightNoCombo;
    ComboBox statusCombo;
    TextField destinationFld;

    Button search;
    protected VerticalLayout userFormLayout;
    protected VerticalLayout tableLayout;
    DBConnection connection;

    private final String FLIGHT_NO = "Flight #";
    private final String TIME = "Time";
    private final String DESTINATION = "Destination";
    private final String PAX_NAME = "Pax Name";
    private final String PNR = "PNR";
    private final String SEAT_NO = "Seat No";
    private final String ORDER_NO = "Order No";
    private final String ORDER_IN_TIME = "Order in Time";
    private final String STATUS = "Status";

    FilterGrid<OrderNow> orderNowFilterGrid;
    List<OrderNow> orderNowList;
    Window windowData;
    FilterGrid<OrderNowItems> orderNowItemsFilterGrid;
    List<OrderNowItems> orderNowDataItem;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public OrderNowView(){
        super();
        connection = DBConnection.getInstance();
        createMainLayout();
    }

    private void createMainLayout(){

        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        addComponent(headerLayout);
        headerLayout.setWidth("55%");
        headerLayout.setMargin(Constants.noMargin);
        userFormLayout = new VerticalLayout();
        addComponent(userFormLayout);
        userFormLayout.setMargin(Constants.bottomMarginInfo);
        tableLayout = new VerticalLayout();
        tableLayout.setSizeFull();
        addComponent(tableLayout);
        tableLayout.setMargin(Constants.noMargin);
        Label h1 = new Label("Order Now");
        h1.addStyleName("headerText");
        headerLayout.addComponent(h1);

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setWidth("50%");
        firstRow.setMargin(Constants.noMargin);
        userFormLayout.addComponent(firstRow);

        flightNoCombo = new ComboBox("Flight No");
        flightNoCombo.setItems(connection.getFlightsNoList());
        firstRow.addComponent(flightNoCombo);
        flightNoCombo.setSizeFull();

        destinationFld = new TextField("Destination");
        destinationFld.setDescription("Destination");
        destinationFld.setSizeFull();
        firstRow.addComponent(destinationFld);

        statusCombo = new ComboBox("Status");
        statusCombo.setItems("Pending","Packed","Rejected");
        statusCombo.setSizeFull();
        firstRow.addComponent(statusCombo);

        userFormLayout.addComponent(firstRow);


        search = new Button("Search");
        search.setSizeFull();
        search.addClickListener((Button.ClickListener) event -> {
            Object flightNoList= flightNoCombo.getValue();
            Object destinationList= destinationFld.getValue();
            Object statusList= statusCombo.getValue();
            setData(flightNoList,destinationList,statusList);

        });

        tableLayout.setWidth("90%");
        tableLayout.setMargin(false);
        search.setWidth("10%");
        userFormLayout.addComponent(search);
        addComponent(userFormLayout);

        VerticalLayout tableLayout=new VerticalLayout();
        tableLayout.setMargin(false);
        addComponent(tableLayout);

        orderNowList = new ArrayList<>();
        orderNowFilterGrid = new FilterGrid<>();
        orderNowFilterGrid.setItems(orderNowList);
        orderNowFilterGrid.setSizeFull();
        orderNowFilterGrid.addColumn(OrderNow::getFlightNo).setCaption(FLIGHT_NO);
        orderNowFilterGrid.addColumn(OrderNow::getTime).setCaption(TIME);
        orderNowFilterGrid.addColumn(OrderNow::getDestination).setCaption(DESTINATION);
        orderNowFilterGrid.addColumn(OrderNow::getPaxName).setCaption(PAX_NAME);
        orderNowFilterGrid.addColumn(OrderNow::getPNR).setCaption(PNR);
        orderNowFilterGrid.addColumn(OrderNow::getSeatNo).setCaption(SEAT_NO);
        orderNowFilterGrid.addColumn(OrderNow::getOrderNo).setCaption(ORDER_NO);
        orderNowFilterGrid.addColumn(bean-> BackOfficeUtils.getDateStringFromDate(bean.getOrderInTime())).setCaption(ORDER_IN_TIME);
        orderNowFilterGrid.addColumn(OrderNow::getStatus).setCaption(STATUS);
        GridContextMenu<OrderNow> gridMenu = new GridContextMenu<>(orderNowFilterGrid);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);

        orderNowFilterGrid.addItemClickListener(event->{
            showDetailWindow(event.getItem());
        });

        userFormLayout.addComponent(tableLayout);
        tableLayout.addComponent(orderNowFilterGrid);

    }

    protected void updateGridBodyMenu(GridContextMenu.GridContextMenuOpenListener.GridContextMenuOpenEvent<?> event) {
        event.getContextMenu().removeItems();
        if (event.getItem() != null) {
            event.getContextMenu().addItem("Pack Order", VaadinIcons.MEDAL, selectedItem -> {
                ConfirmDialog.show(getUI(), "Pack Order", "Are you sure you want to pack this order?",
                        "Yes", "No", new ConfirmDialog.Listener() {
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    packOrRejectOrder((OrderNow) event.getItem(),"pack");
                                }
                            }
                        });
            });
            event.getContextMenu().addItem("Reject Order", VaadinIcons.FOLDER_REMOVE, selectedItem -> {
                ConfirmDialog.show(getUI(), "Reject Order", "Are you sure you want to reject this order?",
                        "Yes", "No", new ConfirmDialog.Listener() {
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    packOrRejectOrder((OrderNow)event.getItem(),"reject");
                                }
                            }
                        });
            });
        }
    }

    private void packOrRejectOrder(OrderNow orderNow,String type){
        int index = orderNowList.indexOf(orderNow);
        orderNowList.remove(orderNow);
        orderNow.setStatus(type);
        orderNowList.set(index,orderNow);
        orderNowFilterGrid.setItems(orderNowList);
    }

    private void setData(Object flightNo,Object destination,Object status){

        orderNowList =(List<OrderNow>)connection.getOrderNow(flightNo, destination, status);
        orderNowFilterGrid.setItems(orderNowList);

    }

    private void showDetailWindow(OrderNow orderNow) {
        orderNowDataItem=new ArrayList<>();
        orderNowItemsFilterGrid =new FilterGrid<>();
        orderNowItemsFilterGrid.removeAllColumns();
        orderNowDataItem=(List<OrderNowItems>)connection.getOrderNowItem(orderNow.getOrderNo());
        windowData=new Window("Order Details");
        windowData.center();

        VerticalLayout table=new VerticalLayout();
        orderNowItemsFilterGrid.addColumn(OrderNowItems::getItemNo).setCaption("Item No");
        orderNowItemsFilterGrid.addColumn(OrderNowItems::getItemDescription).setCaption("Item Description");
        orderNowItemsFilterGrid.addColumn(OrderNowItems::getQuantity).setCaption("Quantity");
        windowData.setContent(table);
        table.addComponent(orderNowItemsFilterGrid);
        orderNowItemsFilterGrid.setItems(orderNowDataItem);
        UI.getCurrent().addWindow(windowData);

    }

    private void resetFields(){
        flightNoCombo.clear();
        destinationFld.clear();
        statusCombo.clear();
    }
}
