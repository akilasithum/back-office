package com.back.office.ui.finance;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
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
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class GrossMargine extends UserEntryView implements View{
    protected ComboBox serviceTypeC;
    protected Button process;
    protected List<ItemDetails> itemList;
    protected Grid<ItemGross> listGrid;
    protected VerticalLayout createLayout;
    protected DBConnection connection;
    protected List<ItemGross> chakedList;
    protected ItemGross itemgrossDetail;
    protected List<ItemGross> grossarrayList=new ArrayList();
    protected Button exportToExcell;
    protected Button print;
    protected File file=new File("grossMargin.xlsx");
    protected FileResource fir=new FileResource(file);
    protected FileDownloader fid=new FileDownloader(fir);





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
        setMargin(true);

    }

    public void createMainLayout() {

        createLayout=new VerticalLayout();
        Label h1=new Label("Gross Margin");
        h1.addStyleName("headerText");
        createLayout.addComponent(h1);

        serviceTypeC=new ComboBox("Service Type");
        serviceTypeC.setDescription("Service Type");
        serviceTypeC.setItems("BOB","DTF","VRT");
        serviceTypeC.setEmptySelectionAllowed(false);
        serviceTypeC.setRequiredIndicatorVisible(true);
        createLayout.addComponent(serviceTypeC);

        HorizontalLayout buttonList=new HorizontalLayout();
        createLayout.addComponent(buttonList);

        process=new Button("Submit");
        buttonList.addComponent(process);

        process.addClickListener((Button.ClickListener) ClickEvent->
                processGrid());


        listGrid=new Grid();
        createLayout.addComponent(listGrid);
        createLayout.addComponent(listGrid);
        listGrid.setSizeFull();
        listGrid.setWidth("80%");

        //listGrid.setVisible(false);

        exportToExcell=new Button("Export To Excel");
        buttonList.addComponent(exportToExcell);
        exportToExcell.setVisible(false);


        print=new Button("Print");
        buttonList.addComponent(print);
        print.setVisible(false);
        listGrid.setWidth("50%");
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
            itemList=connection.getItemGross(serviceTypeC.getValue().toString());
            listGrid.setVisible(true);

            // listGrid.removeAllColumns();
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
                Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);

            }
            exportToExcell.setVisible(true);
            print.setVisible(true);

        }else {
            Notification.show("Error","Please select service type",Notification.Type.WARNING_MESSAGE);

        }
    }
}
