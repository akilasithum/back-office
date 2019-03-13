package com.back.office.ui.salesReports;

import com.back.office.entity.CCByFlightObj;
import com.back.office.entity.CCSummaryObj;
import com.back.office.entity.Flights;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CreditCardSummaryByFlightView extends ReportCommonView{

    DateField flightDateFromDateField;
    TextField sifNoFld;
    ComboBox flightIdComboBox;

    private final String FLIGHT_DATE = "Flight Date";
    private final String CREDIT_CARD_NO = "Card No";
    private final String AMOUNT = "Amount";
    private final String SIF_NO = "SIF No";
    private final String EXPIRE_DATE = "Expire Date";
    private final String FLIGHT_ID = "Flight ID";

    protected Grid<CCByFlightObj> detailsTable;

    @Override
    protected void createMainLayout() {
        super.createMainLayout();
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(firstRow);


        Date date = new Date();
        LocalDate today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        flightDateFromDateField = new DateField(FLIGHT_DATE);
        flightDateFromDateField.setValue(today);
        firstRow.addComponent(flightDateFromDateField);

        sifNoFld = new TextField(SIF_NO);
        firstRow.addComponent(sifNoFld);

        flightIdComboBox = new ComboBox(FLIGHT_ID);
        flightIdComboBox.setItems(getFlightList());
        firstRow.addComponents(flightIdComboBox);

        detailsTable = new Grid<>();
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setSizeFull();
        tableLayout.addComponent(detailsTable);
        userFormLayout.setWidth("70%");
        createShowTableHeader();
    }

    private void createShowTableHeader(){
        detailsTable.addColumn(CCByFlightObj::getSifNo).setCaption(SIF_NO);
        detailsTable.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getFlightDate())).setCaption(FLIGHT_DATE);
        detailsTable.addColumn(CCByFlightObj::getFlightNo).setCaption(FLIGHT_ID);
        detailsTable.addColumn(CCByFlightObj::getCreditCardNumber).setCaption(CREDIT_CARD_NO);
        detailsTable.addColumn(CCByFlightObj::getExpireDate).setCaption(EXPIRE_DATE);
        detailsTable.addColumn(CCByFlightObj::getAmount).setCaption(AMOUNT);
    }

    @Override
    protected Sheet getWorkbook(Sheet sheet) {
        return null;
    }

    @Override
    protected void defineStringFields() {
        this.pageHeader = "Credit Card by Flight";
        this.reportExcelHeader = "Credit Card by Flight";
    }

    @Override
    protected void showFilterData() {
        mainTableLayout.setVisible(true);
        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        String sifNo = sifNoFld.getValue();
        Object flightNo = flightIdComboBox.getValue();
        List<CCByFlightObj> list = connection.getCCbyFlight(dateFrom,sifNo,flightNo);

        String outputStr = "Flight Date :" + BackOfficeUtils.getDateFromDateTime(dateFrom);
        filterCriteriaText.setValue(outputStr);
        detailsTable.setItems(list);
    }

    private List<String> getFlightList(){
        List<Flights> flights = (List<Flights>)connection.getAllValues("com.back.office.entity.Flights");
        return flights.stream().map(flight-> flight.getFlightName()).collect(Collectors.toList());
    }
}
