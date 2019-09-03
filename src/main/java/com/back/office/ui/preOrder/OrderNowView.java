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
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    Map<String, ItemDetails> itemIdNameMap;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public OrderNowView(){
        super();
        connection = DBConnection.getInstance();
        createMainLayout();
        setMargin(false);
        itemIdNameMap = connection.getItemCodeDetailsMap();
    }

    private void createMainLayout(){

        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        addComponent(headerLayout);
        headerLayout.setWidth("55%");
        headerLayout.setMargin(Constants.noMargin);
        userFormLayout = new VerticalLayout();
        addComponent(userFormLayout);
        userFormLayout.setMargin(false);
        tableLayout = new VerticalLayout();
        tableLayout.setSizeFull();
        tableLayout.setMargin(Constants.noMargin);
        Label h1 = new Label("Order Now");
        h1.addStyleName("headerText");
        headerLayout.addComponent(h1);

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setWidth("60%");
        firstRow.setMargin(Constants.noMargin);
        firstRow.addStyleName("report-filter-panel");

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

        HorizontalLayout submitBtnLayout=new HorizontalLayout();
        submitBtnLayout.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        submitBtnLayout.setStyleName("searchButton");
        submitBtnLayout.addComponent(search);
        firstRow.addComponent(submitBtnLayout);

        tableLayout.setWidth("90%");
        tableLayout.setMargin(false);

        Button exportToExcell=new Button();
        exportToExcell.setIcon(FontAwesome.FILE_EXCEL_O);

        Button print=new Button();
        print.setIcon(FontAwesome.PRINT);

        HorizontalLayout optionButtonRow = new HorizontalLayout();
        optionButtonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        optionButtonRow.setSpacing(true);
        optionButtonRow.setMargin(Constants.noMargin);
        optionButtonRow.addComponents(exportToExcell,print);
        userFormLayout.addComponent(optionButtonRow);
        userFormLayout.setComponentAlignment(optionButtonRow,Alignment.MIDDLE_RIGHT);

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
        if(index >= orderNowList.size()){
            orderNowList.add(orderNow);
        }
        else{
            orderNowList.set(index,orderNow);
        }
        connection.updateObjectHBM(orderNow);
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
        orderNowItemsFilterGrid.addColumn(bean -> getItemNameFromId(bean.getItemNo())).setCaption("Item Description");
        orderNowItemsFilterGrid.addColumn(OrderNowItems::getQuantity).setCaption("Quantity");
        windowData.setContent(table);
        table.addComponent(orderNowItemsFilterGrid);
        orderNowItemsFilterGrid.setItems(orderNowDataItem);
        UI.getCurrent().addWindow(windowData);

    }

    private String getItemNameFromId(String itemId){

        ItemDetails item = itemIdNameMap.get(itemId);
        if(item != null) return item.getItemName();
        else return "";
    }

    private void resetFields(){
        flightNoCombo.clear();
        destinationFld.clear();
        statusCombo.clear();
    }
}
