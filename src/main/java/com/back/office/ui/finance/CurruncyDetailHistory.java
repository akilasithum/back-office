package com.back.office.ui.finance;

import java.util.List;

import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;

import com.back.office.db.DBConnection;
import com.back.office.entity.CurrencyDetails;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class CurruncyDetailHistory extends UserEntryView implements View {
    protected DBConnection connection;
    ComboBox currencyDetail;
    FilterGrid<CurrencyDetails> currencyGrid;
    private Button submitButton;

    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public CurruncyDetailHistory() {
        super();
        createMainLayout();
        connection=DBConnection.getInstance();
    }

    public void createMainLayout() {
        Label h1=new Label("Currency History");
        h1.addStyleName("headerText");

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setWidth("40%");
        firstRow.setMargin(Constants.noMargin);
        firstRow.addStyleName("report-filter-panel");

        currencyDetail=new ComboBox("Currency");
        currencyDetail.setItems(BackOfficeUtils.getCurrencyDropDownValues(false));
        currencyDetail.setRequiredIndicatorVisible(true);
        currencyDetail.setSizeFull();
        firstRow.addComponent(currencyDetail);
        currencyGrid=new FilterGrid();
        submitButton =new Button("Submit");
        submitButton.addClickListener((Button.ClickListener) ClickEvent->process());

        HorizontalLayout submitBtnLayout=new HorizontalLayout();
        submitBtnLayout.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        submitBtnLayout.setStyleName("searchButton");
        submitBtnLayout.addComponents(submitButton);

        firstRow.addComponent(submitBtnLayout);
        currencyGrid.setWidth("100%");

        Button exportToExcel=new Button();
        exportToExcel.setIcon(FontAwesome.FILE_EXCEL_O);

        Button exportPdf=new Button();
        exportPdf.setIcon(VaadinIcons.PRINT);

        HorizontalLayout optionButtonRow = new HorizontalLayout();
        optionButtonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        optionButtonRow.setSpacing(true);
        optionButtonRow.setMargin(Constants.noMargin);
        optionButtonRow.addComponents(exportToExcel,exportPdf);

        addComponents(h1,firstRow,optionButtonRow,currencyGrid);
        setComponentAlignment(optionButtonRow, Alignment.MIDDLE_RIGHT);

        currencyGrid.addColumn(CurrencyDetails::getCurrencyCode).setCaption("Currency Code");
        currencyGrid.addColumn(CurrencyDetails::getCurrencyDesc).setCaption("Description");
        currencyGrid.addColumn(CurrencyDetails::getCurrencyRate).setCaption("Rate");
        currencyGrid.addColumn(CurrencyDetails::getLastUpdateDateTime).setCaption("Last Update");

    }
    public void process() {
        if(currencyDetail.getValue()!=null&&!currencyDetail.getValue().toString().isEmpty()) {
            List<CurrencyDetails> currecyDetailsList=connection.getCurrencyDetail(currencyDetail.getValue().toString());
            currencyGrid.setItems(currecyDetailsList);
            currencyGrid.setVisible(true);
        }else {
            Notification.show("Error","Pleas input Currency Code",Type.WARNING_MESSAGE);
        }
    }
}

