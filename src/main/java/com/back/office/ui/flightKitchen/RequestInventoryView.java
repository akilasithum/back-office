package com.back.office.ui.flightKitchen;

import com.back.office.entity.ItemDetails;
import com.back.office.entity.RequestInventory;
import com.back.office.entity.RequestInventoryItem;
import com.back.office.ui.salesReports.ReportCommonView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.back.office.utils.UserNotification;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.ss.usermodel.*;
import org.vaadin.addons.filteringgrid.FilterGrid;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestInventoryView extends ReportCommonView implements View{
    private ComboBox reqFromCombo;
    private ComboBox itemNumberCB;
    private ComboBox itemNameCB;
    private TextField orderQuntity;
    private FilterGrid<RequestInventoryItem> itemDetailsFilterGrid;
    private Button submitButton;
    protected ArrayList arrayListDetailNumber;
    protected ArrayList arrayListDetailName;
    protected String itemName="Item Name";
    protected List<RequestInventoryItem> requestInventoryItems =new ArrayList<>();
    protected File file=new File("Request.xlsx");
    protected FileResource fir=new FileResource(file);
    protected FileDownloader fid=new FileDownloader(fir);
    HorizontalLayout footerBtnLayout;
    Window addInventoryWindow;
    protected Button addNewButton;
    private final String FLIGHT_DATE_FROM = "Date (From)";
    private final String FLIGHT_DATE_TO = "Date (To)";
    protected DateField flightDateFromDateField;
    protected DateField flightDateToDateField;
    protected Grid<RequestInventory> detailsTable;

    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public RequestInventoryView() {
        super();
    }

    public void createMainLayout() {
        super.createMainLayout();
        addInventoryWindow = new Window("Add Inventory");

        addNewButton = new Button();
        addNewButton.setIcon(FontAwesome.PLUS);
        addNewButton.setStyleName("add_button");
        addNewButton.setSizeFull();
        addNewButton.addClickListener((Button.ClickListener) clickEvent -> openAddInventoryWindow());

        additionalBtnLayout.addComponents(addNewButton);

        List<ItemDetails> itemsDetail=connection.getItemDetails("detaSelectNumber","*");

        int listSizeDetailsNumber=itemsDetail.size();
        arrayListDetailNumber=new ArrayList<String>();
        for(int i=1;i<listSizeDetailsNumber;i++) {
            arrayListDetailNumber.add(itemsDetail.get(i).getItemCode());
        }

        int listSizeDetailsName=itemsDetail.size();
        arrayListDetailName=new ArrayList<String>();
        for(int i=0;i<listSizeDetailsName;i++) {
            arrayListDetailName.add(itemsDetail.get(i).getItemName());
        }

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setWidth("60%");
        firstRow.setMargin(Constants.noMargin);
        firstRow.addStyleName("report-filter-panel");
        mainUserInputLayout.addComponent(firstRow);

        Date date = new Date();
        LocalDate today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        flightDateFromDateField = new DateField(FLIGHT_DATE_FROM);
        flightDateFromDateField.setValue(today);
        flightDateFromDateField.setStyleName("datePickerStyle");
        flightDateFromDateField.setSizeFull();
        firstRow.addComponent(flightDateFromDateField);

        flightDateToDateField = new DateField(FLIGHT_DATE_TO);
        flightDateToDateField.setValue(today);
        flightDateToDateField.setSizeFull();
        flightDateToDateField.setStyleName("datePickerStyle");
        firstRow.addComponents(flightDateToDateField,buttonRow);

        detailsTable = new Grid<>();
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setSizeFull();
        tableLayout.addComponent(detailsTable);
        detailsTable.addItemClickListener(event -> {
           openViewItemsWindow(event.getItem());
        });
        firstRow.setWidth("50%");
        createShowTableHeader();
    }

    private void createShowTableHeader() {
        detailsTable.addColumn(RequestInventory::getReqInventoryId).setCaption("Req #");
        detailsTable.addColumn(RequestInventory::getRequestedWareHouse).setCaption("Base Station");
        detailsTable.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getRequestedDate())).setCaption("Date");
        detailsTable.addColumn(RequestInventory::getRequestedUser).setCaption("Requested By");
        detailsTable.addColumn(RequestInventory::getRequestedFrom).setCaption("Requested From");

    }

    @Override
    protected Sheet getWorkbook(Sheet sheet) {
        return null;
    }

    @Override
    protected void defineStringFields() {
        this.pageHeader = "Request Inventory";
        this.reportExcelHeader = "Request Inventory";
    }

    @Override
    protected void showFilterData() {
        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(flightDateToDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        String outputStr = "Flight Date From " + BackOfficeUtils.getDateFromDateTime(dateFrom) +
                " , To " + BackOfficeUtils.getDateFromDateTime(dateTo);
        filterCriteriaText.setValue(outputStr);

        List<RequestInventory> list = connection.getRequestInventory(dateFrom,dateTo);
        detailsTable.setItems(list);
    }

    private void openViewItemsWindow(RequestInventory inventory){

        List<RequestInventoryItem> items = connection.getRequestItems(inventory.getReqInventoryId());

        if(!addInventoryWindow.isAttached()){
            VerticalLayout windowContent = new VerticalLayout();
            windowContent.setMargin(true);
            addInventoryWindow.setContent(windowContent);
            addInventoryWindow.setWidth("600px");
            addInventoryWindow.setCaption("View Requested Inventory");

            itemDetailsFilterGrid =new FilterGrid();
            itemDetailsFilterGrid.addColumn(RequestInventoryItem::getItemCode).setCaption("Item #");
            itemDetailsFilterGrid.addColumn(RequestInventoryItem::getItemName).setCaption("Description");
            itemDetailsFilterGrid.addColumn(RequestInventoryItem::getQty).setCaption("Qty");
            itemDetailsFilterGrid.setSizeFull();
            itemDetailsFilterGrid.setWidth("80%");
            itemDetailsFilterGrid.setItems(items);
            windowContent.addComponents(itemDetailsFilterGrid);

            HorizontalLayout submitBtnLayout = new HorizontalLayout();
            Button downloadPdfBtn = new Button("Download PDF");
            downloadPdfBtn.setIcon(VaadinIcons.DOWNLOAD);
            downloadPdfBtn.addClickListener(event -> downloadPDF());
            submitBtnLayout.setMargin(true);
            submitBtnLayout.addComponents(downloadPdfBtn);
            windowContent.addComponents(submitBtnLayout);

            addInventoryWindow.center();
            addInventoryWindow.addStyleName("mywindowstyle");
            getUI().addWindow(addInventoryWindow);
        }
    }

    private void openAddInventoryWindow(){

        if(!addInventoryWindow.isAttached()){
            VerticalLayout windowContent = new VerticalLayout();
            windowContent.setMargin(true);
            addInventoryWindow.setContent(windowContent);
            addInventoryWindow.setWidth("1000px");

            HorizontalLayout formLayout = new HorizontalLayout();
            formLayout .setSizeFull();
            formLayout.addStyleName("report-filter-panel");
            formLayout .setMargin(false);

            reqFromCombo =new ComboBox("Requested From");
            reqFromCombo.setDescription("Requested From");
            reqFromCombo.setItems("Kitchen","Commissary","Warehouse");
            //UI.getCurrent().getSession().getAttribute("baseStation").toString();
            reqFromCombo.setSizeFull();
            reqFromCombo.setEmptySelectionAllowed(false);
            reqFromCombo.setRequiredIndicatorVisible(true);

            itemNumberCB=new ComboBox("Item Number");
            itemNumberCB.setDescription("Item Number");
            itemNumberCB.setItems(arrayListDetailNumber);
            itemNumberCB.setEmptySelectionAllowed(false);
            itemNumberCB.setRequiredIndicatorVisible(true);
            itemNumberCB.setSizeFull();
            itemNumberCB.addSelectionListener((SingleSelectionListener) ClickEvent->itemNumberSelect(itemNumberCB.getValue().toString()));

            itemNameCB=new ComboBox("Item Name");
            itemNameCB.setDescription("Item Name");
            itemNameCB.setItems(arrayListDetailName);
            itemNameCB.setEmptySelectionAllowed(false);
            itemNameCB.setRequiredIndicatorVisible(true);
            itemNameCB.setSizeFull();
            itemNameCB.addSelectionListener((SingleSelectionListener) ClickEvent->itemNameSelect(itemNameCB.getValue().toString()));

            orderQuntity=new TextField("Order Qty");
            orderQuntity.setDescription("Order Qty");
            orderQuntity.setRequiredIndicatorVisible(true);
            orderQuntity.setSizeFull();

            submitButton=new Button("Add");
            submitButton.addClickListener((Button.ClickListener) ClickEvent->
                    detailsItemList());

            windowContent.addComponent(formLayout);
            formLayout.addComponent(reqFromCombo);
            formLayout.addComponent(itemNumberCB);
            formLayout.addComponent(itemNameCB);
            formLayout.addComponent(orderQuntity);
            HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
            buttonLayout.setStyleName("searchButton");
            buttonLayout.addComponents(submitButton);
            formLayout.setWidth("95%");
            formLayout.addComponent(buttonLayout);

            itemDetailsFilterGrid =new FilterGrid();
            itemDetailsFilterGrid.addColumn(RequestInventoryItem::getItemCode).setCaption("Item Number");
            itemDetailsFilterGrid.addColumn(RequestInventoryItem::getItemName).setCaption("Item Name");
            itemDetailsFilterGrid.addColumn(RequestInventoryItem::getQty).setCaption("Order Qty");
            itemDetailsFilterGrid.addColumn(
                    person -> showDownloadBtn(person),
                    new ComponentRenderer()
            ).setCaption("Del").setWidth(80);
            itemDetailsFilterGrid.setSizeFull();

            VerticalLayout tableLayout = new VerticalLayout();
            tableLayout.setSizeFull();
            tableLayout.setMargin(Constants.topMarginInfo);
            tableLayout.setWidth("95%");

            windowContent.addComponent(tableLayout);
            tableLayout.addComponents(itemDetailsFilterGrid);

            HorizontalLayout submitBtnLayout = new HorizontalLayout();
            Button submitInventoryButton = new Button("Submit");
            submitInventoryButton.setIcon(FontAwesome.SAVE);
            Button downloadPdfBtn = new Button("Download PDF");
            downloadPdfBtn.setIcon(VaadinIcons.DOWNLOAD);
            downloadPdfBtn.addClickListener(event -> downloadPDF());
            submitBtnLayout.setMargin(true);
            submitBtnLayout.addComponents(submitInventoryButton,downloadPdfBtn);
            windowContent.addComponents(submitBtnLayout);
            submitInventoryButton.addClickListener(event -> saveReqInventory());

            itemDetailsFilterGrid.setSizeFull();
            addInventoryWindow.center();
            addInventoryWindow.addStyleName("mywindowstyle");
            getUI().addWindow(addInventoryWindow);
        }
    }

    private void downloadPDF(){

    }

    private void saveReqInventory(){

        if(reqFromCombo.getValue() == null){
            UserNotification.show("Error","'Requested from' is missing.","error",UI.getCurrent());
            return;
        }
        else if(requestInventoryItems != null && !requestInventoryItems.isEmpty()){
            RequestInventory requestInventory = new RequestInventory();
            requestInventory.setRequestedDate(new Date());
            Object userName = UI.getCurrent().getSession().getAttribute("userName");
            requestInventory.setRequestedUser(String.valueOf(userName));
            requestInventory.setRequestedWareHouse("YYZ");
            requestInventory.setRequestedFrom(String.valueOf(reqFromCombo.getValue()));
            int id = connection.insertObjectHBM(requestInventory);
            requestInventoryItems.forEach( i -> i.setReqInventoryId(id));
            requestInventoryItems.forEach(i -> connection.insertObjectHBM(i));
            UserNotification.show("Success","Request inventory added successfully.","warning",UI.getCurrent());
            addInventoryWindow.close();
        }
        else{
            UserNotification.show("Error","Add items before save.","error",UI.getCurrent());
        }
    }

    private Button showDownloadBtn(RequestInventoryItem sifDetails){
        Button btn = new Button();
        btn.setSizeFull();
        btn.setStyleName("downloadBtn");
        btn.setIcon(VaadinIcons.FILE_REMOVE);
        btn.addClickListener(event -> {deleteItem(sifDetails);});
        return btn;
    }

    private void deleteItem(RequestInventoryItem selectedItems){

        if(selectedItems == null){
            UserNotification.show("Error","Select items to delete","warning",UI.getCurrent());
            return;
        }
        requestInventoryItems.remove(selectedItems);
        itemDetailsFilterGrid.setItems(requestInventoryItems);
    }

    public void itemNumberSelect(String selectionValue) {
        List<ItemDetails> itemsDetail=connection.getItemDetails(selectionValue,"itemName");
        String itemName=itemsDetail.get(0).getItemName();
        itemNameCB.setValue(itemName);

    }
    public void itemNameSelect(String selctionValue) {
        List<ItemDetails> itemsDetail=connection.getItemDetails("iteNumbe",selctionValue);
        String itemNumber=itemsDetail.get(0).getItemCode();
        itemNumberCB.setValue(itemNumber);
    }

    public void detailsItemList() {
        if(reqFromCombo.getValue()!=null&&!reqFromCombo.isEmpty()&&itemNameCB.getValue()!=null&
                !itemNameCB.isEmpty()&&itemNumberCB.getValue()!=null&&!itemNumberCB.isEmpty()&&orderQuntity.getValue()!=null&&!orderQuntity.isEmpty()) {
            try {

                RequestInventoryItem itemDetailListGrid=new RequestInventoryItem();
                    itemDetailListGrid.setItemCode(itemNumberCB.getValue().toString());
                    itemDetailListGrid.setItemName(itemNameCB.getValue().toString());
                    itemDetailListGrid.setQty(Integer.parseInt(orderQuntity.getValue()));

                    requestInventoryItems.add(itemDetailListGrid);

                    itemDetailsFilterGrid.setItems(requestInventoryItems);

            }catch(Exception e) {
                UserNotification.show("Error","Please Enter Number in order","warning",UI.getCurrent());
            }


        }else {
            UserNotification.show("Error","Pleas Enter All Details","warning",UI.getCurrent());
        }



    }
}

