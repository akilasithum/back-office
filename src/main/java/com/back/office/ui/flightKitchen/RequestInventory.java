package com.back.office.ui.flightKitchen;

import com.back.office.db.DBConnection;
import com.back.office.entity.ItemDetails;
import com.back.office.framework.UserEntryView;
import com.back.office.utils.Constants;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.vaadin.addons.filteringgrid.FilterGrid;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RequestInventory extends UserEntryView implements View{
    private ComboBox baseStationCB;
    private ComboBox itemNumberCB;
    private ComboBox itemNameCB;
    private TextField orderQuntity;
    private FilterGrid<ItemDetails> itemDetailsFilterGrid;
    private Button submitButton;
    private Button deleteButton;
    private Button printButton;
    private Button downloadPdfButton;
    protected DBConnection connection;
    protected ArrayList arrayListDetailNumber;
    protected ArrayList arrayListDetailName;
    protected String itemName="Item Name";
    protected List<ItemDetails> setList=new ArrayList<>();
    protected File file=new File("Request.xlsx");
    protected FileResource fir=new FileResource(file);
    protected FileDownloader fid=new FileDownloader(fir);
    HorizontalLayout footerBtnLayout;


    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public RequestInventory() {
        super();
        connection=DBConnection.getInstance();
        createMainLayout();
    }

    public void createMainLayout() {
        FormLayout formLayoutList = new FormLayout();
        formLayoutList .setSizeFull();
        formLayoutList .setMargin(Constants.leftMargin);

        HorizontalLayout firstRow = new HorizontalLayout();
        Label h1=new Label("Request Inventory");
        h1.addStyleName("headerText");
        firstRow.addComponent(h1);

        baseStationCB=new ComboBox("Base Station");
        baseStationCB.setDescription("Base Station");
        baseStationCB.setItems("YYZ","YYL");
        String baseStation = UI.getCurrent().getSession().getAttribute("baseStation").toString();
        baseStationCB.setValue(baseStation);
        baseStationCB.setEmptySelectionAllowed(false);
        baseStationCB.setRequiredIndicatorVisible(true);
        addComponent(firstRow);

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

        itemNumberCB=new ComboBox("Item Number");
        itemNumberCB.setDescription("Item Number");
        itemNumberCB.setItems(arrayListDetailNumber);
        itemNumberCB.setEmptySelectionAllowed(false);
        itemNumberCB.setRequiredIndicatorVisible(true);
        itemNumberCB.addSelectionListener((SingleSelectionListener) ClickEvent->itemNumberSelect(itemNumberCB.getValue().toString()));

        itemNameCB=new ComboBox("Item Name");
        itemNameCB.setDescription("Item Name");
        itemNameCB.setItems(arrayListDetailName);
        itemNameCB.setEmptySelectionAllowed(false);
        itemNameCB.setRequiredIndicatorVisible(true);
        itemNameCB.addSelectionListener((SingleSelectionListener) ClickEvent->itemNameSelect(itemNameCB.getValue().toString()));

        orderQuntity=new TextField("Order Qty");
        orderQuntity.setDescription("Order Qty");
        orderQuntity.setRequiredIndicatorVisible(true);

        submitButton=new Button("Add");
        submitButton.addClickListener((Button.ClickListener) ClickEvent->
                detailsItemList());
        deleteButton = new Button("Delete");
        deleteButton.addClickListener((Button.ClickListener) clickEvent -> deleteItem());

        itemDetailsFilterGrid =new FilterGrid();

        itemDetailsFilterGrid.addColumn(ItemDetails::getItemCode).setCaption("Item Number");
        itemDetailsFilterGrid.addColumn(ItemDetails::getItemName).setCaption("Item Name");
        itemDetailsFilterGrid.addColumn(ItemDetails::getorderQuntity).setCaption("Order Qty");

        addComponent(formLayoutList);
        firstRow.addComponent(h1);
        formLayoutList.addComponent(baseStationCB);
        formLayoutList.addComponent(itemNumberCB);
        formLayoutList.addComponent(itemNameCB);
        formLayoutList.addComponent(orderQuntity);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponents(submitButton,deleteButton);
        formLayoutList.addComponent(buttonLayout);
        formLayoutList.addComponent(itemDetailsFilterGrid);
        itemDetailsFilterGrid.setSizeFull();
        itemDetailsFilterGrid.setWidth("70%");

        downloadPdfButton=new Button("Download to Excel");

        printButton = new Button("Print");
        footerBtnLayout = new HorizontalLayout();
        footerBtnLayout.addComponents(downloadPdfButton,printButton);
        formLayoutList.addComponent(footerBtnLayout);
        footerBtnLayout.setVisible(false);

    }

    private void deleteItem(){

        Set<ItemDetails> selectedItems = itemDetailsFilterGrid.getSelectedItems();
        if(selectedItems == null || selectedItems.isEmpty()){
            Notification.show("Select items to delete",Notification.Type.WARNING_MESSAGE);
            return;
        }
        setList.removeAll(selectedItems);
        itemDetailsFilterGrid.setItems(setList);
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
        if(baseStationCB.getValue()!=null&&!baseStationCB.isEmpty()&&itemNameCB.getValue()!=null&
                !itemNameCB.isEmpty()&&itemNumberCB.getValue()!=null&&!itemNumberCB.isEmpty()&&orderQuntity.getValue()!=null&&!orderQuntity.isEmpty()) {
            try {
                String orderQuntityDetails=orderQuntity.getValue();
                int orderQuntityDetailsN=Integer.parseInt(orderQuntityDetails);


                    ItemDetails itemDetailListGrid=new ItemDetails();
                    itemDetailListGrid.setItemCode(itemNumberCB.getValue().toString());

                    itemDetailListGrid.setItemName(itemNameCB.getValue().toString());
                    itemDetailListGrid.setorerQuntity(orderQuntity.getValue());

                    setList.add(itemDetailListGrid);

                    itemDetailsFilterGrid.setItems(setList);
                    footerBtnLayout.setVisible(true);
                   // itemNumberCB.clear();
                    //itemNameCB.clear();
                   // orderQuntity.clear();

                    try {
                        XSSFWorkbook workbook = new XSSFWorkbook();
                        FileOutputStream out = new FileOutputStream(file);

                        XSSFSheet Spreadsheet = workbook.createSheet("request");
                        Font headerFont = workbook.createFont();
                        headerFont.setBold(true);
                        headerFont.setFontHeightInPoints((short) 12);
                        headerFont.setColor(IndexedColors.BLUE.getIndex());
                        CellStyle headerCellStyle = workbook.createCellStyle();
                        headerCellStyle.setFont(headerFont);
                        headerCellStyle.setWrapText(true);
                        headerCellStyle.setShrinkToFit(true);

                        String[] array1 = {"Item Number","Item Name","Order Qty"};
                        Row r1 = Spreadsheet.createRow(0);

                        for (int k = 0; k < array1.length; k++) {

                            Cell c = r1.createCell(k);
                            c.setCellValue(array1[k].toString());
                            c.setCellStyle(headerCellStyle);

                        }

                        for (int i = 0; i < setList.size(); i++) {
                            Row r = Spreadsheet.createRow(i + 1);

                            String s1 = setList.get(i).getItemCode();
                            String s2 = setList.get(i).getItemName();
                            String s3 = setList.get(i).getorderQuntity();

                            Cell c = r.createCell(0);
                            c.setCellValue(s1);
                            Cell c1 = r.createCell(1);
                            c1.setCellValue(s2);
                            Cell c2 = r.createCell(2);
                            c2.setCellValue(s3);


                        }

                        workbook.write(out);
                        out.close();

                        workbook.close();

                        fid.extend(downloadPdfButton);

                    } catch (Exception e) {
                        Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);


                    }
            }catch(Exception e) {
                Notification.show("Error","Pleas Enter Numb in order",Notification.Type.WARNING_MESSAGE);
            }


        }else {
            Notification.show("Error","Pleas Enter All Details",Notification.Type.WARNING_MESSAGE);
        }



    }
}

