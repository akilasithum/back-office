package com.back.office.ui.message;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.back.office.entity.PreOrderItem;
import com.back.office.entity.UserComment;
import com.back.office.framework.UserEntryView;
import com.back.office.ui.salesReports.ReportCommonView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.vaadin.ui.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.vaadin.addons.filteringgrid.FilterGrid;

import com.back.office.db.DBConnection;
import com.back.office.entity.FaMessage;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.themes.ValoTheme;

public class FAMessagesView extends ReportCommonView {

    protected VerticalLayout createLayout;
    protected FilterGrid<UserComment> faMessageFilterGrid;
    protected List<UserComment> flightDetList;
    protected Button clear;
    protected DateField fromDateText;
    protected DateField toDateText;
    protected ComboBox flightNumberList;


    @Override
    protected void createMainLayout() {
        super.createMainLayout();
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

        fromDateText=new DateField("From");
        fromDateText.setRequiredIndicatorVisible(true);
        fromDateText.setStyleName("datePickerStyle");
        fromDateText.setSizeFull();
        fromDateText.setValue(today);

        toDateText=new DateField("To");
        toDateText.setStyleName("datePickerStyle");
        toDateText.setRequiredIndicatorVisible(true);
        toDateText.setSizeFull();
        toDateText.setValue(today);

        flightNumberList=new ComboBox("Flight No");
        flightNumberList.setDescription("Flight No");
        flightNumberList.setItems(connection.getFlightsNoList());
        flightNumberList.setEmptySelectionAllowed(false);
        flightNumberList.setSizeFull();

        firstRow.addComponents(fromDateText,toDateText,flightNumberList,buttonRow);


        faMessageFilterGrid =new FilterGrid();
        faMessageFilterGrid.setSizeFull();
        faMessageFilterGrid.setWidth("90%");
        tableLayout.addComponent(faMessageFilterGrid);
        createShowTableHeader();
        optionButtonRow.setVisible(false);
    }

    private void createShowTableHeader(){
        faMessageFilterGrid.addColumn(UserComment::getFlightNo).setCaption("Flight No").setExpandRatio(1);
        faMessageFilterGrid.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getFlightDate())).setCaption("Dep Date")
                .setExpandRatio(1);
        faMessageFilterGrid.addColumn(UserComment::getUserId).setCaption("FA Name").setExpandRatio(1);
        faMessageFilterGrid.addColumn(UserComment::getArea).setCaption("Functional Area").setExpandRatio(1);
        faMessageFilterGrid.addColumn(UserComment::getComment).setCaption("Message").setWidth(400).setExpandRatio(4);
    }



    @Override
    protected void showFilterData() {

        if(fromDateText.getValue()!=null&&!fromDateText.getValue().toString().isEmpty()&&toDateText.getValue()!=null&&!toDateText.getValue().toString().isEmpty()) {

            String flightNumberField=flightNumberList.getValue()== null || flightNumberList.getValue().toString().isEmpty() ?
                    null : String.valueOf(flightNumberList.getValue());
            Date craftDateFieldFrom=Date.from(fromDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date craftDateFieldTo=Date.from(toDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            faMessageFilterGrid.setVisible(true);

            flightDetList=connection.getFaMessages(flightNumberField,craftDateFieldFrom,craftDateFieldTo);

            faMessageFilterGrid.setItems(flightDetList);

        }else {
            Notification.show("Error","Pleas enter date range",Notification.Type.WARNING_MESSAGE);

        }


    }

    @Override
    protected PdfPTable getPdfTable(PdfPTable sheet, Font redFont) {
        return null;
    }

    @Override
    protected Sheet getWorkbook(Sheet sheet) {
        return null;
    }

    @Override
    protected void defineStringFields() {
        this.pageHeader = "HHC FA";
        this.reportExcelHeader = "HHC FA";
    }

}

