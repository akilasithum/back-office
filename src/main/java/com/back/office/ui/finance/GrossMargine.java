package com.back.office.ui.finance;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.back.office.utils.UserNotification;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.back.office.db.DBConnection;
import com.back.office.entity.ItemDetails;
import com.back.office.entity.ItemGross;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;

public class GrossMargine extends UserEntryView implements View{
    protected ComboBox serviceTypeC;
    protected Button submitButton;
    protected List<ItemDetails> itemList;
    protected Grid<ItemGross> listGrid;
    protected VerticalLayout createLayout;
    protected DBConnection connection;
    protected List<ItemGross> grossarrayList=new ArrayList();
    protected Button exportToExcell;
    protected Button print;
    protected File file=new File("grossMargin.xlsx");
    protected FileResource fir=new FileResource(file);
    protected FileDownloader fid=new FileDownloader(fir);
    protected TextField itemNo;

    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public GrossMargine() {
        super();
        createMainLayout();
        connection=DBConnection.getInstance();
        setMargin(false);
    }

    public void createMainLayout() {

        createLayout=new VerticalLayout();
        Label h1=new Label("Gross Margin");
        h1.addStyleName("headerText");
        createLayout.addComponent(h1);

        serviceTypeC=new ComboBox("Item Type");
        serviceTypeC.setDescription("Item Type");
        serviceTypeC.setItems("Order Now","Transport","Meals","Hotels","Excursions","Gift Cards");
        serviceTypeC.setEmptySelectionAllowed(false);
        serviceTypeC.setRequiredIndicatorVisible(true);

        itemNo = new TextField("Item No");
        itemNo.setDescription("Item No");
        HorizontalLayout fieldLayout = new HorizontalLayout();
        fieldLayout.setSizeFull();
        fieldLayout.setWidth("40%");
        createLayout.addComponent(fieldLayout);
        serviceTypeC.setSizeFull();
        itemNo.setSizeFull();
        fieldLayout.addComponent(serviceTypeC);
        fieldLayout.addComponent(itemNo);

        HorizontalLayout buttonRow = new HorizontalLayout();
        buttonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        buttonRow.setStyleName("searchButton");
        submitButton =new Button("Submit");
        buttonRow.addComponent(submitButton);
        fieldLayout.addComponent(buttonRow);
        fieldLayout.addStyleName("report-filter-panel");
        submitButton.addClickListener((Button.ClickListener) ClickEvent->
                processGrid());


        listGrid=new Grid();

        listGrid.setSizeFull();

        //listGrid.setVisible(false);

        exportToExcell=new Button();
        exportToExcell.setIcon(FontAwesome.FILE_EXCEL_O);

        print=new Button();
        print.setIcon(FontAwesome.PRINT);

        HorizontalLayout optionButtonRow = new HorizontalLayout();
        optionButtonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        optionButtonRow.setSpacing(true);
        optionButtonRow.setMargin(Constants.noMargin);
        optionButtonRow.addComponents(exportToExcell,print);
        createLayout.addComponent(optionButtonRow);
        createLayout.setComponentAlignment(optionButtonRow,Alignment.MIDDLE_RIGHT);
        createLayout.addComponent(listGrid);
        createLayout.setWidth("90%");
        createLayout.setMargin(false);

        listGrid.addColumn(ItemGross::getItemId).setCaption("Item #");
        listGrid.addColumn(ItemGross::getItemDescription).setCaption("Description");
        listGrid.addColumn(ItemGross::getBasePrice).setCaption("Selling Price");
        listGrid.addColumn(ItemGross::getCostPrice).setCaption("Cost");
        listGrid.addColumn(ItemGross::getMargine).setCaption("Margin");
        listGrid.addColumn(bean-> BackOfficeUtils.formatDecimals(bean.getMarginPresentage())).setCaption("Percentage");

        addComponent(createLayout);

    }

    public void processGrid() {
        if(serviceTypeC.getValue()!=null&&!serviceTypeC.getValue().toString().isEmpty()) {

            itemList=connection.getItemGross(serviceTypeC.getValue().toString(),itemNo.getValue());
            listGrid.setVisible(true);
            grossarrayList.clear();
            ItemGross itemgrossDetail;
            for(int i=0;i<itemList.size();i++) {

                float baseCurrun=itemList.get(i).getBasePrice();
                float costCurrun=itemList.get(i).getCostPrice();
                String itemIdSet=itemList.get(i).getItemCode();
                String itemDEscription=itemList.get(i).getItemName();
                float marginList=(baseCurrun-costCurrun);
                float marginPresenList=((marginList)/baseCurrun)*100;
                itemgrossDetail=new ItemGross();

                itemgrossDetail.setItemId(itemIdSet);
                itemgrossDetail.setItemDescription(itemDEscription);
                itemgrossDetail.setBasePrice(baseCurrun);
                itemgrossDetail.setCostPrice(costCurrun);
                itemgrossDetail.setMargine(marginList);
                itemgrossDetail.setMarginPresentage(marginPresenList);
                grossarrayList.add(itemgrossDetail);

            }

            listGrid.setItems(grossarrayList);

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

                String[] array = {"Item Id","Item Description","Base Price","Cost Price","Margin","Margin Percentage"};
                Row r1 = Spreadsheet.createRow(0);

                for (int k = 0; k < array.length; k++) {

                    Cell c = r1.createCell(k);
                    c.setCellValue(array[k].toString());
                    c.setCellStyle(headerCellStyle);

                }

                for (int i = 0; i < grossarrayList.size(); i++) {
                    Row r = Spreadsheet.createRow(i + 1);

                    String s1 = grossarrayList.get(i).getItemId();
                    String s2 = grossarrayList.get(i).getItemDescription();
                    float s3 = grossarrayList.get(i).getBasePrice();
                    float s4 = grossarrayList.get(i).getCostPrice();
                    float s5 = grossarrayList.get(i).getMargine();
                    float s6 = grossarrayList.get(i).getMarginPresentage();


                    Cell c = r.createCell(0);
                    c.setCellValue(s1);
                    Cell c1 = r.createCell(1);
                    c1.setCellValue(s2);
                    Cell c2 = r.createCell(2);
                    c2.setCellValue(s3);
                    Cell c3 = r.createCell(3);
                    c3.setCellValue(s4);
                    Cell c4 = r.createCell(4);
                    c4.setCellValue(s5);
                    Cell c5 = r.createCell(5);
                    c5.setCellValue(s6);


                }

                workbook.write(out);
                out.close();

                workbook.close();

                fid.extend(exportToExcell);

            } catch (Exception e) {
                UserNotification.show("Error","Something wrong", "warning",UI.getCurrent());

            }
            exportToExcell.setVisible(true);
            print.setVisible(true);

        }else {
            UserNotification.show("Error","Please select service type", "warning",UI.getCurrent());

        }
    }
}
